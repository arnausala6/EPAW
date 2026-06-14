<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="epaw.lab3.model.User" %>
<%
    User targetUser = (User) request.getAttribute("targetUser");
%>

<div class="card card-flush">
    <div class="card-head panel-head">
        <h5><img src="assets/icons/perfil-terracota.png" alt="" class="ico"> Demote admin</h5>
        <button type="button" class="panel-close" onclick="$('#rcolumn').html('<p/>')">&times;</button>
    </div>
    <div class="card-body">
        <% if (targetUser != null) { %>
            <p class="hint" style="margin-bottom: 1rem;">
                You are demoting <strong>@<%= targetUser.getUsername() %></strong> back to a regular user.
                Their admin privileges will be removed immediately.
            </p>
        <% } %>

        <input type="hidden" id="demoteTargetUserId" value="<%= targetUser != null ? targetUser.getId() : "" %>">

        <button type="button" class="btn btn-err btn-block" id="demoteUserConfirmBtn">
            <img src="assets/icons/perfil-terracota.png" alt="" class="ico"> Confirm demote
        </button>
        <button type="button" class="btn btn-muted btn-block" style="margin-top:8px" onclick="$('#rcolumn').html('<p/>')">Cancel</button>
    </div>
</div>

<script>
(function() {
    $('#demoteUserConfirmBtn').on('click', function() {
        const userId = $('#demoteTargetUserId').val();

        $.ajax({
            type: 'POST',
            url: 'AdminPanel',
            data: { userId: userId, action: 'demote' },
            dataType: 'json'
        }).done(function(res) {
            if (!res || !res.ok) return;
            $('#rcolumn').html('<p/>');
            $('#row-' + userId).fadeOut(300, function() {
                $(this).remove();
            });
        }).fail(function() {
            alert('Could not demote this user. Please try again.');
        });
    });
})();
</script>
