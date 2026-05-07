<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<c:if test="${countries == null}">
    <jsp:forward page="/Register" />
</c:if>
<!DOCTYPE html>
<html lang="ca">

<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
    <link rel="stylesheet" href="css/style.css">
    <title>Registre</title>
</head>

<body data-ctx="${pageContext.request.contextPath}">

    <div class="main-container">
        <div class="w3-card-4 w3-white">
            <div class="w3-container w3-teal">
                <h2>Registre</h2>
            </div>

            <div class="w3-container w3-padding-16">
                <a href="Login" class="w3-text-teal">Ja tinc compte · Iniciar sessió</a>
            </div>

            <form id="registerForm" action="Register" method="POST" enctype="multipart/form-data"
                class="w3-container w3-padding-24">

                <p>
                    <label class="w3-text-grey">Username</label>
                    <input class="w3-input w3-border" type="text" id="username" name="username" required
                        maxlength="30" pattern="^[A-Za-z0-9_.-]{1,30}$" value="${user.username}" />
                </p>

                <p>
                    <label class="w3-text-grey">Name</label>
                    <input class="w3-input w3-border" type="text" id="name" name="name" required maxlength="30"
                        value="${user.name}" />
                </p>

                <p>
                    <label class="w3-text-grey">Email</label>
                    <input class="w3-input w3-border" type="email" id="email" name="email" required maxlength="255"
                        value="${user.email}" />
                </p>

                <p>
                    <label class="w3-text-grey">Password</label>
                    <span class="w3-small w3-text-grey" style="display:block;margin-bottom:6px;">Requisits mínims: 8 caràcters, una majúscula i un número.</span>
                    <input class="w3-input w3-border" type="password" id="password" name="password" required
                        pattern="^(?=.*[A-Z])(?=.*[0-9]).{8,}$" />
                </p>

                <p>
                    <label class="w3-text-grey">Repeat password</label>
                    <input class="w3-input w3-border" type="password" id="confirmPassword" name="confirmPassword"
                        required />
                </p>

                <p>
                    <label class="w3-text-grey">Age</label>
                    <input class="w3-input w3-border" type="number" id="age" name="age" required min="16"
                        value="${user.age}" />
                </p>

                <p>
                    <label class="w3-text-grey">Gender</label>
                    <select class="w3-input w3-border" id="gender" name="gender" required>
                        <option value="" ${empty user.gender ? 'selected' : ''}>—</option>
                        <option value="male" ${user.gender == 'male' ? 'selected' : ''}>Male</option>
                        <option value="female" ${user.gender == 'female' ? 'selected' : ''}>Female</option>
                        <option value="other" ${user.gender == 'other' ? 'selected' : ''}>Other</option>
                    </select>
                </p>

                <p>
                    <label class="w3-text-grey">Country (optional)</label>
                    <select class="w3-input w3-border" id="country" name="country">
                        <option value="">—</option>
                        <c:forEach var="c" items="${countries}">
                            <option value="${c}" ${user.country == c ? 'selected' : ''}>${c}</option>
                        </c:forEach>
                    </select>
                </p>

                <p>
                    <label class="w3-text-grey">Description (optional)</label>
                    <textarea class="w3-input w3-border" id="description" name="description" maxlength="300"
                        style="height: 100px;">${user.description}</textarea>
                </p>

                <p>
                    <label class="w3-text-grey">Profile picture (optional, max 2MB)</label>
                    <div class="w3-input w3-border profile-file-row">
                        <input type="file" id="profilePicture" name="profilePicture" class="profile-file-native"
                            accept="image/jpeg,image/png,image/webp,image/gif" />
                        <span id="profileFileHint" class="profile-file-hint">Cap arxiu seleccionat</span>
                        <button type="button" id="btnRemoveProfile" class="profile-file-remove"
                            style="display: none;">Eliminar imatge</button>
                    </div>
                    <input type="hidden" name="removeProfilePicture" id="removeProfilePicture" value="false" />
                    <c:if test="${not empty user.profilePicturePath}">
                        <input type="hidden" name="savedProfilePicturePath" id="savedProfilePicturePath"
                            value="<c:out value='${user.profilePicturePath}' />" />
                    </c:if>
                </p>
                <c:if test="${not empty user.profilePicturePath}">
                    <div id="previewContainer" class="preview-container"
                        data-saved-path="<c:out value='${user.profilePicturePath}' />">
                        <img id="profilePreviewImg" class="profile-preview" alt=""
                            src="${pageContext.request.contextPath}/${user.profilePicturePath}" />
                    </div>
                </c:if>
                <c:if test="${empty user.profilePicturePath}">
                    <div id="previewContainer" class="preview-container"
                        data-saved-path=""
                        style="display: none;">
                        <img id="profilePreviewImg" class="profile-preview" alt="" src="" />
                    </div>
                </c:if>

                <p>
                    <label class="w3-text-grey">Group to join (optional)</label>
                    <select class="w3-input w3-border" id="groupId" name="groupId">
                        <option value="">—</option>
                        <c:forEach var="g" items="${groups}">
                            <option value="${g.id}" ${user.groupId == g.id ? 'selected' : ''}>${g.name}</option>
                        </c:forEach>
                    </select>
                </p>

                <button type="submit" class="w3-button w3-teal w3-block w3-section w3-padding">Registrar-me</button>

            </form>
        </div>
    </div>

    <script id="server-errors-data" type="application/json">
        {
            <c:forEach var="error" items="${errors}" varStatus="status">
                "${error.propertyPath}": "${error.message}"<c:if test="${!status.last}">,</c:if>
            </c:forEach>
        }
    </script>
    <script>
        const serverErrors = JSON.parse(document.getElementById("server-errors-data").textContent);
    </script>
    <script src="js/validation.js"></script>

</body>

</html>
