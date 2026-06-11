package epaw.lab3.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import epaw.lab3.model.Group;
import epaw.lab3.model.User;
import epaw.lab3.service.GroupService;
import epaw.lab3.service.PostService;
import epaw.lab3.service.UserService;
import epaw.lab3.util.BannedUserGuard;

import java.io.IOException;
import java.util.Map;

@WebServlet("/RequestJoinGroup")
public class RequestJoinGroup extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private GroupService groupService;
    private PostService postService;
    private UserService userService;

    @Override
    public void init() throws ServletException {
        groupService = GroupService.getInstance();
        postService = PostService.getInstance();
        userService = UserService.getInstance();
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
        if (BannedUserGuard.redirectIfBanned(user, userService, request, response)) {
            return;
        }

        int groupId;
        try {
            groupId = Integer.parseInt(request.getParameter("groupId"));
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        Map<String, String> errors = groupService.requestToJoinGroup(groupId, user);

        request.setAttribute("userGroups", groupService.getUserGroups(user.getId()));
        request.setAttribute("suggestedGroups", groupService.getSuggestedGroups(user.getId()));

        Group group = groupService.getGroupById(groupId);
        if (group != null) {
            boolean isGroupMember = groupService.isGroupMember(user, groupId);
            request.setAttribute("group", group);
            request.setAttribute("isGroupOwner", groupService.isGroupOwner(group, user));
            request.setAttribute("isGroupMember", isGroupMember);
            request.setAttribute("hasPendingJoinRequest", groupService.hasPendingJoinRequest(user.getId(), groupId));
            if (groupService.canViewGroupPosts(group, user)) {
                request.setAttribute("posts", postService.getPostsByGroupId(groupId, user.getId()));
            }
        }

        if (!errors.isEmpty()) {
            request.setAttribute("errors", errors);
        }

        request.getRequestDispatcher("Groups.jsp").forward(request, response);
    }
}
