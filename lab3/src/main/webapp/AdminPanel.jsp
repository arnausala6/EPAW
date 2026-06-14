<%@ page import="epaw.lab3.model.User" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    Integer totalUsers = (Integer) request.getAttribute("totalUsers");
    Integer blockedUsers = (Integer) request.getAttribute("blockedUsers");
    Integer activeGroups = (Integer) request.getAttribute("activeGroups");
    List<User> users = (List<User>) request.getAttribute("users");
    String mode = (String) request.getAttribute("mode");
    Integer currentUserId = (Integer) request.getAttribute("currentUserId");
    
    if (totalUsers == null) totalUsers = 0;
    if (blockedUsers == null) blockedUsers = 0;
    if (activeGroups == null) activeGroups = 0;
    if (mode == null) mode = "users";
    if (currentUserId == null) currentUserId = -1;
    
    boolean isUsersMode = "users".equalsIgnoreCase(mode);
%>
<div class="card card-flush page-head">
    <div class="card-head">
        <h3><img src="assets/icons/admin-blanco.png" alt="" class="ico"> Admin Panel</h3>
    </div>
    <div class="card-body">
        <div class="stat-grid">
            <div class="card stat-card">
                <h2><%= totalUsers %></h2>
                <p class="text-muted">Total users</p>
            </div>
            <div class="card stat-card">
                <h2><%= blockedUsers %></h2>
                <p class="text-muted">Blocked users</p>
            </div>
            <div class="card stat-card">
                <h2><%= activeGroups %></h2>
                <p class="text-muted">Active groups</p>
            </div>
        </div>

        <div class="timeline-tabs-bar">
            <a href="AdminPanel?mode=users"
               class="timeline-tab <%= isUsersMode ? "timeline-tab-active" : "" %>"
               onclick="event.preventDefault(); window.loadContent('AdminPanel?mode=users');">
                Users
            </a>
            <a href="AdminPanel?mode=admins"
               class="timeline-tab <%= !isUsersMode ? "timeline-tab-active" : "" %>"
               onclick="event.preventDefault(); window.loadContent('AdminPanel?mode=admins');">
                Admins
            </a>
        </div>

        <div class="card" style="margin-top: 12px;">
            <table class="data-table admin-table">
                <thead>
                    <tr>
                        <th style="width: 60px;"></th>
                        <th>Name</th>
                        <th>Description</th>
                        <th style="width: 120px; text-align: center;">Actions</th>
                    </tr>
                </thead>
                <tbody id="admin-user-list">
                    <% if (users != null && !users.isEmpty()) { %>
                        <% for (User user : users) { 
                            boolean isCurrentUser = user.getId() == currentUserId;
                            String rowClass = "";
                            if (isCurrentUser) {
                                rowClass = "admin-row-self";
                            } else if (user.isBlocked()) {
                                rowClass = "admin-row-blocked";
                            }
                        %>
                            <tr id="row-<%= user.getId() %>" class="<%= rowClass %>" data-user-id="<%= user.getId() %>" data-blocked="<%= user.isBlocked() ? "true" : "false" %>">
                                <td class="admin-avatar-cell">
                                    <% if (user.getPicture() != null && !user.getPicture().isBlank()) { %>
                                        <img src="<%= user.getPicture() %>" alt="<%= user.getUsername() %>" class="admin-avatar">
                                    <% } else { %>
                                        <div class="admin-avatar admin-avatar-placeholder">
                                            <%= user.getUsername().substring(0, 1).toUpperCase() %>
                                        </div>
                                    <% } %>
                                </td>
                                <td>
                                    <strong><%= user.getUsername() %></strong>
                                </td>
                                <td>
                                    <span class="admin-user-desc"><%= user.getDescription() != null && !user.getDescription().isBlank() ? user.getDescription() : "" %></span>
                                </td>
                                <td class="admin-actions-cell">
                                    <% if (!isCurrentUser) { %>
                                        <% if (isUsersMode) { %>
                                            <% if (user.isBlocked()) { %>
                                                <div class="admin-actions-inner">
                                                    <button type="button" class="btn-icon btn-unblock-user" data-user-id="<%= user.getId() %>" title="Unblock user">
                                                        <img src="assets/icons/unblock-ok.png" alt="Unblock" class="ico-action">
                                                    </button>
                                                </div>
                                            <% } else { %>
                                                <div class="admin-actions-inner">
                                                    <div class="admin-actions-group">
                                                        <button type="button" class="btn-icon btn-block-user" data-user-id="<%= user.getId() %>" title="Block user">
                                                            <img src="assets/icons/block-error.png" alt="Block" class="ico-action">
                                                        </button>
                                                        <button type="button" class="btn-icon btn-promote-user" data-user-id="<%= user.getId() %>" title="Promote to admin">
                                                            <img src="assets/icons/admin-verde.png" alt="Promote" class="ico-action">
                                                        </button>
                                                    </div>
                                                </div>
                                            <% } %>
                                        <% } else { %>
                                            <div class="admin-actions-inner">
                                                <button type="button" class="btn-icon btn-demote-user" data-user-id="<%= user.getId() %>" title="Demote to user">
                                                    <img src="assets/icons/perfil-terracota.png" alt="Demote" class="ico-action">
                                                </button>
                                            </div>
                                        <% } %>
                                    <% } %>
                                </td>
                            </tr>
                        <% } %>
                    <% } else { %>
                        <tr>
                            <td colspan="4" class="text-muted" style="text-align: center;">No <%= isUsersMode ? "users" : "administrators" %> available.</td>
                        </tr>
                    <% } %>
                </tbody>
            </table>
        </div>
    </div>
</div>

<script>
(function() {
    function showBlockPanel(userId) {
        $('#rcolumn').load('BlockUser?userId=' + userId);
    }

    function unblockUser(userId) {
        $.ajax({
            type: 'POST',
            url: 'AdminPanel',
            data: { userId: userId, action: 'unblock' },
            dataType: 'json'
        }).done(function (res) {
            if (res && res.ok) {
                const row = $('#row-' + userId);
                row.removeClass('admin-row-blocked');
                row.attr('data-blocked', 'false');
                
                const actionsCell = row.find('.admin-actions-cell');
                actionsCell.html(
                    '<div class="admin-actions-inner">' +
                    '<div class="admin-actions-group">' +
                    '<button type="button" class="btn-icon btn-block-user" data-user-id="' + userId + '" title="Block user">' +
                    '<img src="assets/icons/block-error.png" alt="Block" class="ico-action">' +
                    '</button>' +
                    '<button type="button" class="btn-icon btn-promote-user" data-user-id="' + userId + '" title="Promote to admin">' +
                    '<img src="assets/icons/admin-verde.png" alt="Promote" class="ico-action">' +
                    '</button>' +
                    '</div>' +
                    '</div>'
                );
                
                const counter = $('.stat-grid .card:nth-child(2) h2');
                const current = parseInt(counter.text(), 10) || 0;
                counter.text(Math.max(0, current - 1));
            }
        });
    }

    function promoteUser(userId) {
        $('#rcolumn').load('PromoteUser?userId=' + userId);
    }

    function demoteUser(userId) {
        $('#rcolumn').load('DemoteUser?userId=' + userId);
    }

    $(document).off('click.adminBlock').on('click.adminBlock', '.btn-block-user', function () {
        const userId = $(this).data('user-id');
        showBlockPanel(userId);
    });

    $(document).off('click.adminUnblock').on('click.adminUnblock', '.btn-unblock-user', function () {
        const userId = $(this).data('user-id');
        unblockUser(userId);
    });

    $(document).off('click.adminPromote').on('click.adminPromote', '.btn-promote-user', function () {
        const userId = $(this).data('user-id');
        promoteUser(userId);
    });

    $(document).off('click.adminDemote').on('click.adminDemote', '.btn-demote-user', function () {
        const userId = $(this).data('user-id');
        demoteUser(userId);
    });
})();
</script>
