package epaw.lab3.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import epaw.lab3.model.Post;
import epaw.lab3.model.User;
import epaw.lab3.service.PostService;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@MultipartConfig
@WebServlet("/EditPost")
public class EditPost extends HttpServlet {

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
        request.getRequestDispatcher("EditPostPanel.jsp").forward(request, response);
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
        String returnView = request.getParameter("returnView");
        boolean removeImage = "true".equals(request.getParameter("removeImage"));
        Part filePart = getOptionalPart(request, "postPicture");

        Map<String, String> errors = postService.editPost(postId, user, content, filePart, removeImage);

        Optional<Post> postOpt = postService.getPostById(postId);
        if (postOpt.isEmpty() || postOpt.get().getResponseId() != null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        if (!errors.isEmpty()) {
            request.setAttribute("post", postOpt.get());
            request.setAttribute("returnView", returnView);
            request.setAttribute("errors", errors);
            request.setAttribute("content", content);
            request.setAttribute("removeImage", removeImage);
            request.getRequestDispatcher("EditPostPanel.jsp").forward(request, response);
            return;
        }

        Post updated = postService.getPostById(postId).orElseThrow();
        postService.attachUserVotes(Collections.singletonList(updated), user.getId());
        request.setAttribute("post", updated);

        if ("timeline".equals(returnView)) {
            request.getRequestDispatcher("TimelinePostCard.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("GroupPostCard.jsp").forward(request, response);
        }
    }

    private Part getOptionalPart(HttpServletRequest request, String name) {
        try {
            Part part = request.getPart(name);
            if (part != null && part.getSize() > 0) {
                return part;
            }
        } catch (Exception ignored) {
            // field not present in this multipart request
        }
        return null;
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
