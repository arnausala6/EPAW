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

@WebServlet("/NewPost")
public class NewPost extends HttpServlet {

    private PostService postService;
    private UserService userService;

    @Override
    public void init() throws ServletException {
        postService = PostService.getInstance();
        userService = UserService.getInstance();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Cogemos el usuario de la sesión
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            response.sendRedirect("Login");
            return;
        }

        User user = (User) session.getAttribute("user");
        request.setAttribute("groups", userService.getGroupsByUserId(user.getId()));

        // Forward a NewPost.jsp
        request.getRequestDispatcher("NewPost.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            response.sendRedirect("Login");
            return;
        }
        
        User user = (User) session.getAttribute("user");

        String content = request.getParameter("content");
        int groupId = Integer.parseInt(request.getParameter("groupId"));

        Post post = new Post();
        post.setUserId(user.getId());
        post.setContent(content);
        post.setGroupId(groupId);

        // Llamamos a postService.createPost(post)
        var errors = postService.createPost(post);

        // Si no hay errores, redirigimos a Timeline. Si hay errores, los mostramos en NewPost.jsp
        if (errors.isEmpty()) {
            response.sendRedirect("Timeline");
        } else {
            request.setAttribute("errors", errors); 
            request.getRequestDispatcher("NewPost.jsp").forward(request, response);
        }
    }
}

