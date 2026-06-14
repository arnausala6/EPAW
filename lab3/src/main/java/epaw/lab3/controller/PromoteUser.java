package epaw.lab3.controller;

import java.io.IOException;

import epaw.lab3.model.User;
import epaw.lab3.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/PromoteUser")
public class PromoteUser extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private UserRepository userRepository;

    @Override
    public void init() throws ServletException {
        userRepository = UserRepository.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
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

        String userIdParam = request.getParameter("userId");
        if (userIdParam == null || userIdParam.isBlank()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing userId parameter.");
            return;
        }

        int targetUserId = Integer.parseInt(userIdParam);
        var targetUserOpt = userRepository.findById(targetUserId);
        if (targetUserOpt.isEmpty()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found.");
            return;
        }

        request.setAttribute("targetUser", targetUserOpt.get());
        request.getRequestDispatcher("/PromoteUserPanel.jsp").forward(request, response);
    }
}
