package epaw.lab3.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class Group implements Serializable {

    private static final long serialVersionUID = 1L;

    private int groupId;
    private String groupName;
    private String description;
    private String groupPicture;
    private LocalDateTime dateOfCreation;
    private Integer creatorId;
    private int participants;

    private Set<User> groupParticipants = new HashSet<>();

    public Group() {}

    // ID
    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    // Name
    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    // Description
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Picture
    public String getGroupPicture() {
        return groupPicture;
    }

    public void setGroupPicture(String groupPicture) {
        this.groupPicture = groupPicture;
    }

    // Date of creation
    public LocalDateTime getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(LocalDateTime dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    // Creator
    public Integer getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

    // Participants
    public int getParticipants() {
        return participants;
    }

    public void setParticipants(int participants) {
        this.participants = participants;
    }

    public Set<User> getGroupParticipants() {
        return groupParticipants;
    }

    public void setGroupParticipants(Set<User> groupParticipants) {
        this.groupParticipants = groupParticipants;
    }
}
