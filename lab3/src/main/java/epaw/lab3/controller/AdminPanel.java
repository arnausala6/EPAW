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

        String mode = request.getParameter("mode");
        if (mode == null || mode.isBlank()) {
            mode = "users";
        }

        int totalUsers = userRepository.countUsers();
        int blockedUsers = userRepository.countBlockedUsers(currentUser.getId());
        int activeGroups = groupRepository.countActiveGroups();
        
        List<User> targetList;
        if ("admins".equalsIgnoreCase(mode)) {
            targetList = userRepository.findUsersByRoleForAdmin(currentUser.getId(), "admin");
        } else {
            targetList = userRepository.findUsersByRoleForAdmin(currentUser.getId(), "user");
        }

        request.setAttribute("totalUsers", totalUsers);
        request.setAttribute("blockedUsers", blockedUsers);
        request.setAttribute("activeGroups", activeGroups);
        request.setAttribute("users", targetList);
        request.setAttribute("mode", mode);
        request.setAttribute("currentUserId", currentUser.getId());

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
            String password = request.getParameter("password");
            String reason = request.getParameter("reason");
            if (password == null || password.isBlank()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing admin password.");
                return;
            }
            if (!userRepository.verifyPassword(currentUser.getId(), password)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Incorrect password.");
                return;
            }
            userService.block(currentUser.getId(), targetUserId, reason, true);
            response.getWriter().write("{\"ok\":true,\"action\":\"block\",\"userId\":" + targetUserId + "}");
            return;
        }

        if ("unblock".equals(action)) {
            userService.unblock(currentUser.getId(), targetUserId);
            response.getWriter().write("{\"ok\":true,\"action\":\"unblock\",\"userId\":" + targetUserId + "}");
            return;
        }

        if ("promote".equals(action)) {
            String password = request.getParameter("password");
            if (password == null || password.isBlank()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing admin password.");
                return;
            }

            if (!userRepository.verifyPassword(currentUser.getId(), password)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Incorrect password.");
                return;
            }

            userRepository.updateUserRole(targetUserId, "admin");
            userRepository.clearPersonalBlocks(targetUserId);
            response.getWriter().write("{\"ok\":true,\"action\":\"promote\",\"userId\":" + targetUserId + "}");
            return;
        }

        if ("demote".equals(action)) {
            userRepository.updateUserRole(targetUserId, "user");
            response.getWriter().write("{\"ok\":true,\"action\":\"demote\",\"userId\":" + targetUserId + "}");
            return;
        }

        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown action.");
    }
}
