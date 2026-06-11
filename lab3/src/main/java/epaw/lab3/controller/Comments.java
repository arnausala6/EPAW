package epaw.lab3.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import epaw.lab3.model.Post;
import epaw.lab3.model.User;
import epaw.lab3.service.PostService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet("/Comments")
public class Comments extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private PostService postService;

    @Override
    public void init() throws ServletException {
        postService = PostService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            response.sendRedirect("Login");
            return;
        }

        String param = request.getParameter("postId");
        if (param == null || param.isBlank()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        int postId;
        try {
            postId = Integer.parseInt(param);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        Optional<Post> post = postService.getPostById(postId);
        if (post.isEmpty()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        User user = (User) session.getAttribute("user");
        List<Post> comments = postService.getCommentsByPostId(postId);
        request.setAttribute("postId", postId);
        request.setAttribute("comments", comments);
        request.setAttribute("currentUserId", user.getId());
        request.getRequestDispatcher("CommentThread.jsp").forward(request, response);
    }
}
