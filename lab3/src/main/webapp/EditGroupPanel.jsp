<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<c:set var="soleMember" value="${group.memberCount != null and group.memberCount <= 1}" />

<div class="card card-flush">
	<div class="card-head panel-head">
		<h5><img src="assets/icons/grupos-blanco.png" alt="" class="ico"> Edit group</h5>
		<button type="button" class="panel-close" onclick="$('#rcolumn').html('<p/>')">&times;</button>
	</div>
	<div class="card-body">
		<form id="editGroupForm" action="EditGroup" method="POST" enctype="multipart/form-data">
			<input type="hidden" name="groupId" value="${group.groupId}">

			<div class="form-field">
				<label for="editGroupName">Group name</label>
				<input class="input${not empty errors.groupName ? ' input-error' : ''}" type="text" id="editGroupName" name="groupName" required maxlength="50"
					value="${group.groupName}">
				<c:if test="${not empty errors.groupName}">
					<p class="field-error">${errors.groupName}</p>
				</c:if>
			</div>

			<div class="form-field">
				<label for="editGroupDescription">Description</label>
				<textarea class="input input-optional${not empty errors.groupDescription ? ' input-error' : ''}" id="editGroupDescription" name="groupDescription"
					style="height: 80px;" maxlength="300">${group.description}</textarea>
				<c:if test="${not empty errors.groupDescription}">
					<p class="field-error">${errors.groupDescription}</p>
				</c:if>
			</div>

			<div class="form-field">
				<label for="editGroupImage">Group image</label>
				<input class="input input-optional${not empty errors.groupImage ? ' input-error' : ''}" type="file" id="editGroupImage" name="groupImage"
					accept="image/jpeg,image/png,image/webp,image/gif">
				<c:if test="${not empty errors.groupImage}">
					<p class="field-error">${errors.groupImage}</p>
				</c:if>
			</div>

			<div class="form-field">
				<label for="editGroupVisibility">Visibility</label>
				<select class="input${not empty errors.groupVisibility ? ' input-error' : ''}" id="editGroupVisibility" name="groupVisibility" required>
					<option value="public" ${group.privacy != 'private' ? 'selected' : ''}>Public</option>
					<option value="private" ${group.privacy == 'private' ? 'selected' : ''}>Private</option>
				</select>
				<c:if test="${not empty errors.groupVisibility}">
					<p class="field-error">${errors.groupVisibility}</p>
				</c:if>
			</div>

			<c:if test="${not empty errors.groupId}">
				<p class="field-error">${errors.groupId}</p>
			</c:if>

			<button type="submit" class="btn btn-primary btn-block">
				<img src="assets/icons/edit-blanco.png" alt="" class="ico"> Save changes
			</button>
		</form>

		<button type="button" class="btn ${soleMember ? 'btn-err' : 'btn-muted'} btn-block" id="editGroupLeaveBtn" style="margin-top: 12px;">
			<c:choose>
				<c:when test="${soleMember}">
					<img src="assets/icons/delete-error.png" alt="" class="ico"> Delete group
				</c:when>
				<c:otherwise>
					<img src="assets/icons/log-out-suave.png" alt="" class="ico"> Leave group
				</c:otherwise>
			</c:choose>
		</button>

		<div id="editGroupLeaveConfirm" class="edit-group-confirm" hidden>
			<p class="edit-group-confirm-text">
				<c:choose>
					<c:when test="${soleMember}">
						You are the only member. This group will be permanently deleted.
					</c:when>
					<c:otherwise>
						You will leave this group and it will no longer appear in your list.
					</c:otherwise>
				</c:choose>
			</p>
			<div class="edit-group-confirm-actions">
				<button type="button" class="btn btn-err btn-sm" id="editGroupLeaveConfirmBtn">
					<c:choose>
						<c:when test="${soleMember}">
							<img src="assets/icons/delete-error.png" alt="" class="ico"> Confirm delete
						</c:when>
						<c:otherwise>
							<img src="assets/icons/log-out-suave.png" alt="" class="ico"> Confirm leave
						</c:otherwise>
					</c:choose>
				</button>
				<button type="button" class="btn btn-muted btn-sm" id="editGroupLeaveCancelBtn">Cancel</button>
			</div>
		</div>
	</div>
</div>

<script>
(function() {
	const form = document.getElementById('editGroupForm');
	if (form) {
		form.addEventListener('submit', function(event) {
			event.preventDefault();
			event.stopImmediatePropagation();

			const data = new FormData(form);
			$.ajax({
				type: 'POST',
				enctype: 'multipart/form-data',
				url: form.action,
				data: data,
				processData: false,
				contentType: false
			}).done(function(html) {
				if (html.indexOf('id="editGroupForm"') !== -1) {
					$('#rcolumn').html(html);
				} else {
					$('#rcolumn').html('<p/>');
					$('#content').html(html);
				}
			});
		}, true);
	}

	const leaveBtn = document.getElementById('editGroupLeaveBtn');
	const confirmPanel = document.getElementById('editGroupLeaveConfirm');
	const confirmBtn = document.getElementById('editGroupLeaveConfirmBtn');
	const cancelBtn = document.getElementById('editGroupLeaveCancelBtn');
	const groupId = '${group.groupId}';

	if (!leaveBtn || !confirmPanel || !confirmBtn || !cancelBtn) return;

	leaveBtn.addEventListener('click', function() {
		confirmPanel.hidden = false;
	});

	cancelBtn.addEventListener('click', function() {
		confirmPanel.hidden = true;
	});

	confirmBtn.addEventListener('click', function() {
		$.post('LeaveGroup', { groupId: groupId }).done(function(html) {
			if (html.indexOf('id="editGroupForm"') !== -1) {
				$('#rcolumn').html(html);
			} else {
				$('#rcolumn').html('<p/>');
				$('#content').html(html);
			}
		});
	});
})();
</script>
