<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<c:set var="soleMember" value="${group.memberCount != null and group.memberCount <= 1}" />

<div class="card card-flush">
	<div class="card-head panel-head">
		<h5><img src="assets/icons/grupos-blanco.png" alt="" class="ico"> Leave group</h5>
		<button type="button" class="panel-close" onclick="$('#rcolumn').html('<p/>')">&times;</button>
	</div>
	<div class="card-body">
		<c:if test="${not empty errors.groupId}">
			<p class="field-error">${errors.groupId}</p>
		</c:if>

		<div class="edit-group-confirm">
			<p class="edit-group-confirm-text">
				<c:choose>
					<c:when test="${soleMember}">
						You are the only member. Leaving will permanently delete this group.
					</c:when>
					<c:otherwise>
						You will leave this group and it will no longer appear in your list.
					</c:otherwise>
				</c:choose>
			</p>
			<div class="edit-group-confirm-actions">
				<button type="button" class="btn btn-err btn-sm" id="leaveGroupConfirmBtn">
					<c:choose>
						<c:when test="${soleMember}">
							<img src="assets/icons/delete-error.png" alt="" class="ico"> Confirm delete
						</c:when>
						<c:otherwise>
							<img src="assets/icons/log-out-suave.png" alt="" class="ico"> Confirm leave
						</c:otherwise>
					</c:choose>
				</button>
				<button type="button" class="btn btn-muted btn-sm" id="leaveGroupCancelBtn">Cancel</button>
			</div>
		</div>
	</div>
</div>

<script>
(function() {
	const confirmBtn = document.getElementById('leaveGroupConfirmBtn');
	const cancelBtn = document.getElementById('leaveGroupCancelBtn');
	const groupId = '${group.groupId}';

	if (cancelBtn) {
		cancelBtn.addEventListener('click', function() {
			$('#rcolumn').html('<p/>');
		});
	}

	if (confirmBtn) {
		confirmBtn.addEventListener('click', function() {
			$.post('LeaveGroup', { groupId: groupId }).done(function(html) {
				if (html.indexOf('id="leaveGroupConfirmBtn"') !== -1) {
					$('#rcolumn').html(html);
				} else {
					$('#rcolumn').html('<p/>');
					$('#content').html(html);
				}
			});
		});
	}
})();
</script>
