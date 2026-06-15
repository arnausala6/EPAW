<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<c:set var="isAdmin" value="${not empty sessionScope.user and sessionScope.user.role eq 'admin'}" />

<c:choose>
	<c:when test="${empty comments}">
		<p class="comment-empty">No comments yet. Be the first!</p>
	</c:when>
	<c:otherwise>
		<c:forEach var="comment" items="${comments}">
			<div class="comment-item">
				<c:if test="${(not empty currentUserId and comment.userId == currentUserId) or isAdmin}">
					<div class="comment-item-actions">
						<c:if test="${not empty currentUserId and comment.userId == currentUserId}">
							<button type="button" class="comment-action-btn btn-edit-comment"
								data-comment-id="${comment.postId}" title="Edit comment">
								<img src="assets/icons/edit-suave.png" alt="" class="ico-act">
							</button>
							<button type="button" class="comment-action-btn btn-delete-comment"
								data-comment-id="${comment.postId}" title="Delete comment">
								<img src="assets/icons/delete-error.png" alt="" class="ico-act">
							</button>
						</c:if>
						<c:if test="${isAdmin}">
							<button type="button" class="comment-action-btn btn-block-comment-admin"
								data-comment-id="${comment.postId}" title="Block comment">
								<img src="assets/icons/block-error.png" alt="" class="ico-act">
							</button>
						</c:if>
					</div>
				</c:if>
				<div class="comment-head">
					<div class="avatar avatar-photo avatar-xs<c:if test="${not empty comment.profilePicture}"> has-image</c:if>">
						<img src="${not empty comment.profilePicture ? comment.profilePicture : 'assets/icons/imagen-suave.png'}" alt="">
					</div>
					<div class="comment-head-info">
						<span class="comment-username">@${comment.username}</span>
						<span class="comment-date-row">
							<span class="comment-date">${comment.formattedDate}</span>
							<c:if test="${comment.edited}">
								<img src="assets/icons/edit-suave.png" alt="" class="comment-edited-icon" title="Edited comment">
							</c:if>
						</span>
					</div>
				</div>
				<p class="comment-body">${comment.content}</p>
			</div>
		</c:forEach>
	</c:otherwise>
</c:choose>

<div class="comment-footer">
	<button type="button" class="btn-collapse-comments" data-post-id="${postId}" title="Close comments">Close</button>
</div>
