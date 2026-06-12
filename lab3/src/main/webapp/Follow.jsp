<%@ page import="epaw.lab3.model.User" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    String query = (String) request.getAttribute("query");
    List<User> users = (List<User>) request.getAttribute("users");
    boolean hasQuery = Boolean.TRUE.equals(request.getAttribute("hasQuery"));
    if (query == null) {
        query = "";
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Follow users</title>
</head>
<body>
<div class="card card-flush page-head">
    <div class="card-head">
        <h3><img src="assets/icons/seguir-blanco.png" alt="" class="ico"> Follow users</h3>
    </div>
    <div class="card-body">
        <div class="search-row">
            <input id="followSearch" class="input" type="text" value="<%= query %>" placeholder="Search by username, email or description"
                   onkeydown="if (event.key === 'Enter') { event.preventDefault(); searchFollow(); }">
            <button type="button" class="btn btn-primary" onclick="return searchFollow();">
                <img src="assets/icons/buscar-blanco.png" alt="" class="ico"> Search
            </button>
        </div>

        <p class="post-meta">
            <% if (hasQuery) { %>
                Showing results for “<%= query %>”.
            <% } else { %>
                Recommended users for you.
            <% } %>
        </p>

        <% if (users == null || users.isEmpty()) { %>
            <article class="card">
                <p class="post-body">No users to recommend</p>
            </article>
        <% } else { %>
            <% for (User user : users) { %>
                <article class="card">
                    <h4><%= user.getUsername() %></h4>
                    <p class="post-body">
                        <%= user.getDescription() != null && !user.getDescription().isBlank()
                                ? user.getDescription()
                                : "No description yet." %>
                    </p>
                    <div class="btn-row">
                        <button type="button" id="followBtn<%= user.getId() %>" 
                                class="btn <%= user.isFollowing() ? "btn-muted" : "btn-primary" %> btn-sm" 
                                data-following="<%= user.isFollowing() %>" 
                                onclick="toggleFollow('<%= user.getId() %>', this.getAttribute('data-following') === 'true');">
                            <img src="<%= user.isFollowing() ? "assets/icons/unfollow-error.png" : "assets/icons/seguir-blanco.png" %>" alt="" class="ico"> 
                            <%= user.isFollowing() ? "Following" : "Follow" %>
                        </button>
                        <button type="button" class="btn btn-muted btn-sm" onclick="window.loadContent('PublicProfile?userId=<%= user.getId() %>');">
                            <i class="fa fa-id-card-o ico-missing"></i> View public profile
                        </button>
                    </div>
                </article>
            <% } %>
        <% } %>
    </div>
</div>
<script>
    function searchFollow() {
        var q = document.getElementById('followSearch').value;
        window.loadContent('Follow?q=' + encodeURIComponent(q));
        return false;
    }
    function toggleFollow(userId, currentlyFollowing) {
        var btn = document.getElementById('followBtn' + userId);
        var action = currentlyFollowing ? 'unfollow' : 'follow';

        $.post('Follow', { userId: userId, action: action }, function(response) {
            var following = response && response.following === true;
            btn.setAttribute('data-following', following ? 'true' : 'false');
            if (following) {
                btn.className = "btn btn-muted btn-sm";
                btn.innerHTML = '<img src="assets/icons/unfollow-error.png" alt="" class="ico"> Following';
            } else {
                btn.className = "btn btn-primary btn-sm";
                btn.innerHTML = '<img src="assets/icons/seguir-blanco.png" alt="" class="ico"> Follow';
            }
            btn.onclick = function () {
                toggleFollow(userId, following);
            };
        });
    }
    $(document).ready(function() {
        $('#followSearch').on('keypress', function(event) {
            if (event.key === 'Enter') {
                event.preventDefault();
                $('#btnSearch').click();
            }
        });
    });
</script>
</body>
</html>
