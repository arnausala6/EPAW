<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<div class="card card-flush">
	<div class="card-head panel-head">
		<h5><img src="assets/icons/block-blanco.png" alt="" class="ico"> Block post</h5>
		<button type="button" class="panel-close" onclick="$('#rcolumn').html('<p/>')">&times;</button>
	</div>
	<div class="card-body">
		<c:if test="${not empty post}">
			<p class="hint block-post-target">@${post.username}</p>
		</c:if>
		<c:if test="${not empty errors.postId}">
			<p class="field-error">${errors.postId}</p>
		</c:if>

		<form id="blockPostForm" action="BlockPost" method="POST">
			<input type="hidden" name="postId" value="${post.postId}">

			<div class="form-field">
				<label for="blockPostReason">Reason</label>
				<textarea class="input${not empty errors.reason ? ' input-error' : ''}" id="blockPostReason" name="reason"
					style="height: 80px;" maxlength="300" required
					placeholder="Explain why this post violates the rules...">${reason}</textarea>
				<c:if test="${not empty errors.reason}">
					<p class="field-error">${errors.reason}</p>
				</c:if>
			</div>

			<div class="form-field">
				<label class="block-post-ban-option">
					<input type="checkbox" name="banAuthor" value="true"${banAuthor == true ? ' checked' : ''}>
					<span>Also ban the author</span>
				</label>
			</div>

			<div class="form-field">
				<label for="blockPostPassword">Your password</label>
				<input class="input${not empty errors.password ? ' input-error' : ''}" type="password" id="blockPostPassword"
					name="password" required autocomplete="current-password">
				<c:if test="${not empty errors.password}">
					<p class="field-error">${errors.password}</p>
				</c:if>
			</div>

			<button type="submit" class="btn btn-primary btn-block">
				<img src="assets/icons/block-blanco.png" alt="" class="ico"> Confirm block
			</button>
			<p class="hint">This action is permanent and cannot be undone.</p>
		</form>
	</div>
</div>

<script>
(function() {
	const form = document.getElementById('blockPostForm');
	if (!form) return;

	form.addEventListener('submit', function(event) {
		event.preventDefault();
		event.stopImmediatePropagation();

		$.post(form.action, $(form).serialize()).done(function(html) {
			if (window.App && App.mountRcolumnPanel) {
				App.mountRcolumnPanel(html, 'blockPostForm');
			} else if (html.indexOf('id="blockPostForm"') !== -1) {
				$('#rcolumn').html(html);
			} else {
				$('#rcolumn').html('<p/>');
				$('#content').html(html);
			}
		});
	}, true);
})();
</script>
