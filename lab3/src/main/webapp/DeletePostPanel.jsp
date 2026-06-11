<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<div class="card card-flush">
	<div class="card-head panel-head">
		<h5><img src="assets/icons/delete-white.png" alt="" class="ico"> Delete post</h5>
		<button type="button" class="panel-close" onclick="$('#rcolumn').html('<p/>')">&times;</button>
	</div>
	<div class="card-body">
		<c:if test="${not empty errors.postId}">
			<p class="field-error">${errors.postId}</p>
		</c:if>

		<div class="edit-group-confirm">
			<p class="edit-group-confirm-text">This post will be permanently deleted.</p>
			<div class="comment-delete-preview">
				<p class="comment-delete-preview-body">${post.content}</p>
			</div>
			<div class="edit-group-confirm-actions">
				<button type="button" class="btn btn-err btn-sm" id="deletePostConfirmBtn">
					<img src="assets/icons/delete-error.png" alt="" class="ico"> Confirm delete
				</button>
				<button type="button" class="btn btn-muted btn-sm" id="deletePostCancelBtn">Cancel</button>
			</div>
		</div>

		<input type="hidden" id="deletePostId" value="${post.postId}">
		<input type="hidden" id="deletePostReturnView" value="${returnView}">
	</div>
</div>

<script>
(function() {
	const confirmBtn = document.getElementById('deletePostConfirmBtn');
	const cancelBtn = document.getElementById('deletePostCancelBtn');
	const postId = document.getElementById('deletePostId').value;
	const returnView = document.getElementById('deletePostReturnView').value;

	if (cancelBtn) {
		cancelBtn.addEventListener('click', function() {
			$('#rcolumn').html('<p/>');
		});
	}

	if (confirmBtn) {
		confirmBtn.addEventListener('click', function() {
			confirmBtn.disabled = true;
			$.post('DeletePost', { postId: postId, returnView: returnView }).done(function(html) {
				if (html.indexOf('id="deletePostConfirmBtn"') !== -1) {
					$('#rcolumn').html(html);
				} else if (html.indexOf('id="postDeleteResult"') !== -1) {
					$('#content').find('article[data-post-id="' + postId + '"]').remove();
					$('#rcolumn').html('<p/>');
				} else {
					$('#rcolumn').html('<p/>');
				}
			}).always(function() {
				confirmBtn.disabled = false;
			});
		});
	}
})();
</script>
