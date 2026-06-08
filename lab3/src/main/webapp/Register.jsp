<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<div class="w3-card" title="Register">
    <header class="w3-container w3-theme">
        <h1><img src="assets/icons/sign-in-blanco.png" alt="" class="ico ico-fw"> Register</h1>
    </header>
    <div class="w3-container w3-padding">
        <form id="registerForm" action="Register" method="POST" enctype="multipart/form-data">

            <div class="w3-section">
                <label for="username" class="w3-text-grey">Username</label>
                <input class="w3-input w3-border" type="text" id="username" name="username" required
                    maxlength="30" value="${user.username}" />
            </div>

            <div class="w3-section">
                <label for="email" class="w3-text-grey">Email</label>
                <input class="w3-input w3-border" type="email" id="email" name="email" required maxlength="255"
                    value="${user.email}" />
            </div>

            <div class="w3-section">
                <label for="password" class="w3-text-grey">Password</label>
                <span class="w3-small w3-text-grey" style="display:block;margin-bottom:6px;">Requisits mínims: 8 caràcters, una majúscula i un número.</span>
                <input class="w3-input w3-border" type="password" id="password" name="password" required
                    pattern="^(?=.*[A-Z])(?=.*[0-9]).{8,}$" />
            </div>

            <div class="w3-section">
                <label for="confirmPassword" class="w3-text-grey">Repeat password</label>
                <input class="w3-input w3-border" type="password" id="confirmPassword" name="confirmPassword"
                    required />
            </div>

            <div class="w3-section">
                <label for="age" class="w3-text-grey">Age</label>
                <input class="w3-input w3-border" type="number" id="age" name="age" required min="16"
                    value="${user.age}" />
            </div>

            <div class="w3-section">
                <label for="gender" class="w3-text-grey">Gender</label>
                <select class="w3-input w3-border" id="gender" name="gender" required>
                    <option value="" ${empty user.gender ? 'selected' : ''}>—</option>
                    <option value="male" ${user.gender == 'male' ? 'selected' : ''}>Male</option>
                    <option value="female" ${user.gender == 'female' ? 'selected' : ''}>Female</option>
                    <option value="other" ${user.gender == 'other' ? 'selected' : ''}>Other</option>
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
                <label for="description" class="w3-text-grey">Description (optional)</label>
                <textarea class="w3-input w3-border" id="description" name="description" maxlength="300"
                    style="height: 100px;">${user.description}</textarea>
            </div>

            <div class="w3-section">
                <label for="profilePicture" class="w3-text-grey">Profile picture (optional, max 2MB)</label>
                <div class="w3-input w3-border profile-file-row">
                    <input type="file" id="profilePicture" name="profilePicture" class="profile-file-native"
                        accept="image/jpeg,image/png,image/webp,image/gif" />
                    <button type="button" id="btnRemoveProfile" class="profile-file-remove"
                        style="display: none;">Eliminar imatge</button>
                </div>
            </div>

            <div class="w3-section">
                <label for="groupId" class="w3-text-grey">Group to join (optional)</label>
                <select class="w3-input w3-border" id="groupId" name="groupId">
                    <option value="">—</option>
                    <c:forEach var="g" items="${groups}">
                        <option value="${g.groupId}" ${user.groupId == g.groupId ? 'selected' : ''}>
                            ${g.groupName}
                        </option>
                    </c:forEach>
                </select>
            </div>

            <button type="submit" class="w3-button w3-theme w3-section">Submit Registration</button>

        </form>
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
