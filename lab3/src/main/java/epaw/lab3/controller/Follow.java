package epaw.lab3.controller;

import java.io.IOException;
import java.util.List;

import epaw.lab3.model.User;
import epaw.lab3.repository.UserRepository;
import epaw.lab3.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/Follow")
public class Follow extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private UserRepository userRepository;
    private UserService userService;

    @Override
    public void init() throws ServletException {
        userRepository = UserRepository.getInstance();
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
        String searchTerm = request.getParameter("q");
        String mode = request.getParameter("mode");
        if (mode == null || (!"following".equals(mode) && !"discover".equals(mode))) {
            mode = "following";
        }

        List<User> users;
        if ("discover".equals(mode)) {
            users = userRepository.findRecommendedUsers(currentUser.getId(), searchTerm);
        } else {
            users = userRepository.findFollowingUsers(currentUser.getId(), searchTerm);
        }

        request.setAttribute("query", searchTerm != null ? searchTerm.trim() : "");
        request.setAttribute("users", users);
        request.setAttribute("hasQuery", searchTerm != null && !searchTerm.trim().isEmpty());
        request.setAttribute("followMode", mode);

        request.getRequestDispatcher("/Follow.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String userIdToFollow = request.getParameter("userId");
        String action = request.getParameter("action");
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("Login.jsp");
            return;
        }
        if (userIdToFollow == null || userIdToFollow.isBlank()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing userId parameter.");
            return;
        }

        User currentUser = (User) session.getAttribute("user");
        int followedId = Integer.parseInt(userIdToFollow);
        boolean shouldUnfollow = "unfollow".equalsIgnoreCase(action);

        if (shouldUnfollow) {
            userService.unfollow(currentUser.getId(), followedId);
        } else {
            userService.follow(currentUser.getId(), followedId);
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"following\":" + (!shouldUnfollow) + "}");
    }
}
