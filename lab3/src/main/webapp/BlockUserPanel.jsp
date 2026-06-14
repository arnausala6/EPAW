<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ page import="epaw.lab3.model.User" %>
<%
    User targetUser = (User) request.getAttribute("targetUser");
%>

<div class="card card-flush">
	<div class="card-head panel-head">
		<h5><img src="assets/icons/block-blanco.png" alt="" class="ico"> Block user</h5>
		<button type="button" class="panel-close" onclick="$('#rcolumn').html('<p/>')">&times;</button>
	</div>
	<div class="card-body">
		<% if (targetUser != null) { %>
			<p class="hint" style="margin-bottom: 1rem;">
				You are about to block <strong>@<%= targetUser.getUsername() %></strong>
			</p>
		<% } %>

		<form id="blockUserForm" onsubmit="return false;">
			<input type="hidden" name="userId" value="<%= targetUser != null ? targetUser.getId() : "" %>">

			<div class="form-field">
				<label for="blockUserReason">Reason</label>
				<textarea class="input" id="blockUserReason" name="reason"
					style="height: 80px;" maxlength="300" required
					placeholder="Explain why this user is being blocked..."></textarea>
				<p class="field-error" id="blockReasonError" style="display: none;"></p>
			</div>

			<div class="form-field">
				<label for="blockUserPassword">Your password</label>
				<input class="input" type="password" id="blockUserPassword"
					name="password" required autocomplete="current-password">
				<p class="field-error" id="blockPasswordError" style="display: none;"></p>
			</div>

			<button type="submit" class="btn btn-err btn-block" id="blockUserConfirmBtn">
				<img src="assets/icons/block-error.png" alt="" class="ico"> Confirm block
			</button>
			<p class="hint">This will ban the user from the platform and delete all their posts.</p>
		</form>
	</div>
</div>

<script>
(function() {
	const form = document.getElementById('blockUserForm');
	if (!form) return;

	$('#blockUserConfirmBtn').on('click', function() {
		const userId = $('input[name="userId"]', form).val();
		const reason = $('#blockUserReason').val();
		const password = $('#blockUserPassword').val();

		let hasError = false;

		if (!reason || reason.trim() === '') {
			$('#blockUserReason').addClass('input-error');
			$('#blockReasonError').text('Please provide a reason for blocking this user.').show();
			hasError = true;
		} else {
			$('#blockUserReason').removeClass('input-error');
			$('#blockReasonError').hide();
		}

		if (!password || password.trim() === '') {
			$('#blockUserPassword').addClass('input-error');
			$('#blockPasswordError').text('Please enter your password to confirm.').show();
			hasError = true;
		} else {
			$('#blockUserPassword').removeClass('input-error');
			$('#blockPasswordError').hide();
		}

		if (hasError) return;

		$.ajax({
			type: 'POST',
			url: 'AdminPanel',
			data: { userId: userId, action: 'block', reason: reason, password: password },
			dataType: 'json'
		}).done(function(response) {
			$('#rcolumn').html('<p/>');
			
			const row = $('#row-' + userId);
			row.addClass('admin-row-blocked');
			row.attr('data-blocked', 'true');
			
			const actionsCell = row.find('.admin-actions-cell');
			actionsCell.html(
				'<button type="button" class="btn-icon btn-unblock-user" data-user-id="' + userId + '" title="Unblock user">' +
				'<img src="assets/icons/unblock-ok.png" alt="Unblock" class="ico-action">' +
				'</button>'
			);
			
			const counter = $('.stat-grid .card:nth-child(2) h2');
			const current = parseInt(counter.text(), 10) || 0;
			counter.text(current + 1);
		}).fail(function(xhr) {
			if (xhr.status === 403) {
				$('#blockUserPassword').addClass('input-error');
				$('#blockPasswordError').text('Incorrect password.').show();
			} else {
				$('#blockPasswordError').text('An error occurred while blocking the user.').show();
			}
		});
	});
})();
</script>
