<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<div class="card card-flush">
	<div class="card-head panel-head">
		<h5><img src="assets/icons/mail-terracota.png" alt="" class="ico"> Join requests</h5>
		<button type="button" class="panel-close" onclick="$('#rcolumn').html('<p/>')">&times;</button>
	</div>
	<div class="card-body">
		<c:if test="${not empty errors.groupId}">
			<p class="field-error">${errors.groupId}</p>
		</c:if>
		<c:if test="${not empty errors.userId}">
			<p class="field-error">${errors.userId}</p>
		</c:if>

		<c:if test="${empty joinRequests}">
			<p class="hint join-requests-empty">Join requests for this group will appear here.</p>
		</c:if>

		<c:forEach var="req" items="${joinRequests}">
			<div class="join-request-item">
				<div class="join-request-user">
					<div class="avatar avatar-photo join-request-avatar<c:if test="${not empty req.profilePicture}"> has-image</c:if>">
						<img src="${not empty req.profilePicture ? req.profilePicture : 'assets/icons/imagen-suave.png'}" alt="">
					</div>
					<span class="join-request-username">@${req.username}</span>
				</div>
				<div class="join-request-actions">
					<button type="button" class="btn btn-muted btn-sm btn-join-request-action"
						data-action="accept" data-user-id="${req.userId}" data-group-id="${group.groupId}" title="Accept">
						<img src="assets/icons/unblock-ok.png" alt="" class="ico">
					</button>
					<button type="button" class="btn btn-err btn-sm btn-join-request-action"
						data-action="reject" data-user-id="${req.userId}" data-group-id="${group.groupId}" title="Reject">
						<img src="assets/icons/block-error.png" alt="" class="ico">
					</button>
				</div>
			</div>
		</c:forEach>
	</div>
</div>

<script>
(function() {
	document.querySelectorAll('.btn-join-request-action').forEach(function(btn) {
		btn.addEventListener('click', function() {
			const action = btn.dataset.action;
			const groupId = btn.dataset.groupId;
			$.post('GroupJoinRequests', {
				action: action,
				userId: btn.dataset.userId,
				groupId: groupId
			}).done(function(html) {
				$('#rcolumn').html(html);
				if (action === 'accept') {
					$('#content').load('Groups?id=' + groupId);
				}
			});
		});
	});
})();
</script>
