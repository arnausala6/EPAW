<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="ca">

<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
    <link rel="stylesheet" href="css/style.css">
    <title>Sessió iniciada</title>
</head>

<body>

    <div class="main-container">
        <div class="w3-card-4 w3-white">
            <div class="w3-container w3-green">
                <h2>Sessió iniciada</h2>
            </div>
            <div class="w3-container w3-padding-24">
                <p class="w3-large">Hola <strong>@${user.username}</strong>, has entrat correctament.</p>
                <p><a href="Login" class="w3-button w3-teal">Tornar al login</a></p>
            </div>
        </div>
    </div>

</body>

</html>
