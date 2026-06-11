package epaw.lab3.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import epaw.lab3.model.User;
import epaw.lab3.service.GroupService;

import java.io.IOException;

@WebServlet("/Groups")
public class Groups extends HttpServlet {

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
        request.setAttribute("userGroups", groupService.getUserGroups(user.getId()));
        request.setAttribute("suggestedGroups", groupService.getSuggestedGroups(user.getId()));

        request.getRequestDispatcher("Groups.jsp").forward(request, response);
    }
}
