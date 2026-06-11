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

        List<User> users = userRepository.findRecommendedUsers(currentUser.getId(), searchTerm);

        request.setAttribute("query", searchTerm != null ? searchTerm.trim() : "");
        request.setAttribute("users", users);
        request.setAttribute("hasQuery", searchTerm != null && !searchTerm.trim().isEmpty());

        request.getRequestDispatcher("/Follow.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String userIdToFollow = request.getParameter("userId");
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("Login.jsp");
            return;
        }
        User currentUser = (User) session.getAttribute("user");
        userService.follow(currentUser.getId(), Integer.parseInt(userIdToFollow));
        if (userIdToFollow != null) {
            response.getWriter().write("Success");
            return;
        }
        doGet(request, response);
    }
}
