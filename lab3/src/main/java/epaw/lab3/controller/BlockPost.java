package epaw.lab3.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import epaw.lab3.model.Group;
import epaw.lab3.model.Post;
import epaw.lab3.model.User;
import epaw.lab3.service.GroupService;
import epaw.lab3.service.PostService;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@WebServlet("/BlockPost")
public class BlockPost extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private PostService postService;
    private GroupService groupService;

    @Override
    public void init() throws ServletException {
        postService = PostService.getInstance();
        groupService = GroupService.getInstance();
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
        if (!"admin".equals(user.getRole())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        int postId = parsePostId(request, response);
        if (postId < 0) {
            return;
        }

        Optional<Post> post = postService.getPostById(postId);
        if (post.isEmpty()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        request.setAttribute("post", post.get());
        request.getRequestDispatcher("BlockPostPanel.jsp").forward(request, response);
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
        if (!"admin".equals(user.getRole())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        int postId = parsePostId(request, response);
        if (postId < 0) {
            return;
        }

        String reason = request.getParameter("reason");
        String password = request.getParameter("password");
        boolean banAuthor = "true".equals(request.getParameter("banAuthor"));

        Optional<Post> post = postService.getPostById(postId);
        if (post.isEmpty()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        int groupId = post.get().getGroupId();

        Map<String, String> errors = postService.blockPost(postId, user, password, reason, banAuthor);

        if (!errors.isEmpty()) {
            request.setAttribute("post", post.orElse(null));
            request.setAttribute("errors", errors);
            request.setAttribute("reason", reason);
            request.setAttribute("banAuthor", banAuthor);
            request.getRequestDispatcher("BlockPostPanel.jsp").forward(request, response);
            return;
        }

        forwardGroupDetail(request, user, groupId);
        request.getRequestDispatcher("Groups.jsp").forward(request, response);
    }

    private void forwardGroupDetail(HttpServletRequest request, User user, int groupId) {
        Group group = groupService.getGroupById(groupId);
        if (group == null) {
            return;
        }
        boolean isGroupMember = groupService.isGroupMember(user, groupId);
        request.setAttribute("group", group);
        request.setAttribute("isGroupOwner", groupService.isGroupOwner(group, user));
        request.setAttribute("isGroupMember", isGroupMember);
        request.setAttribute("hasPendingJoinRequest", groupService.hasPendingJoinRequest(user.getId(), groupId));
        if (groupService.canViewGroupPosts(group, user)) {
            request.setAttribute("posts", postService.getPostsByGroupId(groupId, user.getId()));
        }
    }

    private int parsePostId(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String postIdParam = request.getParameter("id");
        if (postIdParam == null || postIdParam.isBlank()) {
            postIdParam = request.getParameter("postId");
        }
        if (postIdParam == null || postIdParam.isBlank()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return -1;
        }
        try {
            return Integer.parseInt(postIdParam);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return -1;
        }
    }
}
