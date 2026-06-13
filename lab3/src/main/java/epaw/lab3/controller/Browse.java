package epaw.lab3.controller;

import java.io.IOException;
import java.util.List;

import epaw.lab3.model.Post;
import epaw.lab3.model.User;
import epaw.lab3.repository.UserRepository;
import epaw.lab3.service.PostService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/Browse")
public class Browse extends HttpServlet {

    private PostService postService;
    private UserRepository userRepository;

    @Override
    public void init() throws ServletException {
        postService = PostService.getInstance();
        userRepository = UserRepository.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String mode = request.getParameter("mode");
        if (mode == null || (!"timeline".equals(mode) && !"search".equals(mode))) {
            mode = "timeline";
        }

        String query = request.getParameter("q");
        request.setAttribute("browseMode", mode);
        request.setAttribute("query", query != null ? query.trim() : "");
        request.setAttribute("viewerRole", "anonymous");

        if ("search".equals(mode)) {
            List<User> users = userRepository.findPublicUsers(query);
            request.setAttribute("users", users);
            request.setAttribute("hasQuery", query != null && !query.trim().isEmpty());
        } else {
            List<Post> posts = postService.getPublicTrendingPosts();
            request.setAttribute("posts", posts);
        }

        request.getRequestDispatcher("/Browse.jsp").forward(request, response);
    }
}