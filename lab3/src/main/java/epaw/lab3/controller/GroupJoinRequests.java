package epaw.lab3.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import epaw.lab3.model.Group;
import epaw.lab3.model.GroupJoinRequest;
import epaw.lab3.model.User;
import epaw.lab3.service.GroupService;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/GroupJoinRequests")
public class GroupJoinRequests extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private GroupService groupService;

    @Override
    public void init() throws ServletException {
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
        int groupId = parseGroupId(request, response);
        if (groupId < 0) {
            return;
        }

        Group group = groupService.getGroupById(groupId);
        if (group == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        if (!groupService.isGroupOwner(group, user)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        if (group.isBlocked()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        forwardPanel(request, response, group, groupService.getPendingJoinRequests(groupId, user));
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
        int groupId = parseGroupId(request, response);
        if (groupId < 0) {
            return;
        }

        Group group = groupService.getGroupById(groupId);
        if (group == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        if (!groupService.isGroupOwner(group, user)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        if (group.isBlocked()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        String action = request.getParameter("action");
        int requestUserId;
        try {
            requestUserId = Integer.parseInt(request.getParameter("userId"));
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        Map<String, String> errors;
        if ("accept".equals(action)) {
            errors = groupService.acceptJoinRequest(groupId, requestUserId, user);
        } else if ("reject".equals(action)) {
            errors = groupService.rejectJoinRequest(groupId, requestUserId, user);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        List<GroupJoinRequest> joinRequests = groupService.getPendingJoinRequests(groupId, user);
        request.setAttribute("group", group);
        request.setAttribute("joinRequests", joinRequests);
        if (!errors.isEmpty()) {
            request.setAttribute("errors", errors);
        }

        request.getRequestDispatcher("GroupJoinRequestsPanel.jsp").forward(request, response);
    }

    private void forwardPanel(HttpServletRequest request, HttpServletResponse response, Group group,
            List<GroupJoinRequest> joinRequests) throws ServletException, IOException {
        request.setAttribute("group", group);
        request.setAttribute("joinRequests", joinRequests);
        request.getRequestDispatcher("GroupJoinRequestsPanel.jsp").forward(request, response);
    }

    private int parseGroupId(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String groupIdParam = request.getParameter("id");
        if (groupIdParam == null || groupIdParam.isBlank()) {
            groupIdParam = request.getParameter("groupId");
        }
        if (groupIdParam == null || groupIdParam.isBlank()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return -1;
        }
        try {
            return Integer.parseInt(groupIdParam);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return -1;
        }
    }
}
