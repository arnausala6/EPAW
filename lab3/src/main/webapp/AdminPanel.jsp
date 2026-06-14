<%@ page import="epaw.lab3.model.User" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    Integer totalUsers = (Integer) request.getAttribute("totalUsers");
    Integer blockedUsers = (Integer) request.getAttribute("blockedUsers");
    Integer activeGroups = (Integer) request.getAttribute("activeGroups");
    List<User> users = (List<User>) request.getAttribute("users");
    if (totalUsers == null) totalUsers = 0;
    if (blockedUsers == null) blockedUsers = 0;
    if (activeGroups == null) activeGroups = 0;
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

        <div class="card">
            <h4><img src="assets/icons/grupos-terracota.png" alt="" class="ico"> User management</h4>
            <table class="data-table">
                <thead>
                    <tr>
                        <th>Username</th>
                        <th>Role</th>
                        <th>Status</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody id="admin-user-list">
                    <% if (users != null && !users.isEmpty()) { %>
                        <% for (User user : users) { %>
                            <tr id="row-<%= user.getId() %>">
                                <td>
                                    <strong><%= user.getUsername() %></strong>
                                    <% if (user.getDescription() != null && !user.getDescription().isBlank()) { %>
                                        <div class="post-meta"><%= user.getDescription() %></div>
                                    <% } %>
                                </td>
                                <td><span class="chip"><%= user.getRole() != null ? user.getRole() : "user" %></span></td>
                                <td>
                                    <span class="chip <%= user.isBlocked() ? "chip-err" : "chip-ok" %>">
                                        <%= user.isBlocked() ? "blocked" : "active" %>
                                    </span>
                                </td>
                                <td>
                                    <% if (user.isBlocked()) { %>
                                        <button type="button" class="btn btn-muted btn-sm btn-unblock-user" data-user-id="<%= user.getId() %>">
                                            <img src="assets/icons/unblock-ok.png" alt="" class="ico"> Unblock
                                        </button>
                                    <% } else { %>
                                        <button type="button" class="btn btn-err btn-sm btn-block-user" data-user-id="<%= user.getId() %>">
                                            <img src="assets/icons/block-error.png" alt="" class="ico"> Block
                                        </button>
                                    <% } %>
                                </td>
                            </tr>
                        <% } %>
                    <% } else { %>
                        <tr>
                            <td colspan="4" class="text-muted">No users available.</td>
                        </tr>
                    <% } %>
                </tbody>
            </table>
            <p class="hint">Use the block/unblock buttons to manage users directly from this panel.</p>
        </div>
    </div>
</div>

<script>
function applyStatusUI(row, action, userId) {
    const statusCell = row.querySelector('td:nth-child(3) span');
    const actionCell = row.querySelector('td:nth-child(4)');

    if (action === 'block') {
        statusCell.textContent = 'blocked';
        statusCell.className = 'chip chip-err';
        actionCell.innerHTML = '<button type="button" class="btn btn-muted btn-sm btn-unblock-user" data-user-id="' + userId + '"><img src="assets/icons/unblock-ok.png" alt="" class="ico"> Unblock</button>';
    } else {
        statusCell.textContent = 'active';
        statusCell.className = 'chip chip-ok';
        actionCell.innerHTML = '<button type="button" class="btn btn-err btn-sm btn-block-user" data-user-id="' + userId + '"><img src="assets/icons/block-error.png" alt="" class="ico"> Block</button>';
    }
}

function refreshBlockedCounter(delta) {
    const counter = document.querySelector('.stat-grid .card:nth-child(2) h2');
    if (!counter) return;
    const current = parseInt(counter.textContent, 10) || 0;
    counter.textContent = Math.max(0, current + delta);
}

function setAdminStatus(userId, action) {
    const row = document.getElementById('row-' + userId);
    if (!row) return;

    const ajaxOptions = {
        type: 'POST',
        dataType: 'json'
    };

    if (action === 'block') {
        ajaxOptions.url = 'Block';
        ajaxOptions.data = { userId: userId };
    } else {
        ajaxOptions.url = 'AdminPanel';
        ajaxOptions.data = { userId: userId, action: 'unblock' };
    }

    $.ajax(ajaxOptions).done(function (res) {
        if (!res || res.ok !== true) {
            return;
        }

        applyStatusUI(row, action, userId);
        if (action === 'block') {
            refreshBlockedCounter(1);
        } else {
            refreshBlockedCounter(-1);
        }
    });
}

$(document).off('click.adminPanelBlock').on('click.adminPanelBlock', '.btn-block-user', function () {
    const userId = $(this).data('user-id');
    setAdminStatus(userId, 'block');
});

$(document).off('click.adminPanelUnblock').on('click.adminPanelUnblock', '.btn-unblock-user', function () {
    const userId = $(this).data('user-id');
    setAdminStatus(userId, 'unblock');
});
</script>
