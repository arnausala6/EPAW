package epaw.lab3.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class Post implements Serializable {

    private static final long serialVersionUID = 1L;

    private int postId;
    private String content;
    private LocalDateTime dateOfCreation;
    private Integer votes;
    private int userId;
    private int groupId;

    public Post() {}
    
    // PostId
    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    // Content
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    // DateOfCreation
    public LocalDateTime getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(LocalDateTime dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    // Votes
    public Integer getVotes() {
        return votes;
    }

    public void setVotes(Integer votes) {
        this.votes = votes;
    }

    // UserId
    public int getUserId() {
        return userId;
    }

    public void setUserId(int user_id) {
        this.userId = user_id;
    }

    // GroupId
    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int group_id) {
        this.groupId = group_id;
    }

}
