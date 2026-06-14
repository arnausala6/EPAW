package epaw.lab3.service;

import epaw.lab3.model.Group;
import epaw.lab3.model.GroupJoinRequest;
import epaw.lab3.model.User;
import epaw.lab3.repository.GroupJoinRequestRepository;
import epaw.lab3.repository.GroupRepository;
import epaw.lab3.repository.UserRepository;
import jakarta.servlet.http.Part;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GroupService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final GroupJoinRequestRepository joinRequestRepository;
    private static GroupService instance;

    public GroupService() {
        this.groupRepository = GroupRepository.getInstance();
        this.userRepository = UserRepository.getInstance();
        this.joinRequestRepository = GroupJoinRequestRepository.getInstance();
    }

    public List<Group> getTopTenGroups() {
        return groupRepository.getTopTenGroups();
    }

    public Group getGroupById(Integer id) {
        return groupRepository.findById(id);
    }

    public boolean isAdmin(User user) {
        return user != null && "admin".equals(user.getRole());
    }

    public boolean isGroupOwner(Group group, User user) {
        return group != null && user != null
                && group.getOwner() != null
                && group.getOwner().equals(user.getUsername());
    }

    public boolean canViewGroupPosts(Group group, User user) {
        if (group == null || user == null) {
            return false;
        }
        if (group.isBlocked()) {
            return false;
        }
        if (isAdmin(user)) {
            return true;
        }
        return !"private".equals(group.getPrivacy()) || isGroupMember(user, group.getGroupId());
    }

    public Map<String, String> blockGroup(int groupId, User admin, String password, String reason, int[] memberIds) {
        Map<String, String> errors = new HashMap<>();

        if (admin == null || !"admin".equals(admin.getRole())) {
            errors.put("groupId", "Only administrators can block groups.");
            return errors;
        }

        if (reason == null || reason.trim().isEmpty()) {
            errors.put("reason", "A reason is required.");
        } else if (reason.length() > 300) {
            errors.put("reason", "Reason cannot exceed 300 characters.");
        }

        if (password == null || password.isEmpty()) {
            errors.put("password", "Password is required.");
        } else if (!userRepository.verifyPassword(admin.getId(), password)) {
            errors.put("password", "Incorrect password.");
        }

        Group group = groupRepository.findById(groupId);
        if (group == null) {
            errors.put("groupId", "Group not found.");
            return errors;
        }

        if (group.isBlocked()) {
            errors.put("groupId", "This group is already blocked.");
        }

        if (!errors.isEmpty()) {
            return errors;
        }

        groupRepository.blockGroup(groupId, reason.trim());

        if (memberIds != null) {
            for (int memberId : memberIds) {
                if (memberId == admin.getId()) {
                    continue;
                }
                if (!userRepository.checkUserInGroup(memberId, groupId)) {
                    continue;
                }
                if (!userRepository.isUserBlocked(admin.getId(), memberId)) {
                    userRepository.saveBlock(admin.getId(), memberId, reason.trim(), true);
                }
            }
        }

        return errors;
    }

    public boolean isGroupMember(User user, int groupId) {
        if (user == null) {
            return false;
        }
        Group group = groupRepository.findById(groupId);
        if (group != null && isGroupOwner(group, user)) {
            return true;
        }
        return userRepository.checkUserInGroup(user.getId(), groupId);
    }

    public List<Group> getUserGroups(Integer userId) {
        return groupRepository.findByUserId(userId);
    }

    public List<Group> getSuggestedGroups(Integer userId) {
        List<Group> groups = groupRepository.findSuggestionsForUser(userId);
        attachPendingJoinFlags(groups, userId);
        return groups;
    }

    public boolean hasPendingJoinRequest(int userId, int groupId) {
        return joinRequestRepository.hasPendingRequest(userId, groupId);
    }

    public List<GroupJoinRequest> getPendingJoinRequests(int groupId, User owner) {
        Group group = groupRepository.findById(groupId);
        if (group == null || !isGroupOwner(group, owner)) {
            return List.of();
        }
        return joinRequestRepository.findPendingByGroupId(groupId);
    }

    private void attachPendingJoinFlags(List<Group> groups, int userId) {
        Set<Integer> pendingGroupIds = joinRequestRepository.findPendingGroupIdsForUser(userId);
        for (Group group : groups) {
            group.setPendingJoinRequest(pendingGroupIds.contains(group.getGroupId()));
        }
    }

    public Map<String, String> requestToJoinGroup(int groupId, User user) {
        Map<String, String> errors = new HashMap<>();

        Group group = groupRepository.findById(groupId);
        if (group == null) {
            errors.put("groupId", "Group not found.");
            return errors;
        }

        if (group.isBlocked()) {
            errors.put("groupId", "This group has been blocked.");
            return errors;
        }

        if (!"private".equals(group.getPrivacy())) {
            errors.put("groupId", "Join requests are only available for private groups.");
            return errors;
        }

        if (userRepository.checkUserInGroup(user.getId(), groupId)) {
            errors.put("groupId", "You are already a member of this group.");
            return errors;
        }

        if (joinRequestRepository.hasPendingRequest(user.getId(), groupId)) {
            errors.put("groupId", "You already have a pending join request for this group.");
            return errors;
        }

        joinRequestRepository.insert(user.getId(), groupId);
        return errors;
    }

    public Map<String, String> acceptJoinRequest(int groupId, int requestUserId, User owner) {
        Map<String, String> errors = new HashMap<>();

        Group group = groupRepository.findById(groupId);
        if (group == null) {
            errors.put("groupId", "Group not found.");
            return errors;
        }

        if (!isGroupOwner(group, owner)) {
            errors.put("groupId", "Only the group owner can accept join requests.");
            return errors;
        }

        if (group.isBlocked()) {
            errors.put("groupId", "This group has been blocked.");
            return errors;
        }

        if (!joinRequestRepository.hasPendingRequest(requestUserId, groupId)) {
            errors.put("userId", "Join request not found.");
            return errors;
        }

        if (!userRepository.existsById(requestUserId)) {
            errors.put("userId", "User not found.");
            return errors;
        }

        userRepository.saveUserJoiningGroup(requestUserId, groupId);
        joinRequestRepository.delete(requestUserId, groupId);
        return errors;
    }

    public Map<String, String> rejectJoinRequest(int groupId, int requestUserId, User owner) {
        Map<String, String> errors = new HashMap<>();

        Group group = groupRepository.findById(groupId);
        if (group == null) {
            errors.put("groupId", "Group not found.");
            return errors;
        }

        if (!isGroupOwner(group, owner)) {
            errors.put("groupId", "Only the group owner can reject join requests.");
            return errors;
        }

        if (group.isBlocked()) {
            errors.put("groupId", "This group has been blocked.");
            return errors;
        }

        if (!joinRequestRepository.hasPendingRequest(requestUserId, groupId)) {
            errors.put("userId", "Join request not found.");
            return errors;
        }

        joinRequestRepository.delete(requestUserId, groupId);
        return errors;
    }

    public Map<String, String> createGroup(Group group, User creator, Part filePart) {
        Map<String, String> errors = new HashMap<>();

        if (group.getGroupName() == null || group.getGroupName().trim().isEmpty()) {
            errors.put("groupName", "Group name is required.");
        } else if (group.getGroupName().length() > 50) {
            errors.put("groupName", "Group name cannot exceed 50 characters.");
        } else if (groupRepository.groupExists(group.getGroupName().trim())) {
            errors.put("groupName", "A group with this name already exists.");
        }

        if (group.getDescription() != null && group.getDescription().length() > 300) {
            errors.put("groupDescription", "Description cannot exceed 300 characters.");
        }

        String privacy = group.getPrivacy();
        if (privacy == null || (!privacy.equals("public") && !privacy.equals("private"))) {
            errors.put("groupVisibility", "Visibility must be public or private.");
        }

        if (filePart != null && filePart.getSize() > 0) {
            long maxBytes = 2L * 1024 * 1024;
            if (filePart.getSize() > maxBytes) {
                errors.put("groupImage", "The group image cannot exceed 2MB.");
            }
        }

        if (!errors.isEmpty()) {
            return errors;
        }

        int groupId = groupRepository.getNextGroupId();
        group.setGroupId(groupId);
        group.setGroupName(group.getGroupName().trim());
        group.setOwner(creator.getUsername());
        group.setCreatorId(creator.getId());
        group.setDateOfCreation(LocalDateTime.now());
        group.setParticipants(1);

        if (filePart != null && filePart.getSize() > 0) {
            group.setGroupPicture(saveGroupPicture(filePart, groupId));
        } else {
            group.setGroupPicture("assets/icons/imagen-suave.png");
        }

        groupRepository.insert(group);
        userRepository.saveUserJoiningGroup(creator.getId(), groupId);

        return errors;
    }

    public Map<String, String> updateGroup(int groupId, Group updates, User user, Part filePart) {
        Map<String, String> errors = new HashMap<>();

        Group existing = groupRepository.findById(groupId);
        if (existing == null) {
            errors.put("groupId", "Group not found.");
            return errors;
        }

        if (!userRepository.checkUserInGroup(user.getId(), groupId)) {
            errors.put("groupId", "You are not a member of this group.");
            return errors;
        }

        if (!isGroupOwner(existing, user)) {
            errors.put("groupId", "Only the group owner can edit this group.");
            return errors;
        }

        if (existing.isBlocked()) {
            errors.put("groupId", "This group has been blocked.");
            return errors;
        }

        if (updates.getGroupName() == null || updates.getGroupName().trim().isEmpty()) {
            errors.put("groupName", "Group name is required.");
        } else if (updates.getGroupName().length() > 50) {
            errors.put("groupName", "Group name cannot exceed 50 characters.");
        } else if (groupRepository.groupNameExistsForOther(updates.getGroupName().trim(), groupId)) {
            errors.put("groupName", "A group with this name already exists.");
        }

        if (updates.getDescription() != null && updates.getDescription().length() > 300) {
            errors.put("groupDescription", "Description cannot exceed 300 characters.");
        }

        String privacy = updates.getPrivacy();
        if (privacy == null || (!privacy.equals("public") && !privacy.equals("private"))) {
            errors.put("groupVisibility", "Visibility must be public or private.");
        }

        if (filePart != null && filePart.getSize() > 0) {
            long maxBytes = 2L * 1024 * 1024;
            if (filePart.getSize() > maxBytes) {
                errors.put("groupImage", "The group image cannot exceed 2MB.");
            }
        }

        if (!errors.isEmpty()) {
            return errors;
        }

        existing.setGroupName(updates.getGroupName().trim());
        existing.setDescription(updates.getDescription());
        existing.setPrivacy(privacy);

        if (filePart != null && filePart.getSize() > 0) {
            String picture = saveGroupPicture(filePart, groupId);
            if (picture != null) {
                existing.setGroupPicture(picture);
            }
        }

        groupRepository.update(existing);
        return errors;
    }

    public Map<String, String> joinGroup(int groupId, User user) {
        Map<String, String> errors = new HashMap<>();

        Group group = groupRepository.findById(groupId);
        if (group == null) {
            errors.put("groupId", "Group not found.");
            return errors;
        }

        if (group.isBlocked()) {
            errors.put("groupId", "This group has been blocked.");
            return errors;
        }

        if (userRepository.checkUserInGroup(user.getId(), groupId)) {
            errors.put("groupId", "You are already a member of this group.");
            return errors;
        }

        if ("private".equals(group.getPrivacy())) {
            errors.put("groupId", "You cannot join a private group.");
            return errors;
        }

        userRepository.saveUserJoiningGroup(user.getId(), groupId);
        return errors;
    }

    public Map<String, String> leaveOrDeleteGroup(int groupId, User user) {
        Map<String, String> errors = new HashMap<>();

        Group group = groupRepository.findById(groupId);
        if (group == null) {
            errors.put("groupId", "Group not found.");
            return errors;
        }

        if (!userRepository.checkUserInGroup(user.getId(), groupId)) {
            errors.put("groupId", "You are not a member of this group.");
            return errors;
        }

        int memberCount = group.getMemberCount() != null ? group.getMemberCount() : 0;
        if (memberCount <= 1) {
            groupRepository.delete(groupId);
        } else {
            userRepository.saveUserLeavingGroup(user.getId(), groupId);
        }

        return errors;
    }

    public String saveGroupPicture(Part filePart, int groupId) {
        if (filePart == null || filePart.getSize() <= 0) {
            return null;
        }
        try {
            String fileName = filePart.getSubmittedFileName();
            String extension = fileName.substring(fileName.lastIndexOf("."));
            String newFileName = "group_" + groupId + extension;
            String resourcesDir = "EXTERNAL_RESOURCES";
            Files.createDirectories(Paths.get(resourcesDir));
            try (InputStream input = filePart.getInputStream()) {
                Files.copy(input, Paths.get(resourcesDir, newFileName), StandardCopyOption.REPLACE_EXISTING);
            }
            return newFileName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static synchronized GroupService getInstance() {
        if (instance == null) {
            instance = new GroupService();
        }
        return instance;
    }
}
