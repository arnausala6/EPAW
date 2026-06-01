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
    private int votes;
    private Integer user_id;
    private Integer group_id;

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
    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    // UserId
    public Integer getUserId() {
        return user_id;
    }

    public void setUserId(Integer user_id) {
        this.user_id = user_id;
    }

    // GroupId
    public Integer getGroupId() {
        return group_id;
    }

    public void setGroupId(Integer group_id) {
        this.group_id = group_id;
    }

}
