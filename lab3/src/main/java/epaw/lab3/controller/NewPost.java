package epaw.lab3.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

import epaw.lab3.service.PostService;
import epaw.lab3.service.UserService;
import epaw.lab3.model.Post;
import epaw.lab3.model.User;
import epaw.lab3.util.BannedUserGuard;
import java.util.List;
import java.util.Map;

@MultipartConfig
@WebServlet("/NewPost")
public class NewPost extends HttpServlet {

    private PostService postService;
    private UserService userService;

    @Override
    public void init() throws ServletException {
        postService = PostService.getInstance();
        userService = UserService.getInstance();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Cogemos el usuario de la sesión
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            response.sendRedirect("Login");
            return;
        }

        User user = (User) session.getAttribute("user");
        if (BannedUserGuard.redirectIfBanned(user, userService, request, response)) {
            return;
        }
        request.setAttribute("groups", userService.getGroupsByUserId(user.getId()));

        // Forward a NewPost.jsp
        request.getRequestDispatcher("NewPost.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            response.sendRedirect("Login");
            return;
        }
        
        User user = (User) session.getAttribute("user");
        if (BannedUserGuard.redirectIfBanned(user, userService, request, response)) {
            return;
        }

        String content = request.getParameter("content");
        int groupId = Integer.parseInt(request.getParameter("groupId"));

        Post post = new Post();
        post.setUserId(user.getId());
        post.setContent(content);
        post.setGroupId(groupId);

        Map<String, String> errors = postService.createPost(post);

        if (errors.isEmpty()) {
            List<Post> posts = postService.getTimelineByUserId(user.getId());
            request.setAttribute("posts", posts);
            request.getRequestDispatcher("Timeline.jsp").forward(request, response);
        } else {
            request.setAttribute("errors", errors);
            request.setAttribute("groups", userService.getGroupsByUserId(user.getId()));
            request.setAttribute("content", content);
            request.setAttribute("selectedGroupId", groupId);
            request.getRequestDispatcher("NewPost.jsp").forward(request, response);
        }
    }
}

