<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<div class="card card-flush">
	<div class="card-head panel-head">
		<h5><img src="assets/icons/grupos-blanco.png" alt="" class="ico"> Create group</h5>
		<button type="button" class="panel-close" onclick="$('#rcolumn').html('<p/>')">&times;</button>
	</div>
	<div class="card-body">
		<form id="createGroupForm" action="CreateGroup" method="POST" enctype="multipart/form-data">
			<div class="form-field">
				<label for="groupName">Group name</label>
				<input class="input${not empty errors.groupName ? ' input-error' : ''}" type="text" id="groupName" name="groupName" required maxlength="50"
					placeholder="Example: Web Engineering Lab 3"
					value="${not empty group ? group.groupName : ''}">
				<c:if test="${not empty errors.groupName}">
					<p class="field-error">${errors.groupName}</p>
				</c:if>
			</div>

			<div class="form-field">
				<label for="groupDescription">Description</label>
				<textarea class="input input-optional${not empty errors.groupDescription ? ' input-error' : ''}" id="groupDescription" name="groupDescription"
					style="height: 80px;" maxlength="300" placeholder="What is this group about?">${not empty group ? group.description : ''}</textarea>
				<c:if test="${not empty errors.groupDescription}">
					<p class="field-error">${errors.groupDescription}</p>
				</c:if>
			</div>

			<div class="form-field">
				<label for="groupImage">Group image</label>
				<input class="input input-optional${not empty errors.groupImage ? ' input-error' : ''}" type="file" id="groupImage" name="groupImage"
					accept="image/jpeg,image/png,image/webp,image/gif">
				<c:if test="${not empty errors.groupImage}">
					<p class="field-error">${errors.groupImage}</p>
				</c:if>
			</div>

			<div class="form-field">
				<label for="groupVisibility">Visibility</label>
				<select class="input${not empty errors.groupVisibility ? ' input-error' : ''}" id="groupVisibility" name="groupVisibility" required>
					<option value="public" ${not empty group && group.privacy == 'private' ? '' : 'selected'}>Public</option>
					<option value="private" ${not empty group && group.privacy == 'private' ? 'selected' : ''}>Private</option>
				</select>
				<c:if test="${not empty errors.groupVisibility}">
					<p class="field-error">${errors.groupVisibility}</p>
				</c:if>
			</div>

			<button type="submit" class="btn btn-primary btn-block">
				<img src="assets/icons/publicar-blanco.png" alt="" class="ico"> Create group
			</button>
		</form>
	</div>
</div>

<script>
(function() {
	const form = document.getElementById('createGroupForm');
	if (!form) return;

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
			if (html.indexOf('id="createGroupForm"') !== -1) {
				$('#rcolumn').html(html);
			} else {
				$('#rcolumn').html('<p/>');
				$('#content').html(html);
			}
		});
	}, true);
})();
</script>
