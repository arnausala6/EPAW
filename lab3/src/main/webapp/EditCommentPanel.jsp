<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<div class="card card-flush">
	<div class="card-head panel-head">
		<h5><img src="assets/icons/edit-blanco.png" alt="" class="ico"> Edit comment</h5>
		<button type="button" class="panel-close" onclick="$('#rcolumn').html('<p/>')">&times;</button>
	</div>
	<div class="card-body">
		<c:if test="${not empty errors.commentId}">
			<p class="field-error">${errors.commentId}</p>
		</c:if>

		<form id="editCommentForm" action="EditComment" method="POST">
			<input type="hidden" name="commentId" value="${comment.postId}">
			<input type="hidden" name="postId" value="${postId}">

			<div class="form-field">
				<textarea class="input${not empty errors.content ? ' input-error' : ''}"
					name="content" id="editCommentContent"
					style="height: 90px;" maxlength="500" required
					placeholder="Edit your comment...">${not empty content ? content : comment.content}</textarea>
				<c:if test="${not empty errors.content}">
					<p class="field-error">${errors.content}</p>
				</c:if>
			</div>

			<button type="submit" class="btn btn-primary btn-block">
				<img src="assets/icons/edit-blanco.png" alt="" class="ico"> Save
			</button>
		</form>
	</div>
</div>

<script>
(function() {
	const form = document.getElementById('editCommentForm');
	if (!form) return;

	form.addEventListener('submit', function(event) {
		event.preventDefault();
		event.stopImmediatePropagation();

		const postId = form.querySelector('[name="postId"]').value;
		const submitBtn = form.querySelector('[type="submit"]');
		submitBtn.disabled = true;

		$.post(form.action, $(form).serialize()).done(function(html) {
			if (html.indexOf('id="editCommentForm"') !== -1) {
				if (window.App && App.mountRcolumnPanel) {
					App.mountRcolumnPanel(html, 'editCommentForm');
				} else {
					$('#rcolumn').html(html);
				}
			} else {
				const thread = $('#content').find('.comment-thread[data-post-id="' + postId + '"]');
				if (thread.length) {
					thread.find('.comment-thread-inner').html(html);
					thread.find('.comment-thread-inner').data('loaded', true);
					thread.show();
				}
				$('#rcolumn').html('<p/>');
			}
		}).always(function() {
			submitBtn.disabled = false;
		});
	}, true);
})();
</script>
