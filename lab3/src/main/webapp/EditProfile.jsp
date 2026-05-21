<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<div class="w3-card w3-white">
	<header class="w3-container w3-theme">
		<h3>Edit profile</h3>
	</header>

	<div class="w3-container w3-padding">
		<form>
			<div class="w3-section">
				<label for="editUsername" class="w3-text-grey">Username</label>
				<input class="w3-input w3-border" type="text" id="editUsername" name="username" maxlength="30" value="${user.username}">
			</div>

			<div class="w3-section">
				<label for="editName" class="w3-text-grey">Name</label>
				<input class="w3-input w3-border" type="text" id="editName" name="name" maxlength="30" value="${user.name}">
			</div>

			<div class="w3-section">
				<label for="editEmail" class="w3-text-grey">Email</label>
				<input class="w3-input w3-border" type="email" id="editEmail" name="email" maxlength="255" value="${user.email}">
			</div>

			<div class="w3-section">
				<label for="editAge" class="w3-text-grey">Age</label>
				<input class="w3-input w3-border" type="number" id="editAge" name="age" min="16" value="${user.age}">
			</div>

			<div class="w3-section">
				<label for="editGender" class="w3-text-grey">Gender</label>
				<select class="w3-input w3-border" id="editGender" name="gender">
					<option value="">—</option>
					<option value="male"   ${user.gender == 'male'   ? 'selected' : ''}>Male</option>
					<option value="female" ${user.gender == 'female' ? 'selected' : ''}>Female</option>
					<option value="other"  ${user.gender == 'other'  ? 'selected' : ''}>Other</option>
				</select>
			</div>

			<div class="w3-section">
				<label for="country" class="w3-text-grey">Country (optional)</label>
				<select class="w3-input w3-border" id="country" name="country">
					<option value="">—</option>
					<c:forEach var="c" items="${countries}">
						<option value="${c}" ${user.country == c ? 'selected' : ''}>${c}</option>
					</c:forEach>
				</select>
			</div>

			<div class="w3-section">
				<label for="editDescription" class="w3-text-grey">Description</label>
				<textarea class="w3-input w3-border" id="editDescription" name="description" maxlength="300" style="height:100px">${user.description}</textarea>
			</div>

			<div class="w3-section">
				<label for="editPicture" class="w3-text-grey">Profile picture</label>
				<input class="w3-input w3-border" type="file" id="editPicture" name="profilePicture" accept="image/jpeg,image/png,image/webp,image/gif">
			</div>
			<div>
				<button type="button" class="w3-button w3-theme">Save changes</button>
				<button type="button" class="w3-button w3-light-grey w3-margin-left" onclick="$('#lcolumn').load('Profile')">Cancel</button>
			</div>
			<span class="w3-small w3-text-grey">Mockup only: changes will not be saved.</span>
		</form>
	</div>
</div>
