package epaw.lab3.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

import epaw.lab3.service.PostService;
import epaw.lab3.service.UserService;
import epaw.lab3.model.Post;
import epaw.lab3.model.User;
import java.util.List;

@WebServlet("/Timeline")
public class Timeline extends HttpServlet {

    private PostService postService;

    @Override
    public void init() throws ServletException {
        postService = PostService.getInstance();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            response.sendRedirect("Login");
            return;
        }

        User user = (User) session.getAttribute("user");

        // Llamamos a postService.getTimelineByUserId con su id
        List<Post> posts = postService.getTimelineByUserId(user.getId());
        request.setAttribute("posts", posts);
        request.getRequestDispatcher("Timeline.jsp").forward(request, response);
    }
}
