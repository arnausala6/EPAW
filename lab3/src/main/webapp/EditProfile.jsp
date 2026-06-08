<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<div class="card card-flush">
	<div class="card-head">
		<h3>Edit profile</h3>
	</div>
	<div class="card-body">
		<form>
			<div class="form-field">
				<label for="editUsername">Username</label>
				<input class="input" type="text" id="editUsername" name="username" maxlength="30" value="${user.username}">
			</div>

			<div class="form-field">
				<label for="editEmail">Email</label>
				<input class="input" type="email" id="editEmail" name="email" maxlength="255" value="${user.email}">
			</div>

			<div class="form-field">
				<label for="editAge">Age</label>
				<input class="input" type="number" id="editAge" name="age" min="16" value="${user.age}">
			</div>

			<div class="form-field">
				<label for="editGender">Gender</label>
				<select class="input" id="editGender" name="gender">
					<option value="">—</option>
					<option value="male"   ${user.gender == 'male'   ? 'selected' : ''}>Male</option>
					<option value="female" ${user.gender == 'female' ? 'selected' : ''}>Female</option>
					<option value="other"  ${user.gender == 'other'  ? 'selected' : ''}>Other</option>
				</select>
			</div>

			<div class="form-field">
				<label for="country">Country (optional)</label>
				<select class="input" id="country" name="country">
					<option value="">—</option>
					<c:forEach var="c" items="${countries}">
						<option value="${c}" ${user.country == c ? 'selected' : ''}>${c}</option>
					</c:forEach>
				</select>
			</div>

			<div class="form-field">
				<label for="editDescription">Description</label>
				<textarea class="input" id="editDescription" name="description" maxlength="300" style="height:100px">${user.description}</textarea>
			</div>

			<div class="form-field">
				<label for="editPicture">Profile picture</label>
				<input class="input" type="file" id="editPicture" name="profilePicture" accept="image/jpeg,image/png,image/webp,image/gif">
			</div>

			<div class="btn-row">
				<button type="button" class="btn btn-primary">Save changes</button>
				<button type="button" class="btn btn-muted" onclick="$('#lcolumn').load('Profile')">Cancel</button>
			</div>
			<p class="hint">Mockup only: changes will not be saved.</p>
		</form>
	</div>
</div>
