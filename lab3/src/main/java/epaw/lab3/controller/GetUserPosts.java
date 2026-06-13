package epaw.lab3.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import epaw.lab3.model.Post;
import epaw.lab3.model.User;
import epaw.lab3.repository.PostRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/GetUserPosts")
public class GetUserPosts extends HttpServlet {

    private PostRepository postRepository;

    @Override
    public void init() throws ServletException {
        postRepository = PostRepository.getInstance(); 
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String userIdParam = request.getParameter("userId");
        String pageParam = request.getParameter("page");

        if (userIdParam == null || pageParam == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        int userId = Integer.parseInt(userIdParam);
        int page = Integer.parseInt(pageParam);
        
        int limit = 10;
        int offset = page * limit;

        HttpSession session = request.getSession(false);
        User currentUser = session != null ? (User) session.getAttribute("user") : null;

        List<Post> posts;
        if (currentUser == null) {
            posts = postRepository.findPublicPostsByUserIdPaginated(userId, limit, offset);
        } else {
            posts = postRepository.findPostsByUserIdPaginated(userId, limit, offset);
        }

        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        if (posts.isEmpty()) {
            out.print("NO_MORE_POSTS");
            return;
        }

        for (Post post : posts) {
            out.println("<article class='card' style='margin-top: 0.8rem; text-align: left;'>");
            out.println("  <p class='post-body'>" + post.getContent() + "</p>");
            out.println("  <span class='post-meta'>" + post.getFormattedDate() + "</span>");
            out.println("</article>");
        }
    }
}