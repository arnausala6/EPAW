package epaw.lab3.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

import epaw.lab3.model.Group;
import epaw.lab3.model.Post;
import epaw.lab3.model.User;
import epaw.lab3.service.GroupService;
import epaw.lab3.service.PostService;
import epaw.lab3.service.UserService;
import epaw.lab3.util.BannedUserGuard;
import java.util.List;
import java.util.Map;

@MultipartConfig
@WebServlet("/NewPost")
public class NewPost extends HttpServlet {

    private PostService postService;
    private UserService userService;
    private GroupService groupService;

    @Override
    public void init() throws ServletException {
        postService = PostService.getInstance();
        userService = UserService.getInstance();
        groupService = GroupService.getInstance();
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
        applyGroupContext(request, user, request.getParameter("groupId"));

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
            String returnGroupIdParam = request.getParameter("returnGroupId");
            if (returnGroupIdParam != null && !returnGroupIdParam.isBlank()) {
                try {
                    int returnGroupId = Integer.parseInt(returnGroupIdParam);
                    if (groupService.isGroupMember(user, returnGroupId)) {
                        forwardGroupDetail(request, user, returnGroupId);
                        request.getRequestDispatcher("Groups.jsp").forward(request, response);
                        return;
                    }
                } catch (NumberFormatException ignored) {
                    // fall through to timeline
                }
            }
            List<Post> posts = postService.getTimelineByUserId(user.getId());
            request.setAttribute("posts", posts);
            request.getRequestDispatcher("Timeline.jsp").forward(request, response);
        } else {
            request.setAttribute("errors", errors);
            request.setAttribute("groups", userService.getGroupsByUserId(user.getId()));
            request.setAttribute("content", content);
            request.setAttribute("selectedGroupId", groupId);
            applyGroupContext(request, user, request.getParameter("returnGroupId"));
            request.getRequestDispatcher("NewPost.jsp").forward(request, response);
        }
    }

    private void applyGroupContext(HttpServletRequest request, User user, String groupIdParam) {
        if (groupIdParam == null || groupIdParam.isBlank()) {
            return;
        }
        try {
            int groupId = Integer.parseInt(groupIdParam);
            Group group = groupService.getGroupById(groupId);
            if (groupService.isGroupMember(user, groupId) && group != null && !group.isBlocked()) {
                request.setAttribute("selectedGroupId", groupId);
                request.setAttribute("returnGroupId", groupId);
            }
        } catch (NumberFormatException ignored) {
            // ignore invalid group id
        }
    }

    private void forwardGroupDetail(HttpServletRequest request, User user, int groupId) {
        Group group = groupService.getGroupById(groupId);
        if (group == null) {
            return;
        }
        request.setAttribute("group", group);
        request.setAttribute("isGroupOwner", groupService.isGroupOwner(group, user));
        request.setAttribute("isGroupMember", groupService.isGroupMember(user, groupId));
        request.setAttribute("hasPendingJoinRequest",
                groupService.hasPendingJoinRequest(user.getId(), groupId));
        if (groupService.canViewGroupPosts(group, user)) {
            request.setAttribute("posts", postService.getPostsByGroupId(groupId, user.getId()));
        }
    }
}

