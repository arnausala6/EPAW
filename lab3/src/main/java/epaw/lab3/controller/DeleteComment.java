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

@WebServlet("/DeleteComment")
public class DeleteComment extends HttpServlet {

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

        User user = (User) session.getAttribute("user");
        int commentId = parseCommentId(request, response);
        if (commentId < 0) return;

        Optional<Post> commentOpt = postService.getPostById(commentId);
        if (commentOpt.isEmpty() || commentOpt.get().getResponseId() == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Post comment = commentOpt.get();
        if (!comment.getUserId().equals(user.getId())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        request.setAttribute("comment", comment);
        request.setAttribute("postId", comment.getResponseId());
        request.getRequestDispatcher("DeleteCommentPanel.jsp").forward(request, response);
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
        int commentId = parseCommentId(request, response);
        if (commentId < 0) return;

        Optional<Post> commentOpt = postService.getPostById(commentId);
        if (commentOpt.isEmpty() || commentOpt.get().getResponseId() == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        int parentPostId = commentOpt.get().getResponseId();
        Map<String, String> errors = postService.deleteComment(commentId, user);

        if (!errors.isEmpty()) {
            request.setAttribute("comment", commentOpt.get());
            request.setAttribute("postId", parentPostId);
            request.setAttribute("errors", errors);
            request.getRequestDispatcher("DeleteCommentPanel.jsp").forward(request, response);
            return;
        }

        List<Post> comments = postService.getCommentsByPostId(parentPostId);
        request.setAttribute("postId", parentPostId);
        request.setAttribute("comments", comments);
        request.setAttribute("currentUserId", user.getId());
        request.getRequestDispatcher("CommentThread.jsp").forward(request, response);
    }

    private int parseCommentId(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String param = request.getParameter("commentId");
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
