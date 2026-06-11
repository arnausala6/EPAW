package epaw.lab3.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import epaw.lab3.model.Group;
import epaw.lab3.model.User;
import epaw.lab3.repository.UserRepository;
import epaw.lab3.service.GroupService;
import epaw.lab3.service.UserService;
import epaw.lab3.util.BannedUserGuard;

import java.io.IOException;
import java.util.Map;

@WebServlet("/LeaveGroup")
public class LeaveGroup extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private GroupService groupService;
    private UserRepository userRepository;
    private UserService userService;

    @Override
    public void init() throws ServletException {
        groupService = GroupService.getInstance();
        userRepository = UserRepository.getInstance();
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
            if (groupService.isGroupOwner(group, user)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
            if (!userRepository.checkUserInGroup(user.getId(), groupId)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
            request.setAttribute("group", group);
            request.getRequestDispatcher("LeaveGroupPanel.jsp").forward(request, response);
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

        Map<String, String> errors = groupService.leaveOrDeleteGroup(groupId, user);

        if (errors.isEmpty()) {
            request.setAttribute("userGroups", groupService.getUserGroups(user.getId()));
            request.setAttribute("suggestedGroups", groupService.getSuggestedGroups(user.getId()));
            request.getRequestDispatcher("Groups.jsp").forward(request, response);
        } else {
            Group group = groupService.getGroupById(groupId);
            request.setAttribute("group", group);
            request.setAttribute("errors", errors);
            if (group != null && groupService.isGroupOwner(group, user)) {
                request.getRequestDispatcher("EditGroupPanel.jsp").forward(request, response);
            } else {
                request.getRequestDispatcher("LeaveGroupPanel.jsp").forward(request, response);
            }
        }
    }
}
