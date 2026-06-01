package epaw.lab3.service;

import epaw.lab3.model.Post;
import epaw.lab3.repository.PostRepository;

import java.util.List;

public class PostService {

    private final PostRepository postRepository;
    private static PostService instance;

    public PostService() {
        this.postRepository = new PostRepository();
    }
}
