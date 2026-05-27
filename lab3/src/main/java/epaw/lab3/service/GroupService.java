package epaw.lab3.service;

import epaw.lab3.model.Group;
import epaw.lab3.repository.GroupRepository;

import java.util.List;

public class GroupService {

    private final GroupRepository groupRepository;
    private static GroupService instance;

    public GroupService() {
        this.groupRepository = new GroupRepository();
    }

    public List<Group> getTopTenGroups() {
        return groupRepository.getTopTenGroups();
    }

    public Group getGroupById(int id) {
        return groupRepository.findById(id);
    }

    public void createGroup(Group group, int creatorId) {

        group.setCreatorId(creatorId);

        if (group.getParticipants() <= 0) {
            group.setParticipants(0);
        }

        groupRepository.save(group);
    }

    public static synchronized GroupService getInstance() {
        if (instance == null) {
            instance = new GroupService();
        }
        return instance;
    }
}