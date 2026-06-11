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
import java.util.Map;
import java.util.Optional;

@WebServlet("/AddComment")
public class AddComment extends HttpServlet {

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

        int postId = parsePostId(request, response);
        if (postId < 0) return;

        Optional<Post> post = postService.getPostById(postId);
        if (post.isEmpty()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        request.setAttribute("postId", postId);
        request.setAttribute("postAuthor", post.get().getUsername());
        request.getRequestDispatcher("AddCommentPanel.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            response.sendRedirect("Login");
            return;
        }

        User user = (User) session.getAttribute("user");

        int postId = parsePostId(request, response);
        if (postId < 0) return;

        String content = request.getParameter("content");

        Map<String, String> errors = postService.addComment(postId, user, content);

        if (!errors.isEmpty()) {
            Optional<Post> parentPost = postService.getPostById(postId);
            request.setAttribute("postId", postId);
            request.setAttribute("postAuthor", parentPost.map(Post::getUsername).orElse(""));
            request.setAttribute("errors", errors);
            request.setAttribute("content", content);
            request.getRequestDispatcher("AddCommentPanel.jsp").forward(request, response);
            return;
        }

        List<Post> comments = postService.getCommentsByPostId(postId);
        request.setAttribute("postId", postId);
        request.setAttribute("comments", comments);
        request.setAttribute("currentUserId", user.getId());
        request.getRequestDispatcher("CommentThread.jsp").forward(request, response);
    }

    private int parsePostId(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String param = request.getParameter("postId");
        if (param == null || param.isBlank()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return -1;
        }
        try {
            return Integer.parseInt(param);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return -1;
        }
    }
}
