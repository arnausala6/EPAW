<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<article class="card timeline-post-card public-profile-post-link" data-post-id="${post.postId}" data-group-id="${post.groupId}" style="margin-top: 0.8rem; cursor: pointer;">
    <div class="post-head" style="display: flex !important; align-items: center !important; gap: 12px !important; width: 100% !important;">
        <div class="avatar-link" title="Open this post in its group" style="display: block !important; width: 40px !important; height: 40px !important; flex-shrink: 0 !important;">
            <div class="avatar avatar-photo <c:if test='${not empty post.profilePicture}'>has-image</c:if>" style="width: 40px !important; height: 40px !important; margin: 0 !important; padding: 0 !important;">
                <img src="${not empty post.profilePicture ? post.profilePicture : 'assets/icons/imagen-suave.png'}"
                     alt="Avatar de ${post.username}" class="post-author-avatar"
                     style="width: 40px !important; height: 40px !important; border-radius: 50% !important; object-fit: cover !important; display: block !important;">
            </div>
        </div>

        <div style="display: flex !important; flex-direction: column !important; justify-content: center !important; gap: 2px !important; flex-grow: 1 !important;">
            <span class="post-group" style="font-weight: bold !important; line-height: 1.2 !important; margin: 0 !important; color: inherit !important; display: block !important;">${post.groupName}</span>
            <span class="post-meta-row" style="display: flex !important; align-items: center !important; gap: 6px !important; flex-wrap: wrap !important; line-height: 1.2 !important;">
                <span class="post-meta" style="margin: 0 !important;">@${post.username}</span>
                <span class="post-meta" style="color: #888 !important; margin: 0 !important;">· ${post.formattedDate}</span>
                <c:if test="${post.edited}">
                    <img src="assets/icons/edit-suave.png" alt="" class="post-edited-icon" title="Edited post" style="width: 14px !important; height: 14px !important; margin: 0 !important; display: inline-block !important;">
                </c:if>
            </span>
        </div>
    </div>

    <p class="post-body">${post.content}</p>

    <%@ include file="PostVoteBar.jsp" %>
</article>
