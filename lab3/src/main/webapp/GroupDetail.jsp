<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<div class="group-detail-banner">
	<div class="group-detail-layout">
		<div class="group-detail-photo<c:if test="${not empty group.groupPicture and group.groupPicture ne 'assets/icons/imagen-suave.png'}"> has-image</c:if>">
			<img src="${not empty group.groupPicture ? group.groupPicture : 'assets/icons/imagen-suave.png'}" alt="">
		</div>
		<div class="group-detail-desc-col">
			<p class="group-detail-desc">${group.description}</p>
		</div>
		<div class="group-detail-stats">
			<div class="group-detail-stat">
				<span class="group-detail-stat-icon"><img src="assets/icons/grupos-terracota.png" alt=""></span>
				<span>${group.memberCount} member${group.memberCount == 1 ? '' : 's'}</span>
			</div>
			<div class="group-detail-stat">
				<span class="group-detail-stat-icon"><img src="assets/icons/description-terracota.png" alt=""></span>
				<span>${group.postCount} post${group.postCount == 1 ? '' : 's'}</span>
			</div>
			<div class="group-detail-stat">
				<span class="group-detail-stat-icon"><img src="assets/icons/privado-terracota.png" alt=""></span>
				<span><c:choose><c:when test="${group.privacy != 'private'}">Public</c:when><c:otherwise>Private</c:otherwise></c:choose></span>
			</div>
			<div class="group-detail-stat">
				<span class="group-detail-stat-icon"><img src="assets/icons/perfil-terracota.png" alt=""></span>
				<span>@${group.owner}</span>
			</div>
			<c:if test="${not empty group.dateOfCreation}">
				<div class="group-detail-stat">
					<span class="group-detail-stat-icon"><img src="assets/icons/calendar-terracota.png" alt=""></span>
					<span>${group.dateOfCreation.dayOfMonth}/${group.dateOfCreation.monthValue}/${group.dateOfCreation.year}</span>
				</div>
			</c:if>
			<div class="group-detail-stat">
				<span class="group-detail-stat-icon"><img src="assets/icons/country-terracota.png" alt=""></span>
				<span>${not empty group.ownerCountry ? group.ownerCountry : '—'}</span>
			</div>
		</div>
	</div>
</div>

<c:set var="canViewPosts" value="${isAdmin or group.privacy != 'private' or isGroupMember}" />

<div class="group-post-feed">
	<c:choose>
	<c:when test="${canViewPosts}">
	<c:if test="${empty posts}">
		<p class="hint">No posts in this group yet.</p>
	</c:if>

	<c:forEach var="post" items="${posts}">
		<%@ include file="GroupPostCard.jsp" %>
	</c:forEach>
	</c:when>
	<c:otherwise>
		<div class="group-private-notice">
			<c:choose>
				<c:when test="${hasPendingJoinRequest}">
					<img src="assets/icons/clock-terracota.png" alt="" class="group-private-notice-icon">
					<p>Your join request is pending. The group owner must accept it before you can view posts.</p>
				</c:when>
				<c:otherwise>
					<img src="assets/icons/privado-terracota.png" alt="" class="group-private-notice-icon">
					<p>This group is private. Request to join using the mail icon above to view its posts once accepted.</p>
				</c:otherwise>
			</c:choose>
		</div>
	</c:otherwise>
	</c:choose>
</div>
