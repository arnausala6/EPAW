package epaw.lab3.controller;

import java.io.IOException;
import epaw.lab3.model.User;
import epaw.lab3.repository.UserRepository;
import epaw.lab3.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/Block")
public class Block extends HttpServlet {
    private UserService userService;

    @Override
    public void init() throws ServletException {
        userService = UserService.getInstance();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String userIdToBlock = request.getParameter("userId");
        if (userIdToBlock == null || userIdToBlock.isBlank()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing userId parameter.");
            return;
        }

        User currentUser = (User) session.getAttribute("user");
        int blockedId = Integer.parseInt(userIdToBlock);

        boolean is_admin = currentUser.getRole().equals("admin");

        userService.block(currentUser.getId(), blockedId, is_admin);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write("{\"ok\":true}");
    }
}