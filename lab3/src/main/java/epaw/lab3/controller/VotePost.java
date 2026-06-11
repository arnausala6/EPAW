package epaw.lab3.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import epaw.lab3.model.User;
import epaw.lab3.service.PostService;
import epaw.lab3.service.UserService;
import epaw.lab3.service.VoteResult;
import epaw.lab3.util.BannedUserGuard;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@WebServlet("/VotePost")
public class VotePost extends HttpServlet {

    private PostService postService;
    private UserService userService;

    @Override
    public void init() throws ServletException {
        postService = PostService.getInstance();
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

        User user = (User) session.getAttribute("user");
        if (BannedUserGuard.redirectIfBanned(user, userService, request, response)) {
            return;
        }

        int postId;
        int voteType;
        try {
            postId = Integer.parseInt(request.getParameter("postId"));
            voteType = Integer.parseInt(request.getParameter("voteType"));
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        VoteResult result = postService.votePost(user.getId(), postId, voteType);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType("application/json;charset=UTF-8");

        if (!result.isSuccess()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"" + escapeJson(result.getError()) + "\"}");
            return;
        }

        String userVoteJson = result.getUserVote() == null ? "null" : result.getUserVote().toString();
        response.getWriter().write(String.format(
                "{\"upvotes\":%d,\"downvotes\":%d,\"votes\":%d,\"userVote\":%s}",
                result.getUpvotes(),
                result.getDownvotes(),
                result.getVotes(),
                userVoteJson));
    }

    private String escapeJson(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
