package epaw.lab3.service;

import epaw.lab3.model.Group;
import epaw.lab3.model.Post;
import epaw.lab3.model.User;
import epaw.lab3.repository.PostRepository;
import epaw.lab3.repository.UserRepository;
import epaw.lab3.repository.VoteRepository;

import jakarta.servlet.http.Part;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final VoteRepository voteRepository;
    private final GroupService groupService;
    private static PostService instance;

    public PostService() {
        this.postRepository = PostRepository.getInstance();
        this.userRepository = UserRepository.getInstance();
        this.voteRepository = VoteRepository.getInstance();
        this.groupService = GroupService.getInstance();
    }

    public Map<String, String> createPost(Post post) {
        return createPost(post, null);
    }

    public Map<String, String> createPost(Post post, Part filePart) {
        Map<String, String> errors = new HashMap<>();
        if (post.getGroupId() == null) {
            errors.put("groupId", "Post must be part of a group.");
        }
        if (post.getUserId() == null) {
            errors.put("userId", "User cannot be null.");
        }
        if (post.getContent() == null || post.getContent().trim().isEmpty()) {
            errors.put("content", "You cannot publish a post without content.");
        }
        if (post.getGroupId() != null && post.getUserId() != null
                && !userRepository.checkUserInGroup(post.getUserId(), post.getGroupId())) {
            errors.put("groupId", "You are not a member of this group.");
        }
        if (post.getResponseId() != null && !postRepository.postExistsById(post.getResponseId())) {
            errors.put("responseId", "Response post does not exist.");
        }

        if (filePart != null && filePart.getSize() > 0) {
            long maxBytes = 2L * 1024 * 1024;
            if (filePart.getSize() > maxBytes) {
                errors.put("postPicture", "The post image cannot exceed 2MB.");
            } else {
                String fileName = filePart.getSubmittedFileName();
                if (fileName != null) {
                    String lower = fileName.toLowerCase();
                    if (!lower.endsWith(".jpg") && !lower.endsWith(".jpeg")
                            && !lower.endsWith(".png") && !lower.endsWith(".webp")
                            && !lower.endsWith(".gif")) {
                        errors.put("postPicture", "Image must be JPG, PNG, WEBP or GIF.");
                    }
                }
            }
        }

        if (errors.isEmpty()) {
            int postId = postRepository.publishPost(post);
            if (postId > 0 && filePart != null && filePart.getSize() > 0) {
                String picture = savePostPicture(filePart, postId);
                if (picture != null) {
                    postRepository.setPostPicture(postId, picture);
                }
            }
        }

        return errors;
    }

    public static synchronized PostService getInstance() {
        if (instance == null) {
            instance = new PostService();
        }
        return instance;
    }

    public List<Post> getTimelineByUserId(Integer userId) {
        List<Post> posts = postRepository.getTimelineByUserId(userId);
        attachUserVotes(posts, userId);
        return posts;
    }

    public List<Post> getTrendingPosts(Integer userId) {
        List<Post> posts = postRepository.getTrendingPosts(userId);
        if (userId != null) {
            attachUserVotes(posts, userId);
        }
        return posts;
    }

    public List<Post> getPublicTrendingPosts() {
        return postRepository.getPublicTrendingPosts();
    }

    public List<Post> getPostsByGroupId(int groupId, int userId) {
        List<Post> posts = postRepository.findByGroupId(groupId, userId);
        attachUserVotes(posts, userId);
        return posts;
    }

    public void attachUserVotes(List<Post> posts, int userId) {
        if (posts == null || posts.isEmpty()) {
            return;
        }
        List<Integer> postIds = posts.stream()
                .map(Post::getPostId)
                .collect(Collectors.toList());
        Map<Integer, Integer> userVotes = voteRepository.findUserVotesForPosts(userId, postIds);
        for (Post post : posts) {
            post.setUserVote(userVotes.get(post.getPostId()));
        }
    }

    public VoteResult votePost(int userId, int postId, int voteType) {
        if (voteType != 1 && voteType != -1) {
            return VoteResult.error("Invalid vote type.");
        }

        Optional<Post> postOpt = postRepository.findById(postId);
        if (postOpt.isEmpty()) {
            return VoteResult.error("Post not found.");
        }

        Post post = postOpt.get();

        Optional<Integer> currentVote = voteRepository.findUserVote(userId, postId);
        Integer resultingVote;
        int upvotesDelta = 0;
        int downvotesDelta = 0;
        int votesDelta = 0;
        if (currentVote.isEmpty()) {
            // no vote yet → insert
            voteRepository.insertVote(userId, postId, voteType);
            resultingVote = voteType;
            if (voteType == 1) {
                upvotesDelta = 1;
                votesDelta = 1;
            } else {
                downvotesDelta = 1;
                votesDelta = -1;
            }
        } else if (currentVote.get().intValue() == voteType) {
            // same vote clicked → toggle off
            voteRepository.deleteVote(userId, postId);
            resultingVote = null;
            if (voteType == 1) {
                upvotesDelta = -1;
                votesDelta = -1;
            } else {
                downvotesDelta = -1;
                votesDelta = 1;
            }
        } else {
            // opposite vote clicked → switch
            voteRepository.updateVote(userId, postId, voteType);
            resultingVote = voteType;
            if (voteType == 1) {
                upvotesDelta = 1;
                downvotesDelta = -1;
                votesDelta = 2;
            } else {
                upvotesDelta = -1;
                downvotesDelta = 1;
                votesDelta = -2;
            }
        }

        voteRepository.applyVoteDeltas(postId, upvotesDelta, downvotesDelta, votesDelta);
        int[] counts = voteRepository.getPostCounts(postId);
        return VoteResult.ok(counts[0], counts[1], counts[2], resultingVote);
    }

    public Optional<Post> getPostById(int postId) {
        return postRepository.findById(postId);
    }

    public List<Post> getCommentsByPostId(int postId) {
        return postRepository.findCommentsByPostId(postId);
    }

    public Map<String, String> addComment(int parentPostId, User user, String content) {
        Map<String, String> errors = new HashMap<>();

        if (content == null || content.trim().isEmpty()) {
            errors.put("content", "Comment cannot be empty.");
        } else if (content.length() > 500) {
            errors.put("content", "Comment cannot exceed 500 characters.");
        }

        Optional<Post> parentOpt = postRepository.findById(parentPostId);
        if (parentOpt.isEmpty()) {
            errors.put("postId", "Post not found.");
            return errors;
        }
        Post parent = parentOpt.get();
        if (!userRepository.checkUserInGroup(user.getId(), parent.getGroupId())) {
            errors.put("postId", "You are not a member of this group.");
            return errors;
        }

        if (errors.isEmpty()) {
            Post comment = new Post();
            comment.setUserId(user.getId());
            comment.setContent(content.trim());
            comment.setGroupId(parent.getGroupId());
            comment.setResponseId(parentPostId);
            postRepository.publishPost(comment);
            postRepository.incrementCommentCount(parentPostId);
        }

        return errors;
    }

    public Map<String, String> editComment(int commentId, User user, String content) {
        Map<String, String> errors = new HashMap<>();

        if (content == null || content.trim().isEmpty()) {
            errors.put("content", "Comment cannot be empty.");
        } else if (content.length() > 500) {
            errors.put("content", "Comment cannot exceed 500 characters.");
        }

        Optional<Post> commentOpt = postRepository.findById(commentId);
        if (commentOpt.isEmpty()) {
            errors.put("commentId", "Comment not found.");
            return errors;
        }

        Post comment = commentOpt.get();
        if (comment.getResponseId() == null) {
            errors.put("commentId", "This post is not a comment.");
            return errors;
        }
        if (!comment.getUserId().equals(user.getId())) {
            errors.put("commentId", "You can only edit your own comments.");
            return errors;
        }

        if (!errors.isEmpty()) {
            return errors;
        }

        postRepository.updateComment(commentId, content.trim());
        return errors;
    }

    public Map<String, String> deleteComment(int commentId, User user) {
        Map<String, String> errors = new HashMap<>();

        Optional<Post> commentOpt = postRepository.findById(commentId);
        if (commentOpt.isEmpty()) {
            errors.put("commentId", "Comment not found.");
            return errors;
        }

        Post comment = commentOpt.get();
        if (comment.getResponseId() == null) {
            errors.put("commentId", "This post is not a comment.");
            return errors;
        }
        if (!comment.getUserId().equals(user.getId())) {
            errors.put("commentId", "You can only delete your own comments.");
            return errors;
        }

        if (!postRepository.deleteComment(commentId)) {
            errors.put("commentId", "Could not delete this comment.");
            return errors;
        }

        postRepository.decrementCommentCount(comment.getResponseId());
        return errors;
    }

    public Map<String, String> editPost(int postId, User user, String content, Part filePart, boolean removeImage) {
        Map<String, String> errors = new HashMap<>();

        if (content == null || content.trim().isEmpty()) {
            errors.put("content", "Post content cannot be empty.");
        }

        Optional<Post> postOpt = postRepository.findById(postId);
        if (postOpt.isEmpty()) {
            errors.put("postId", "Post not found.");
            return errors;
        }

        Post post = postOpt.get();
        if (post.getResponseId() != null) {
            errors.put("postId", "This is not a top-level post.");
            return errors;
        }
        if (!post.getUserId().equals(user.getId())) {
            errors.put("postId", "You can only edit your own posts.");
            return errors;
        }

        Group group = groupService.getGroupById(post.getGroupId());
        boolean hasExistingImage = post.getPostPicture() != null && !post.getPostPicture().isBlank();
        boolean hasNewImage = filePart != null && filePart.getSize() > 0;

        if (hasNewImage && hasExistingImage) {
            errors.put("postPicture", "Remove the current image before adding a new one.");
        } else if (hasNewImage) {
            long maxBytes = 2L * 1024 * 1024;
            if (filePart.getSize() > maxBytes) {
                errors.put("postPicture", "The post image cannot exceed 2MB.");
            } else {
                String fileName = filePart.getSubmittedFileName();
                if (fileName != null) {
                    String lower = fileName.toLowerCase();
                    if (!lower.endsWith(".jpg") && !lower.endsWith(".jpeg")
                            && !lower.endsWith(".png") && !lower.endsWith(".webp")
                            && !lower.endsWith(".gif")) {
                        errors.put("postPicture", "Image must be JPG, PNG, WEBP or GIF.");
                    }
                }
            }
        }

        if (!errors.isEmpty()) {
            return errors;
        }

        if (hasNewImage) {
            deleteExistingPostPictures(postId, post.getPostPicture());
            String saved = savePostPicture(filePart, postId);
            if (saved == null) {
                errors.put("postPicture", "Could not save the image.");
                return errors;
            }
            postRepository.setPostPicture(postId, saved);
        } else if (removeImage && hasExistingImage) {
            deleteExistingPostPictures(postId, post.getPostPicture());
            postRepository.clearPostPicture(postId);
        }

        postRepository.updatePostContent(postId, content.trim());
        return errors;
    }

    public Map<String, String> deletePost(int postId, User user) {
        Map<String, String> errors = new HashMap<>();

        Optional<Post> postOpt = postRepository.findById(postId);
        if (postOpt.isEmpty()) {
            errors.put("postId", "Post not found.");
            return errors;
        }

        Post post = postOpt.get();
        if (post.getResponseId() != null) {
            errors.put("postId", "This is not a top-level post.");
            return errors;
        }
        if (!post.getUserId().equals(user.getId())) {
            errors.put("postId", "You can only delete your own posts.");
            return errors;
        }

        if (!postRepository.deletePost(postId)) {
            errors.put("postId", "Could not delete this post.");
        }

        return errors;
    }

    private void deleteExistingPostPictures(int postId, String currentPicturePath) {
        try {
            String prefix = "post_" + postId;
            Path postsDir = Paths.get("EXTERNAL_RESOURCES/posts");
            if (Files.isDirectory(postsDir)) {
                try (DirectoryStream<Path> stream = Files.newDirectoryStream(postsDir)) {
                    for (Path file : stream) {
                        if (file.getFileName().toString().startsWith(prefix)) {
                            Files.deleteIfExists(file);
                        }
                    }
                }
            }
            if (currentPicturePath != null && currentPicturePath.startsWith("posts/")) {
                Files.deleteIfExists(Paths.get("EXTERNAL_RESOURCES", currentPicturePath));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String savePostPicture(Part filePart, int postId) {
        if (filePart == null || filePart.getSize() <= 0) {
            return null;
        }
        try {
            String fileName = filePart.getSubmittedFileName();
            String extension = fileName.substring(fileName.lastIndexOf('.'));
            String newFileName = "post_" + postId + "_" + System.currentTimeMillis() + extension;
            String resourcesDir = "EXTERNAL_RESOURCES/posts";
            Files.createDirectories(Paths.get(resourcesDir));
            try (InputStream input = filePart.getInputStream()) {
                Files.copy(input, Paths.get(resourcesDir, newFileName), StandardCopyOption.REPLACE_EXISTING);
            }
            return "posts/" + newFileName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Map<String, String> blockPost(int postId, User admin, String password, String reason, boolean banAuthor) {
        Map<String, String> errors = new HashMap<>();

        if (admin == null || !"admin".equals(admin.getRole())) {
            errors.put("postId", "Only administrators can block posts.");
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

        Optional<Post> postOpt = postRepository.findById(postId);
        if (postOpt.isEmpty()) {
            errors.put("postId", "Post not found.");
            return errors;
        }

        Post post = postOpt.get();

        if (!errors.isEmpty()) {
            return errors;
        }

        // Delete the post instead of marking it as blocked
        boolean deleted = postRepository.deletePost(postId);
        if (!deleted) {
            errors.put("postId", "Failed to delete post.");
            return errors;
        }

        // If banAuthor is checked, block the author
        if (banAuthor && !userRepository.isUserBlocked(admin.getId(), post.getUserId())) {
            userRepository.saveBlock(admin.getId(), post.getUserId(), reason.trim(), true);
        }

        return errors;
    }

}
