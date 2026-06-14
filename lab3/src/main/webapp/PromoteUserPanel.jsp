<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="epaw.lab3.model.User" %>
<%
    User targetUser = (User) request.getAttribute("targetUser");
%>

<div class="card card-flush">
    <div class="card-head panel-head">
        <h5><img src="assets/icons/admin-blanco.png" alt="" class="ico"> Promote user</h5>
        <button type="button" class="panel-close" onclick="$('#rcolumn').html('<p/>')">&times;</button>
    </div>
    <div class="card-body">
        <% if (targetUser != null) { %>
            <p class="hint" style="margin-bottom: 1rem;">
                You are promoting <strong>@<%= targetUser.getUsername() %></strong> to administrator.
            </p>
        <% } %>

        <form id="promoteUserForm" onsubmit="return false;">
            <input type="hidden" name="userId" value="<%= targetUser != null ? targetUser.getId() : "" %>">

            <div class="form-field">
                <label for="promoteAdminPassword">Your password</label>
                <input class="input" type="password" id="promoteAdminPassword"
                    name="password" required autocomplete="current-password">
                <p class="field-error" id="promotePasswordError" style="display: none;"></p>
            </div>

            <button type="button" class="btn btn-primary btn-block" id="promoteUserConfirmBtn">
                <img src="assets/icons/admin-blanco.png" alt="" class="ico"> Confirm promote
            </button>
            <button type="button" class="btn btn-muted btn-block" style="margin-top:8px" onclick="$('#rcolumn').html('<p/>')">Cancel</button>
        </form>
    </div>
</div>

<script>
(function() {
    const form = document.getElementById('promoteUserForm');
    if (!form) return;

    $('#promoteUserConfirmBtn').on('click', function() {
        const userId = $('input[name="userId"]', form).val();
        const password = $('#promoteAdminPassword').val();

        if (!password || password.trim() === '') {
            $('#promoteAdminPassword').addClass('input-error');
            $('#promotePasswordError').text('Please enter your password.').show();
            return;
        }

        $('#promoteAdminPassword').removeClass('input-error');
        $('#promotePasswordError').hide();

        $.ajax({
            type: 'POST',
            url: 'AdminPanel',
            data: { userId: userId, action: 'promote', password: password },
            dataType: 'json'
        }).done(function(res) {
            if (!res || !res.ok) return;
            $('#rcolumn').html('<p/>');
            $('#row-' + userId).fadeOut(300, function() {
                $(this).remove();
            });
        }).fail(function(xhr) {
            if (xhr.status === 403) {
                $('#promoteAdminPassword').addClass('input-error');
                $('#promotePasswordError').text('Incorrect password.').show();
            } else {
                $('#promoteAdminPassword').addClass('input-error');
                $('#promotePasswordError').text('Could not promote this user.').show();
            }
        });
    });
})();
</script>
