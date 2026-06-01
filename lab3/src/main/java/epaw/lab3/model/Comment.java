package epaw.lab3.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class Comment implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private int commentId;
    private String content;
    private Integer userId;
    private Integer groupId;

    // Constructor vacío
    public Comment() {}

    // --- GETTERS & SETTERS ---

    // CommentId
    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    // Content
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    // UserId
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    // GroupId
    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }
}
