package epaw.lab3.service;

import epaw.lab3.model.Post;
import epaw.lab3.repository.PostRepository;
import epaw.lab3.repository.UserRepository;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            errors.put("group_id", "Post must be part of a group");
        }
        if (post.getUserId() == null) { //MIRAR SEGURIDAD DE ESTO
            errors.put("user_id", "User cannot be null");
        }
        if (post.getContent() == null || post.getContent().trim().isEmpty()) {
            errors.put("content", "You cannot publish a post without content");
        }
        if(!userRepository.checkUserInGroup(post.getUserId(), post.getGroupId())){
            errors.put("group_id", "user is not part of the group");
        }

        postRepository.publishPost(post);
        return errors;
    }
}
