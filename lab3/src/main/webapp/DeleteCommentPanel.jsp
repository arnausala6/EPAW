<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<div class="card card-flush">
	<div class="card-head panel-head">
		<h5><img src="assets/icons/delete-white.png" alt="" class="ico"> Delete comment</h5>
		<button type="button" class="panel-close" onclick="$('#rcolumn').html('<p/>')">&times;</button>
	</div>
	<div class="card-body">
		<c:if test="${not empty errors.commentId}">
			<p class="field-error">${errors.commentId}</p>
		</c:if>

		<div class="edit-group-confirm">
			<p class="edit-group-confirm-text">This comment will be permanently deleted.</p>
			<div class="comment-delete-preview">
				<p class="comment-delete-preview-body">${comment.content}</p>
			</div>
			<div class="edit-group-confirm-actions">
				<button type="button" class="btn btn-err btn-sm" id="deleteCommentConfirmBtn">
					<img src="assets/icons/delete-error.png" alt="" class="ico"> Confirm delete
				</button>
				<button type="button" class="btn btn-muted btn-sm" id="deleteCommentCancelBtn">Cancel</button>
			</div>
		</div>

		<input type="hidden" id="deleteCommentId" value="${comment.postId}">
		<input type="hidden" id="deleteCommentPostId" name="postId" value="${postId}">
	</div>
</div>

<script>
(function() {
	const confirmBtn = document.getElementById('deleteCommentConfirmBtn');
	const cancelBtn = document.getElementById('deleteCommentCancelBtn');
	const commentId = document.getElementById('deleteCommentId').value;
	const postId = document.getElementById('deleteCommentPostId').value;

	if (cancelBtn) {
		cancelBtn.addEventListener('click', function() {
			$('#rcolumn').html('<p/>');
		});
	}

	if (confirmBtn) {
		confirmBtn.addEventListener('click', function() {
			confirmBtn.disabled = true;
			$.post('DeleteComment', { commentId: commentId }).done(function(html) {
				if (html.indexOf('id="deleteCommentConfirmBtn"') !== -1) {
					$('#rcolumn').html(html);
				} else {
					const thread = $('#content').find('.comment-thread[data-post-id="' + postId + '"]');
					if (thread.length) {
						thread.find('.comment-thread-inner').html(html);
						thread.find('.comment-thread-inner').data('loaded', true);
						thread.show();
						const countSpan = $('#content').find(
							'.btn-toggle-comments[data-post-id="' + postId + '"] .comment-count');
						const current = parseInt(countSpan.text() || '0', 10);
						countSpan.text(Math.max(0, current - 1));
					}
					$('#rcolumn').html('<p/>');
				}
			}).always(function() {
				confirmBtn.disabled = false;
			});
		});
	}
})();
</script>
