<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<div class="card card-flush">
	<div class="card-head panel-head">
		<h5><img src="assets/icons/block-blanco.png" alt="" class="ico"> Block group</h5>
		<button type="button" class="panel-close" onclick="$('#rcolumn').html('<p/>')">&times;</button>
	</div>
	<div class="card-body">
		<c:if test="${not empty group}">
			<p class="hint block-post-target">${group.groupName}</p>
		</c:if>
		<c:if test="${not empty errors.groupId}">
			<p class="field-error">${errors.groupId}</p>
		</c:if>

		<form id="blockGroupForm" action="BlockGroup" method="POST">
			<input type="hidden" name="groupId" value="${group.groupId}">

			<div class="form-field">
				<label for="blockGroupReason">Reason</label>
				<textarea class="input${not empty errors.reason ? ' input-error' : ''}" id="blockGroupReason" name="reason"
					style="height: 80px;" maxlength="300" required
					placeholder="Explain why this group violates the rules...">${reason}</textarea>
				<c:if test="${not empty errors.reason}">
					<p class="field-error">${errors.reason}</p>
				</c:if>
			</div>

			<div class="form-field">
				<label>Also ban these members</label>
				<div class="card block-group-members-list">
					<c:if test="${empty members}">
						<p class="hint">This group has no members.</p>
					</c:if>
					<c:forEach var="member" items="${members}">
						<label class="block-group-member-option">
							<input type="checkbox" name="memberIds" value="${member.id}"${not empty selectedMemberIds and selectedMemberIds.contains(member.id) ? ' checked' : ''}>
							<span>@${member.username}</span>
						</label>
					</c:forEach>
				</div>
			</div>

			<div class="form-field">
				<label for="blockGroupPassword">Your password</label>
				<input class="input${not empty errors.password ? ' input-error' : ''}" type="password" id="blockGroupPassword"
					name="password" required autocomplete="current-password">
				<c:if test="${not empty errors.password}">
					<p class="field-error">${errors.password}</p>
				</c:if>
			</div>

			<button type="submit" class="btn btn-primary btn-block">
				<img src="assets/icons/block-blanco.png" alt="" class="ico"> Confirm block
			</button>
			<p class="hint">This action is permanent and cannot be undone.</p>
		</form>
	</div>
</div>

<script>
(function() {
	const form = document.getElementById('blockGroupForm');
	if (!form) return;

	form.addEventListener('submit', function(event) {
		event.preventDefault();
		event.stopImmediatePropagation();

		$.post(form.action, $(form).serialize()).done(function(html) {
			if (window.App && App.mountRcolumnPanel) {
				App.mountRcolumnPanel(html, 'blockGroupForm');
			} else if (html.indexOf('id="blockGroupForm"') !== -1) {
				$('#rcolumn').html(html);
			} else {
				$('#rcolumn').html('<p/>');
				$('#content').html(html);
			}
		});
	}, true);
})();
</script>
