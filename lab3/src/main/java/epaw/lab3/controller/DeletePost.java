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
import java.util.Map;
import java.util.Optional;

@WebServlet("/DeletePost")
public class DeletePost extends HttpServlet {

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
        int postId = parsePostId(request, response);
        if (postId < 0) return;

        Optional<Post> postOpt = postService.getPostById(postId);
        if (postOpt.isEmpty() || postOpt.get().getResponseId() != null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Post post = postOpt.get();
        if (!post.getUserId().equals(user.getId())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        request.setAttribute("post", post);
        request.setAttribute("returnView", request.getParameter("view"));
        request.getRequestDispatcher("DeletePostPanel.jsp").forward(request, response);
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

        Map<String, String> errors = postService.deletePost(postId, user);

        if (!errors.isEmpty()) {
            Optional<Post> postOpt = postService.getPostById(postId);
            request.setAttribute("post", postOpt.orElse(null));
            request.setAttribute("returnView", request.getParameter("returnView"));
            request.setAttribute("errors", errors);
            request.getRequestDispatcher("DeletePostPanel.jsp").forward(request, response);
            return;
        }

        request.setAttribute("postId", postId);
        request.getRequestDispatcher("PostDeleteResult.jsp").forward(request, response);
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
