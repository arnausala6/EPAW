package epaw.lab3.service;

import epaw.lab3.model.Post;
import epaw.lab3.model.User;
import epaw.lab3.repository.PostRepository;
import epaw.lab3.repository.UserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private static PostService instance;

    public PostService() {
        this.postRepository = new PostRepository();
        this.userRepository = new UserRepository();
    }

    public Map<String, String> createPost(Post post){
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

        if (errors.isEmpty()) {
            postRepository.publishPost(post);
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
        return postRepository.getTimelineByUserId(userId);
    }

    public List<Post> getPostsByGroupId(int groupId) {
        return postRepository.findByGroupId(groupId);
    }

    public Optional<Post> getPostById(int postId) {
        return postRepository.findById(postId);
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
        if (post.isBlocked()) {
            errors.put("postId", "This post is already blocked.");
        }

        if (!errors.isEmpty()) {
            return errors;
        }

        postRepository.blockPost(postId, reason.trim(), banAuthor);

        if (banAuthor && !userRepository.isUserBlocked(admin.getId(), post.getUserId())) {
            userRepository.saveBlock(admin.getId(), post.getUserId(), reason.trim());
        }

        return errors;
    }

}
