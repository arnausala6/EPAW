<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<div class="post-actions-bar" data-post-id="${post.postId}">
	<button type="button" class="act act-up btn-vote btn-vote-up${post.userVote == 1 ? ' act-voted' : ''}"
		data-post-id="${post.postId}" data-vote-type="1" title="Upvote">
		<img src="assets/icons/upvote-terracota.png" alt="" class="ico-act">
		<span class="vote-count-up">${post.upvotes}</span>
	</button>
	<button type="button" class="act act-down btn-vote btn-vote-down${post.userVote == -1 ? ' act-voted-down' : ''}"
		data-post-id="${post.postId}" data-vote-type="-1" title="Downvote">
		<img src="assets/icons/downvote-suave.png" alt="" class="ico-act">
		<span class="vote-count-down">${post.downvotes}</span>
	</button>
	<span class="act-spacer"></span>
	<button type="button" class="act btn-toggle-comments"
		data-post-id="${post.postId}" title="Comments">
		<img src="assets/icons/comment-suave.png" alt="" class="ico-act">
		<span class="comment-count">${post.commentCount}</span>
	</button>
</div>
