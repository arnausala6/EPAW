package epaw.lab3.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import epaw.lab3.model.Group;
import epaw.lab3.model.User;
import epaw.lab3.service.GroupService;
import epaw.lab3.service.PostService;
import epaw.lab3.service.UserService;
import epaw.lab3.util.BannedUserGuard;

import java.io.IOException;
import java.util.Map;

@MultipartConfig
@WebServlet("/EditGroup")
public class EditGroup extends HttpServlet {

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
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
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

        String groupIdParam = request.getParameter("id");
        if (groupIdParam == null || groupIdParam.isBlank()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            int groupId = Integer.parseInt(groupIdParam);
            Group group = groupService.getGroupById(groupId);
            if (group == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            if (!groupService.isGroupOwner(group, user)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
            request.setAttribute("group", group);
            request.getRequestDispatcher("EditGroupPanel.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
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

        Group updates = new Group();
        updates.setGroupName(request.getParameter("groupName"));
        updates.setDescription(request.getParameter("groupDescription"));
        updates.setPrivacy(request.getParameter("groupVisibility"));

        Part filePart = request.getPart("groupImage");
        Map<String, String> errors = groupService.updateGroup(groupId, updates, user, filePart);

        if (errors.isEmpty()) {
            Group group = groupService.getGroupById(groupId);
            boolean isGroupMember = groupService.isGroupMember(user, groupId);
            request.setAttribute("group", group);
            request.setAttribute("isGroupOwner", groupService.isGroupOwner(group, user));
            request.setAttribute("isGroupMember", isGroupMember);
            request.setAttribute("hasPendingJoinRequest",
                    groupService.hasPendingJoinRequest(user.getId(), groupId));
            if (group != null && groupService.canViewGroupPosts(group, user)) {
                request.setAttribute("posts", postService.getPostsByGroupId(groupId));
            }
            request.setAttribute("userGroups", groupService.getUserGroups(user.getId()));
            request.setAttribute("suggestedGroups", groupService.getSuggestedGroups(user.getId()));
            request.getRequestDispatcher("Groups.jsp").forward(request, response);
        } else {
            Group group = groupService.getGroupById(groupId);
            if (group != null) {
                group.setGroupName(updates.getGroupName());
                group.setDescription(updates.getDescription());
                group.setPrivacy(updates.getPrivacy());
            }
            request.setAttribute("group", group);
            request.setAttribute("errors", errors);
            request.getRequestDispatcher("EditGroupPanel.jsp").forward(request, response);
        }
    }
}
