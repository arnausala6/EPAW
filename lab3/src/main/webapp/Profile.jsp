<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<c:choose>
<c:when test="${not empty user}">
<div id="${user.id}" class="card card-flush profile">
	<div class="profile-head">
		<div class="avatar lg avatar-photo">
			<img src="${not empty user.picture ? user.picture : 'assets/default-avatar.svg'}" alt="Avatar">
		</div>
		<div class="name">@${user.username}</div>
	</div>
	<div class="profile-fields">
		<div class="profile-row">
			<span class="profile-icon"><img src="assets/icons/perfil-terracota.png" alt="" class="ico"></span>
			<div>
				<div class="profile-label">Name</div>
				<div class="profile-value">${user.username}</div>
			</div>
		</div>
		<div class="profile-row">
			<span class="profile-icon"><img src="assets/icons/mail-terracota.png" alt="" class="ico"></span>
			<div>
				<div class="profile-label">Email</div>
				<div class="profile-value">${user.email}</div>
			</div>
		</div>
		<div class="profile-row">
			<span class="profile-icon"><img src="assets/icons/calendar-terracota.png" alt="" class="ico"></span>
			<div>
				<div class="profile-label">Age</div>
				<div class="profile-value">${user.age}</div>
			</div>
		</div>
		<div class="profile-row">
			<span class="profile-icon"><img src="assets/icons/description-terracota.png" alt="" class="ico"></span>
			<div>
				<div class="profile-label">Description</div>
				<div class="profile-value">${user.description != null ? user.description : 'No descripción'}</div>
			</div>
		</div>
		<div class="profile-row">
			<span class="profile-icon"><img src="assets/icons/gender-terracota.png" alt="" class="ico"></span>
			<div>
				<div class="profile-label">Gender</div>
				<div class="profile-value">
				<c:choose>
					<c:when test="${user.gender == 'male'}">male</c:when>
					<c:when test="${user.gender == 'female'}">female</c:when>
					<c:when test="${user.gender == 'other'}">other</c:when>
					<c:otherwise>No gender</c:otherwise>
				</c:choose>
				</div>
			</div>
		</div>
		<div class="profile-row">
			<span class="profile-icon"><img src="assets/icons/country-terracota.png" alt="" class="ico"></span>
			<div>
				<div class="profile-label">Country</div>
				<div class="profile-value">${user.country != null ? user.country : 'No country'}</div>
			</div>
		</div>
	</div>
	<button type="button" class="btn btn-ghost btn-block" onclick="$('#lcolumn').load('EditProfile')">
		<img src="assets/icons/edit-suave.png" alt="" class="ico"> Edit profile
	</button>
</div>
</c:when>
<c:otherwise>
<p/>
</c:otherwise>
</c:choose>
