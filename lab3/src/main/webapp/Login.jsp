<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<div class="auth">
	<div class="card card-flush" title="Log In">
		<div class="card-head">
			<h1><img src="assets/icons/log-in-blanco.png" alt="" class="ico"> Log In</h1>
		</div>
		<div class="card-body">
			<form id="loginForm" action="Login" method="POST">
				<div class="form-field">
					<label for="username">Username:</label>
					<input type="text" class="input"
						id="username" name="username" required maxlength="30" value="${user.username}"
						title="Enter your username" />
				</div>
				<div class="form-field">
					<label for="password">Password:</label>
					<input type="password" class="input"
						id="password" name="password" required
						pattern="^(?=.*[A-Z])(?=.*[0-9]).{8,}$"
						value="${user.password}"
						title="Requisits mínims: 8 caràcters, una majúscula i un número." />
				</div>
				<button type="submit" class="btn btn-primary btn-block">Log in</button>
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
	App.initLoginValidation(App.Errors);
</script>
