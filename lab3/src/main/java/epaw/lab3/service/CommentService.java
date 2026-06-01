package epaw.lab3.service;

import epaw.lab3.model.Comment;
import epaw.lab3.repository.CommentRepository;

import java.util.List;

public class CommentService {

    private final CommentRepository commentRepository;
    private static CommentService instance;

    public CommentService() {
        this.commentRepository = new CommentRepository();
    }
}
