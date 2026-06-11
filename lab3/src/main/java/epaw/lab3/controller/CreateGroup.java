package epaw.lab3.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import epaw.lab3.model.Group;
import epaw.lab3.model.User;
import epaw.lab3.service.GroupService;

import java.io.IOException;
import java.util.Map;

@MultipartConfig
@WebServlet("/CreateGroup")
public class CreateGroup extends HttpServlet {

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

        request.getRequestDispatcher("CreateGroupPanel.jsp").forward(request, response);
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

        Group group = new Group();
        group.setGroupName(request.getParameter("groupName"));
        group.setDescription(request.getParameter("groupDescription"));
        group.setPrivacy(request.getParameter("groupVisibility"));

        Part filePart = request.getPart("groupImage");
        Map<String, String> errors = groupService.createGroup(group, user, filePart);

        if (errors.isEmpty()) {
            request.setAttribute("userGroups", groupService.getUserGroups(user.getId()));
            request.setAttribute("suggestedGroups", groupService.getSuggestedGroups(user.getId()));
            request.getRequestDispatcher("Groups.jsp").forward(request, response);
        } else {
            request.setAttribute("errors", errors);
            request.setAttribute("group", group);
            request.getRequestDispatcher("CreateGroupPanel.jsp").forward(request, response);
        }
    }
}
