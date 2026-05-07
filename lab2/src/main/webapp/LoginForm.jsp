<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<!DOCTYPE html>
<html lang="ca">

<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
    <link rel="stylesheet" href="css/style.css">
    <title>Iniciar sessió</title>
</head>

<body>

    <div class="main-container">
        <div class="w3-card-4 w3-white">
            <div class="w3-container w3-teal">
                <h2>Iniciar sessió</h2>
            </div>
            <div class="w3-container w3-padding-16">
                <a href="Register" class="w3-text-teal">No tinc compte · Registrar-me</a>
            </div>
            <form id="loginForm" action="Login" method="POST" class="w3-container w3-padding-24">
                <p>
                    <label class="w3-text-grey">Username</label>
                    <input class="w3-input w3-border" type="text" name="loginUsername" required maxlength="30"
                        value="${loginUsername}" />
                </p>
                <p>
                    <label class="w3-text-grey">Password</label>
                    <input class="w3-input w3-border" type="password" name="loginPassword" required />
                </p>
                <button type="submit" class="w3-button w3-teal w3-block w3-section w3-padding">Entrar</button>
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
    <script src="js/login-validation.js"></script>

</body>

</html>
