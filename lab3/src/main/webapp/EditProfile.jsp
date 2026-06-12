<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<div class="card card-flush">
	<c:choose>
    <c:when test="${success}">
        <div class="card">
            <p class="hint ok">Profile updated successfully.</p>
        </div>
        <script>setTimeout(function () { $('#lcolumn').load('Profile'); }, 2000);</script>
    </c:when>
    <c:otherwise>
		<div class="card-head">
			<h3>Edit profile</h3>
		</div>
		<div class="card-body">
			<form id="editProfileForm" action="EditProfile" method="POST" enctype="multipart/form-data">
				<div class="form-field">
					<label for="editUsername">Username</label>
					<input class="input" type="text" id="editUsername" name="username" maxlength="30" value="${user.username}">
				</div>

				<div class="form-field">
					<label for="editEmail">Email</label>
					<input class="input" type="email" id="editEmail" name="email" maxlength="255" value="${user.email}" readonly>
				</div>

				<div class="form-field">
					<label for="editAge">Age</label>
					<input class="input" type="number" id="editAge" name="age" min="16" value="${user.age}" readonly>
				</div>

				<div class="form-field">
					<label for="editGender">Gender</label>
					<select class="input" id="editGender" name="gender" disabled>
						<option value="">—</option>
						<option value="male"   ${user.gender == 'male'   ? 'selected' : ''}>Male</option>
						<option value="female" ${user.gender == 'female' ? 'selected' : ''}>Female</option>
						<option value="other"  ${user.gender == 'other'  ? 'selected' : ''}>Other</option>
					</select>
				</div>

				<div class="form-field">
					<label for="country">Country (optional)</label>
					<select class="input input-optional" id="country" name="country" disabled>
						<option value="">—</option>
						<c:forEach var="c" items="${countries}">
							<option value="${c}" ${user.country == c ? 'selected' : ''}>${c}</option>
						</c:forEach>
					</select>
				</div>

				<div class="form-field">
					<label for="editDescription">Description</label>
					<textarea class="input input-optional" id="editDescription" name="description" maxlength="300" style="height:100px">${user.description}</textarea>
				</div>

				<div class="form-field">
					<label for="editPicture">Profile picture</label>
					<input class="input input-optional" type="file" id="editPicture" name="profilePicture" accept="image/jpeg,image/png,image/webp,image/gif">
				</div>

				<div class="btn-row">
					<button type="submit" class="btn btn-primary">Save changes</button>
					<button type="button" class="btn btn-muted" onclick="$('#lcolumn').load('Profile')">Cancel</button>
				</div>
			</form>
		</div>
    </c:otherwise>
	</c:choose>
</div>
