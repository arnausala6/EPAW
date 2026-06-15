<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<div class="card card-flush">
	<div class="card-head panel-head">
		<h5><img src="assets/icons/block-blanco.png" alt="" class="ico"> Block comment</h5>
		<button type="button" class="panel-close" onclick="$('#rcolumn').html('<p/>')">&times;</button>
	</div>
	<div class="card-body">
		<c:if test="${not empty errors.commentId}">
			<p class="field-error">${errors.commentId}</p>
		</c:if>

		<p class="hint block-post-target">@${comment.username}</p>
		<div class="comment-delete-preview">
			<p class="comment-delete-preview-body">${comment.content}</p>
		</div>

		<form id="blockCommentForm" action="BlockComment" method="POST">
			<input type="hidden" name="commentId" value="${comment.postId}">
			<input type="hidden" id="blockCommentPostId" value="${postId}">

			<div class="form-field">
				<label for="blockCommentReason">Reason</label>
				<textarea class="input${not empty errors.reason ? ' input-error' : ''}" id="blockCommentReason" name="reason"
					style="height: 80px;" maxlength="300" required
					placeholder="Explain why this comment violates the rules...">${reason}</textarea>
				<c:if test="${not empty errors.reason}">
					<p class="field-error">${errors.reason}</p>
				</c:if>
			</div>

			<div class="form-field">
				<label for="blockCommentPassword">Your password</label>
				<input class="input${not empty errors.password ? ' input-error' : ''}" type="password" id="blockCommentPassword"
					name="password" required autocomplete="current-password">
				<c:if test="${not empty errors.password}">
					<p class="field-error">${errors.password}</p>
				</c:if>
			</div>

			<button type="submit" class="btn btn-primary btn-block">
				<img src="assets/icons/block-blanco.png" alt="" class="ico"> Confirm block
			</button>
			<p class="hint">This action permanently deletes the comment.</p>
		</form>
	</div>
</div>

<script>
(function() {
	const form = document.getElementById('blockCommentForm');
	if (!form) return;

	form.addEventListener('submit', function(event) {
		event.preventDefault();
		event.stopImmediatePropagation();

		const postId = document.getElementById('blockCommentPostId').value;
		const submitBtn = form.querySelector('button[type="submit"]');
		if (submitBtn) submitBtn.disabled = true;

		$.post(form.action, $(form).serialize()).done(function(html) {
			if (html.indexOf('id="blockCommentForm"') !== -1) {
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
			if (submitBtn) submitBtn.disabled = false;
		});
	}, true);
})();
</script>
