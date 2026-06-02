package epaw.lab3.service;

import epaw.lab3.model.Comment;
import epaw.lab3.model.Post;
import epaw.lab3.repository.CommentRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommentService {

    private final CommentRepository commentRepository;
    private static CommentService instance;

    public CommentService() {
        this.commentRepository = new CommentRepository();
    }

    public Map<String, String> createComment(Comment comment){
        Map<String, String> errors = new HashMap<>();
        if (comment.getPostId() == null) {
            errors.put("post_id", "Comment must be in a post");
        }
        if (comment.getContent() == null || comment.getContent().trim().isEmpty()) {
            errors.put("content", "You cannot publish a comment without content");
        }
        if(comment.getUserId() == null){ //MIRAR SEGURIDAD DE ESTO
            errors.put("user_id", "Comment must come from a user");
        }

        commentRepository.publishComment(comment);
        return errors;
    }
}
