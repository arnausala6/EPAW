package epaw.lab3.controller;

import java.io.IOException;
import java.util.List;

import epaw.lab3.model.User;
import epaw.lab3.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/BlockedUsers")
public class BlockedUsers extends HttpServlet {

    private UserService userService;

    @Override
    public void init() throws ServletException {
        userService = UserService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            response.sendRedirect("index.html");
            return;
        }

        if ("admin".equalsIgnoreCase(user.getRole())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Admins do not have personal blocked users.");
            return;
        }

        List<User> blockedUsers = userService.getBlockedUsers(user.getId());
        request.setAttribute("blockedUsers", blockedUsers);
        request.getRequestDispatcher("BlockedUsers.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        if ("admin".equalsIgnoreCase(user.getRole())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Admins do not have personal blocked users.");
            return;
        }

        String blockedIdParam = request.getParameter("blockedId");
        if (blockedIdParam == null || blockedIdParam.isBlank()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing blockedId parameter.");
            return;
        }

        try {
            int blockedId = Integer.parseInt(blockedIdParam);
            userService.unblock(user.getId(), blockedId);
        } catch (NumberFormatException ex) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid blockedId parameter.");
            return;
        }

        List<User> blockedUsers = userService.getBlockedUsers(user.getId());
        request.setAttribute("blockedUsers", blockedUsers);
        request.getRequestDispatcher("BlockedUsers.jsp").forward(request, response);
    }
}
