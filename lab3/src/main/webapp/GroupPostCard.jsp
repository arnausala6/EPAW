<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<c:set var="isAdmin" value="${not empty sessionScope.user and sessionScope.user.role eq 'admin'}" />
<c:set var="currentUserId" value="${sessionScope.user.id}" />

<article class="card group-post-card" data-post-id="${post.postId}">
	<c:if test="${isAdmin}">
		<button type="button" class="group-post-block-btn btn-block-post-admin" data-post-id="${post.postId}" title="Block post">
			<img src="assets/icons/block-error.png" alt="" class="ico-act">
		</button>
	</c:if>
	<c:if test="${not empty currentUserId and post.userId == currentUserId}">
		<div class="post-item-actions${isAdmin ? ' post-item-actions-with-admin' : ''}">
			<button type="button" class="post-action-btn btn-edit-post"
				data-post-id="${post.postId}" data-view="group" title="Edit post">
				<img src="assets/icons/edit-suave.png" alt="" class="ico-act">
			</button>
			<button type="button" class="post-action-btn btn-delete-post"
				data-post-id="${post.postId}" data-view="group" title="Delete post">
				<img src="assets/icons/delete-error.png" alt="" class="ico-act">
			</button>
		</div>
	</c:if>
	<div class="post-head">
		<div class="avatar avatar-photo<c:if test="${not empty post.profilePicture}"> has-image</c:if>">
			<img src="${not empty post.profilePicture ? post.profilePicture : 'assets/icons/imagen-suave.png'}" alt="">
		</div>
		<div class="post-head-text">
			<div class="post-head-top">
				<div class="post-head-lines">
					<div class="post-group">@${post.username}</div>
					<span class="post-meta-row">
						<span class="post-meta">${post.formattedDate}</span>
						<c:if test="${post.edited}">
							<img src="assets/icons/edit-suave.png" alt="" class="post-edited-icon" title="Edited post">
						</c:if>
					</span>
				</div>
			</div>
		</div>
	</div>
	<p class="post-body">${post.content}</p>
	<c:if test="${not empty post.postPicture}">
		<div class="post-image">
			<img src="${post.postPicture}" alt="">
		</div>
	</c:if>
	<%@ include file="PostVoteBar.jsp" %>
	<div class="comment-thread" data-post-id="${post.postId}" style="display:none">
		<div class="comment-thread-inner"></div>
	</div>
</article>
