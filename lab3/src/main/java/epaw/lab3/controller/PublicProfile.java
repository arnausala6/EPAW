package epaw.lab3.controller;

import java.io.IOException;
import java.util.Optional;

import epaw.lab3.model.User;
import epaw.lab3.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/PublicProfile")
public class PublicProfile extends HttpServlet {

    private UserRepository userRepository;

    @Override
    public void init() throws ServletException {
        userRepository = UserRepository.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String userIdParam = request.getParameter("userId");
        if (userIdParam == null || userIdParam.isBlank()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing userId parameter.");
            return;
        }

        try {
            int userId = Integer.parseInt(userIdParam);
            Optional<User> userOpt = userRepository.findById(userId);

            if (userOpt.isEmpty()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found.");
                return;
            }

            User profileUser = userOpt.get();

            HttpSession session = request.getSession(false);
            boolean canManageRelationship = false;
            if (session != null && session.getAttribute("user") != null) {
                User currentUser = (User) session.getAttribute("user");
                
                boolean isFollowing = userRepository.isFollowing(currentUser.getId(), profileUser.getId());
                profileUser.setFollowing(isFollowing);
                canManageRelationship = true;
            }

            request.setAttribute("profileUser", profileUser);
            request.setAttribute("canManageRelationship", canManageRelationship);
            request.setAttribute("viewerRole", canManageRelationship ? "user" : "anonymous");
            request.getRequestDispatcher("/PublicProfile.jsp").forward(request, response);
        } catch (NumberFormatException ex) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid userId parameter.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
