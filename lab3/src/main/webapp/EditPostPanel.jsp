<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<div class="card card-flush">
	<div class="card-head panel-head">
		<h5><img src="assets/icons/edit-blanco.png" alt="" class="ico"> Edit post</h5>
		<button type="button" class="panel-close" onclick="$('#rcolumn').html('<p/>')">&times;</button>
	</div>
	<div class="card-body">
		<c:if test="${not empty errors.postId}">
			<p class="field-error">${errors.postId}</p>
		</c:if>

		<form id="editPostForm" action="EditPost" method="POST" enctype="multipart/form-data">
			<input type="hidden" name="postId" value="${post.postId}">
			<input type="hidden" name="returnView" value="${returnView}">

			<div class="form-field">
				<label for="editPostContent">Content</label>
				<textarea class="input${not empty errors.content ? ' input-error' : ''}"
					name="content" id="editPostContent"
					style="height: 120px;" required
					placeholder="Edit your post...">${not empty content ? content : post.content}</textarea>
				<c:if test="${not empty errors.content}">
					<p class="field-error">${errors.content}</p>
				</c:if>
			</div>

			<c:choose>
				<c:when test="${not empty post.postPicture}">
					<c:if test="${not removeImage}">
						<div class="form-field">
							<label>Current image</label>
							<div class="edit-post-image-preview">
								<img src="${post.postPicture}" alt="">
							</div>
						</div>
					</c:if>
					<div class="form-field">
						<label class="edit-post-remove-image">
							<input type="checkbox" name="removeImage" value="true"${removeImage == true ? ' checked' : ''}>
							<span>Remove image</span>
						</label>
					</div>
				</c:when>
				<c:otherwise>
					<div class="form-field">
						<label for="editPostPicture">Post image</label>
						<input class="input input-optional${not empty errors.postPicture ? ' input-error' : ''}"
							type="file" id="editPostPicture" name="postPicture"
							accept="image/jpeg,image/png,image/webp,image/gif">
						<c:if test="${not empty errors.postPicture}">
							<p class="field-error">${errors.postPicture}</p>
						</c:if>
					</div>
				</c:otherwise>
			</c:choose>

			<button type="submit" class="btn btn-primary btn-block">
				<img src="assets/icons/edit-blanco.png" alt="" class="ico"> Save
			</button>
		</form>
	</div>
</div>

<script>
(function() {
	const form = document.getElementById('editPostForm');
	if (!form) return;

	form.addEventListener('submit', function(event) {
		event.preventDefault();
		event.stopImmediatePropagation();

		const postId = form.querySelector('[name="postId"]').value;
		const submitBtn = form.querySelector('[type="submit"]');
		submitBtn.disabled = true;

		const data = new FormData(form);
		$.ajax({
			type: 'POST',
			enctype: 'multipart/form-data',
			url: form.action,
			data: data,
			processData: false,
			contentType: false
		}).done(function(html) {
			if (html.indexOf('id="editPostForm"') !== -1) {
				if (window.App && App.mountRcolumnPanel) {
					App.mountRcolumnPanel(html, 'editPostForm');
				} else {
					$('#rcolumn').html(html);
				}
			} else {
				const article = $('#content').find('article[data-post-id="' + postId + '"]');
				if (article.length) {
					article.replaceWith(html);
				}
				$('#rcolumn').html('<p/>');
			}
		}).always(function() {
			submitBtn.disabled = false;
		});
	}, true);
})();
</script>
