package epaw.lab3.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.HashSet;
import java.util.Set;

public class Post implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer postId;
    private String content;
    private LocalDateTime dateOfCreation;
    private Integer votes;
    private Integer upvotes;
    private Integer downvotes;
    private Integer commentCount;
    private String postPicture;
    private String profilePicture;
    private Integer userId;
    private Integer groupId;
    private Integer responseId;
    private String username;
    private String groupName;
    private boolean blocked;
    private String blockReason;
    private boolean authorBanned;



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

    public Integer getUpvotes() {
        return upvotes;
    }

    public void setUpvotes(Integer upvotes) {
        this.upvotes = upvotes;
    }

    public Integer getDownvotes() {
        return downvotes;
    }

    public void setDownvotes(Integer downvotes) {
        this.downvotes = downvotes;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public String getPostPicture() {
        return postPicture;
    }

    public void setPostPicture(String postPicture) {
        this.postPicture = postPicture;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
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

    // Username
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // GroupName
    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getFormattedDate() {
        if (dateOfCreation == null) {
            return "";
        }
        return dateOfCreation.atZone(ZoneOffset.UTC)
                .withZoneSameInstant(ZoneId.of("Europe/Madrid"))
                .format(DateTimeFormatter.ofPattern("d MMM yyyy · HH:mm", Locale.forLanguageTag("es-ES")));
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public String getBlockReason() {
        return blockReason;
    }

    public void setBlockReason(String blockReason) {
        this.blockReason = blockReason;
    }

    public boolean isAuthorBanned() {
        return authorBanned;
    }

    public void setAuthorBanned(boolean authorBanned) {
        this.authorBanned = authorBanned;
    }

}
