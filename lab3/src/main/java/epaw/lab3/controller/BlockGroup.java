package epaw.lab3.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import epaw.lab3.model.Group;
import epaw.lab3.model.User;
import epaw.lab3.repository.UserRepository;
import epaw.lab3.service.GroupService;
import epaw.lab3.service.PostService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebServlet("/BlockGroup")
public class BlockGroup extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private GroupService groupService;
    private UserRepository userRepository;
    private PostService postService;

    @Override
    public void init() throws ServletException {
        groupService = GroupService.getInstance();
        userRepository = UserRepository.getInstance();
        postService = PostService.getInstance();
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
        if (!"admin".equals(user.getRole())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        int groupId = parseGroupId(request, response);
        if (groupId < 0) {
            return;
        }

        Group group = groupService.getGroupById(groupId);
        if (group == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        if (group.isBlocked()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        request.setAttribute("group", group);
        request.setAttribute("members", membersExceptSelf(groupId, user.getId()));
        request.getRequestDispatcher("BlockGroupPanel.jsp").forward(request, response);
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
        if (!"admin".equals(user.getRole())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        int groupId = parseGroupId(request, response);
        if (groupId < 0) {
            return;
        }

        String reason = request.getParameter("reason");
        String password = request.getParameter("password");
        int[] memberIds = parseMemberIds(request);

        Map<String, String> errors = groupService.blockGroup(groupId, user, password, reason, memberIds);

        if (!errors.isEmpty()) {
            Group group = groupService.getGroupById(groupId);
            request.setAttribute("group", group);
            request.setAttribute("members", membersExceptSelf(groupId, user.getId()));
            request.setAttribute("errors", errors);
            request.setAttribute("reason", reason);
            request.setAttribute("selectedMemberIds", toIntegerList(memberIds));
            request.getRequestDispatcher("BlockGroupPanel.jsp").forward(request, response);
            return;
        }

        forwardGroupDetail(request, user, groupId);
        request.getRequestDispatcher("Groups.jsp").forward(request, response);
    }

    private void forwardGroupDetail(HttpServletRequest request, User user, int groupId) {
        Group group = groupService.getGroupById(groupId);
        if (group == null) {
            return;
        }
        boolean isGroupMember = groupService.isGroupMember(user, groupId);
        request.setAttribute("group", group);
        request.setAttribute("isGroupOwner", groupService.isGroupOwner(group, user));
        request.setAttribute("isGroupMember", isGroupMember);
        request.setAttribute("hasPendingJoinRequest", groupService.hasPendingJoinRequest(user.getId(), groupId));
        if (groupService.canViewGroupPosts(group, user)) {
            request.setAttribute("posts", postService.getPostsByGroupId(groupId));
        }
    }

    private int parseGroupId(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String groupIdParam = request.getParameter("id");
        if (groupIdParam == null || groupIdParam.isBlank()) {
            groupIdParam = request.getParameter("groupId");
        }
        if (groupIdParam == null || groupIdParam.isBlank()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return -1;
        }
        try {
            return Integer.parseInt(groupIdParam);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return -1;
        }
    }

    private int[] parseMemberIds(HttpServletRequest request) {
        String[] memberIdParams = request.getParameterValues("memberIds");
        if (memberIdParams == null) {
            return new int[0];
        }
        List<Integer> ids = new ArrayList<>();
        for (String param : memberIdParams) {
            try {
                ids.add(Integer.parseInt(param));
            } catch (NumberFormatException ignored) {
                // skip invalid ids
            }
        }
        return ids.stream().mapToInt(Integer::intValue).toArray();
    }

    private List<User> membersExceptSelf(int groupId, int userId) {
        List<User> members = userRepository.findUsersByGroupId(groupId);
        members.removeIf(member -> member.getId() == userId);
        return members;
    }

    private List<Integer> toIntegerList(int[] memberIds) {
        List<Integer> ids = new ArrayList<>();
        if (memberIds != null) {
            for (int memberId : memberIds) {
                ids.add(memberId);
            }
        }
        return ids;
    }
}
