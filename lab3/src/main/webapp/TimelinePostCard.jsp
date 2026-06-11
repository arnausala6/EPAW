<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<c:set var="currentUserId" value="${sessionScope.user.id}" />

<article class="card timeline-post-card" data-post-id="${post.postId}">
	<c:if test="${not empty currentUserId and post.userId == currentUserId and not post.blocked}">
		<div class="post-item-actions">
			<button type="button" class="post-action-btn btn-edit-post"
				data-post-id="${post.postId}" data-view="timeline" title="Edit post">
				<img src="assets/icons/edit-suave.png" alt="" class="ico-act">
			</button>
			<button type="button" class="post-action-btn btn-delete-post"
				data-post-id="${post.postId}" data-view="timeline" title="Delete post">
				<img src="assets/icons/delete-error.png" alt="" class="ico-act">
			</button>
		</div>
	</c:if>
	<div class="post-head">
		<div>
			<div class="post-group">${post.groupName}</div>
			<div class="post-meta">@${post.username}</div>
			<span class="post-meta-row">
				<span class="post-meta">${post.formattedDate}</span>
				<c:if test="${post.edited}">
					<img src="assets/icons/edit-suave.png" alt="" class="post-edited-icon" title="Edited post">
				</c:if>
			</span>
		</div>
	</div>
	<p class="post-body">${post.content}</p>
	<c:if test="${not empty post.postPicture}">
		<div class="post-image">
			<img src="${post.postPicture}" alt="">
		</div>
	</c:if>
	<c:if test="${not post.blocked}">
		<%@ include file="PostVoteBar.jsp" %>
		<div class="comment-thread" data-post-id="${post.postId}" style="display:none">
			<div class="comment-thread-inner"></div>
		</div>
	</c:if>
</article>
