<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<div class="w3-card" title="Log In">
	<header class="w3-container w3-theme">
        <h1><img src="assets/icons/log-in-blanco.png" alt="" class="ico ico-fw"> Log In</h1>
    </header>
	<div class="w3-container w3-padding">
		<form id="loginForm" action="Login" method="POST">

			<div class="w3-margin-bottom">
				<label for="username" class="w3-text-grey">Username:</label> 
				<input type="text" class="w3-input w3-border" 
					id="username" name="username" required maxlength="30" value="${user.username}"
					title="Enter your username" />
			</div>
			<div>
				<label for="password" class="w3-text-grey">Password:</label> 
				<input type="password" class="w3-input w3-border" 
					id="password" name="password" required
					pattern="^(?=.*[A-Z])(?=.*[0-9]).{8,}$"
					value="${user.password}"
					title="Requisits mínims: 8 caràcters, una majúscula i un número." />
			</div>

			<button type="submit" class="w3-button w3-theme w3-section"> Log in</button>

		</form>
	</div>
</div>


<script>
	App.Errors = {
	  <c:forEach var="error" items="${errors}">
	    "${error.key}": "${error.value}",
	  </c:forEach>
	};
	App.initLoginValidation(App.Errors);	
</script>