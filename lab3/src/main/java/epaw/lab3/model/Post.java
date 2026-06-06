package epaw.lab3.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class Post implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer postId;
    private String content;
    private LocalDateTime dateOfCreation;
    private Integer votes;
    private Integer userId;
    private Integer groupId;
    private Integer responseId;

    public Post() {
        responseId = null;
    }
    
    // PostId
    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    // ResponseId
    public Integer getResponseId() {
        return responseId;
    }

    public void setResponseId(Integer responseId) {
        this.responseId = responseId;
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
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer user_id) {
        this.userId = user_id;
    }

    // GroupId
    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer group_id) {
        this.groupId = group_id;
    }

}
