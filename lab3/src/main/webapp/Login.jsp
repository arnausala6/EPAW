<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<form id="loginForm" action="Login" method="POST">

	<div>
		<label for="name" class="w3-text-theme">Name:</label> 
		<input type="text" class="w3-input w3-border w3-light-grey" 
		    id="name" name="name" required maxlength="30" value="${user.name}"
			title="Username must have less than 30 characters" />
	</div>
	<div>
		<label for="password" class="w3-text-theme">Password:</label> 
		<input type="password" class="w3-input w3-border w3-light-grey" 
			id="password" name="password" required
			pattern="^(?=.*[A-Z])(?=.*[0-9]).{8,}$"
			value="${user.password}"
			title="Requisits mínims: 8 caràcters, una majúscula i un número." />
	</div>

	<button type="submit" class="w3-button w3-theme w3-section"> Log in</button>

</form>


<script>
	App.Errors = {
	  <c:forEach var="error" items="${errors}">
	    "${error.key}": "${error.value}",
	  </c:forEach>
	};
	App.initLoginValidation(App.Errors);	
</script>