<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<div class="card card-flush">
	<div class="card-head panel-head">
		<h5><img src="assets/icons/comment-blanco.png" alt="" class="ico"> Comment</h5>
		<button type="button" class="panel-close" onclick="$('#rcolumn').html('<p/>')">&times;</button>
	</div>
	<div class="card-body">
		<c:if test="${not empty postAuthor}">
			<p class="comment-panel-context">Responding to @${postAuthor}'s post</p>
		</c:if>
		<c:if test="${not empty errors.postId}">
			<p class="field-error">${errors.postId}</p>
		</c:if>

		<form id="addCommentForm" action="AddComment" method="POST">
			<input type="hidden" name="postId" value="${postId}">

			<div class="form-field">
				<textarea class="input${not empty errors.content ? ' input-error' : ''}"
					name="content" id="addCommentContent"
					style="height: 90px;" maxlength="500" required
					placeholder="Write a comment...">${content}</textarea>
				<c:if test="${not empty errors.content}">
					<p class="field-error">${errors.content}</p>
				</c:if>
			</div>

			<button type="submit" class="btn btn-primary btn-block">
				<img src="assets/icons/publicar-blanco.png" alt="" class="ico"> Comment
			</button>
		</form>
	</div>
</div>

<script>
(function() {
	const form = document.getElementById('addCommentForm');
	if (!form) return;

	form.addEventListener('submit', function(event) {
		event.preventDefault();
		event.stopImmediatePropagation();

		const postId = form.querySelector('[name="postId"]').value;
		const submitBtn = form.querySelector('[type="submit"]');
		submitBtn.disabled = true;

		$.post(form.action, $(form).serialize()).done(function(html) {
			if (html.indexOf('id="addCommentForm"') !== -1) {
				// Validation errors — reload panel with errors
				if (window.App && App.mountRcolumnPanel) {
					App.mountRcolumnPanel(html, 'addCommentForm');
				} else {
					$('#rcolumn').html(html);
				}
			} else {
				// Success — update the comment thread in #content
				const thread = $('#content').find('.comment-thread[data-post-id="' + postId + '"]');
				if (thread.length) {
					thread.find('.comment-thread-inner').html(html);
					thread.find('.comment-thread-inner').data('loaded', true);
					thread.show();
					const countSpan = $('#content').find(
						'.btn-toggle-comments[data-post-id="' + postId + '"] .comment-count');
					countSpan.text(parseInt(countSpan.text() || '0', 10) + 1);
				}
				// Keep the panel open but clear the textarea for a fresh comment
				form.querySelector('[name="content"]').value = '';
				submitBtn.disabled = false;
			}
		}).always(function() {
			submitBtn.disabled = false;
		});
	}, true);
})();
</script>
