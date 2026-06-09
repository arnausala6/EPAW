<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<div class="auth">
	<div class="card card-flush" title="Register">
		<div class="card-head">
			<h1><img src="assets/icons/sign-in-blanco.png" alt="" class="ico"> Register</h1>
		</div>
		<div class="card-body">
			<form id="registerForm" action="Register" method="POST" enctype="multipart/form-data">

				<div class="form-field">
					<label for="username">Username</label>
					<input class="input" type="text" id="username" name="username" required
						maxlength="30" value="${user.username}" />
				</div>

				<div class="form-field">
					<label for="email">Email</label>
					<input class="input" type="email" id="email" name="email" required maxlength="255"
						value="${user.email}" />
				</div>

				<div class="form-field">
					<label for="password">Password</label>
					<p class="hint">Requisits mínims: 8 caràcters, una majúscula i un número.</p>
					<input class="input" type="password" id="password" name="password" required
						pattern="^(?=.*[A-Z])(?=.*[0-9]).{8,}$" />
				</div>

				<div class="form-field">
					<label for="confirmPassword">Repeat password</label>
					<input class="input" type="password" id="confirmPassword" name="confirmPassword" required />
				</div>

				<div class="form-field">
					<label for="age">Age</label>
					<input class="input" type="number" id="age" name="age" required min="16" value="${user.age}" />
				</div>

				<div class="form-field">
					<label for="gender">Gender</label>
					<select class="input" id="gender" name="gender" required>
						<option value="" ${empty user.gender ? 'selected' : ''}>—</option>
						<option value="male" ${user.gender == 'male' ? 'selected' : ''}>Male</option>
						<option value="female" ${user.gender == 'female' ? 'selected' : ''}>Female</option>
						<option value="other" ${user.gender == 'other' ? 'selected' : ''}>Other</option>
					</select>
				</div>

				<div class="form-field">
					<label for="country">Country (optional)</label>
					<select class="input input-optional" id="country" name="country">
						<option value="">—</option>
						<c:forEach var="c" items="${countries}">
							<option value="${c}" ${user.country == c ? 'selected' : ''}>${c}</option>
						</c:forEach>
					</select>
				</div>

				<div class="form-field">
					<label for="description">Description (optional)</label>
					<textarea class="input input-optional" id="description" name="description" maxlength="300"
						style="height: 100px;">${user.description}</textarea>
				</div>

				<div class="form-field">
					<label for="profilePicture">Profile picture (optional, max 2MB)</label>
					<div class="profile-file-row">
						<input type="file" id="profilePicture" name="profilePicture" class="profile-file-native input input-optional"
							accept="image/jpeg,image/png,image/webp,image/gif" />
						<button type="button" id="btnRemoveProfile" class="profile-file-remove"
							style="display: none;">Eliminar imatge</button>
					</div>
				</div>

				<div class="form-field">
					<label for="groupId">Group to join (optional)</label>
					<select class="input input-optional" id="groupId" name="groupId">
						<option value="">—</option>
						<c:forEach var="g" items="${groups}">
							<option value="${g.groupId}" ${user.groupId == g.groupId ? 'selected' : ''}>
								${g.groupName}
							</option>
						</c:forEach>
					</select>
				</div>

				<button type="submit" class="btn btn-primary btn-block">Submit Registration</button>

			</form>
		</div>
	</div>
</div>

<script>
	App.Errors = {
	  <c:forEach var="error" items="${errors}">
	    "${error.key}": "${error.value}",
	  </c:forEach>
	};
	App.initRegisterValidation(App.Errors);
</script>
