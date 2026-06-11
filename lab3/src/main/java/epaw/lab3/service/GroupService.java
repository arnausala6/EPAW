package epaw.lab3.service;

import epaw.lab3.model.Group;
import epaw.lab3.model.User;
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

public class GroupService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private static GroupService instance;

    public GroupService() {
        this.groupRepository = GroupRepository.getInstance();
        this.userRepository = UserRepository.getInstance();
    }

    public List<Group> getTopTenGroups() {
        return groupRepository.getTopTenGroups();
    }

    public Group getGroupById(Integer id) {
        return groupRepository.findById(id);
    }

    public List<Group> getUserGroups(Integer userId) {
        return groupRepository.findByUserId(userId);
    }

    public List<Group> getSuggestedGroups(Integer userId) {
        return groupRepository.findSuggestionsForUser(userId);
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
