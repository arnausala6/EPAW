package epaw.lab3.controller;

import java.io.IOException;
import java.util.List;

import epaw.lab3.model.User;
import epaw.lab3.repository.GroupRepository;
import epaw.lab3.repository.UserRepository;
import epaw.lab3.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/AdminPanel")
public class AdminPanel extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private UserRepository userRepository;
    private GroupRepository groupRepository;
    private UserService userService;

    @Override
    public void init() {
        userRepository = UserRepository.getInstance();
        groupRepository = GroupRepository.getInstance();
        userService = UserService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("Login.jsp");
            return;
        }

        User currentUser = (User) session.getAttribute("user");
        if (!"admin".equalsIgnoreCase(currentUser.getRole())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Only admins can access this panel.");
            return;
        }

        int totalUsers = userRepository.countUsers();
        int blockedUsers = userRepository.countBlockedUsers(currentUser.getId());
        int activeGroups = groupRepository.countActiveGroups();
        List<User> users = userRepository.findAllUsersForAdmin(currentUser.getId());

        request.setAttribute("totalUsers", totalUsers);
        request.setAttribute("blockedUsers", blockedUsers);
        request.setAttribute("activeGroups", activeGroups);
        request.setAttribute("users", users);

        request.getRequestDispatcher("/AdminPanel.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        User currentUser = (User) session.getAttribute("user");
        if (!"admin".equalsIgnoreCase(currentUser.getRole())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        String action = request.getParameter("action");
        String userIdParam = request.getParameter("userId");

        if (userIdParam == null || userIdParam.isBlank()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing userId.");
            return;
        }

        int targetUserId = Integer.parseInt(userIdParam);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        if ("block".equals(action)) {
            userService.block(currentUser.getId(), targetUserId, true);
            response.getWriter().write("{\"ok\":true,\"action\":\"block\",\"userId\":" + targetUserId + "}");
            return;
        }

        if ("unblock".equals(action)) {
            userService.unblock(currentUser.getId(), targetUserId);
            response.getWriter().write("{\"ok\":true,\"action\":\"unblock\",\"userId\":" + targetUserId + "}");
            return;
        }

        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown action.");
    }
}
