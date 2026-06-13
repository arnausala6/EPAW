<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<c:set var="currentUserId" value="${sessionScope.user.id}" />
<c:set var="readOnlyPublic" value="${requestScope.readOnlyPublic}" />

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
    
	<div class="post-head" style="display: flex !important; align-items: center !important; gap: 12px !important; width: 100% !important;">
        
        <a href="PublicProfile?userId=${post.userId}" class="menu avatar-link" title="Ver el perfil de @${post.username}" style="display: block !important; width: 40px !important; height: 40px !important; flex-shrink: 0 !important;">
            <div class="avatar avatar-photo <c:if test='${not empty post.profilePicture}'>has-image</c:if>' style="width: 40px !important; height: 40px !important; margin: 0 !important; padding: 0 !important;">
                <img src="${not empty post.profilePicture ? post.profilePicture : 'assets/icons/imagen-suave.png'}" 
                     alt="Avatar de ${post.username}" class="post-author-avatar" 
                     style="width: 40px !important; height: 40px !important; border-radius: 50% !important; object-fit: cover !important; display: block !important;">
            </div>
        </a>
        
        <div style="display: flex !important; flex-direction: column !important; justify-content: center !important; gap: 2px !important; flex-grow: 1 !important;">
            <c:choose>
                <c:when test="${readOnlyPublic}">
                    <span class="post-group" style="font-weight: bold !important; line-height: 1.2 !important; margin: 0 !important; color: inherit !important; display: block !important;">${post.groupName}</span>
                </c:when>
                <c:otherwise>
                    <a href="Groups?id=${post.groupId}" class="menu post-group" style="font-weight: bold !important; line-height: 1.2 !important; margin: 0 !important; text-decoration: none !important; color: inherit !important; display: block !important;">${post.groupName}</a>
                </c:otherwise>
            </c:choose>
            
            <span class="post-meta-row" style="display: flex !important; align-items: center !important; gap: 6px !important; flex-wrap: wrap !important; line-height: 1.2 !important;">
                <a href="PublicProfile?userId=${post.userId}" class="menu post-meta" style="text-decoration: none !important; margin: 0 !important;">
                    @${post.username}
                </a>
                <span class="post-meta" style="color: #888 !important; margin: 0 !important;">· ${post.formattedDate}</span>
                <c:if test="${post.edited}">
                    <img src="assets/icons/edit-suave.png" alt="" class="post-edited-icon" title="Edited post" style="width: 14px !important; height: 14px !important; margin: 0 !important; display: inline-block !important;">
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
        <c:if test="${not readOnlyPublic}">
            <div class="comment-thread" data-post-id="${post.postId}" style="display:none">
                <div class="comment-thread-inner"></div>
            </div>
        </c:if>
    </c:if>
</article>
