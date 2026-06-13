<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<script type="text/javascript">
$(document).ready(function(){
	$('#lcolumn').load('Profile');
});
</script>

<div class="card">
	<p class="welcome-text">Login successful!</p>
	<p>Hello <strong>${user.username}</strong>, you can now enjoy all the features.</p>
	<p style="margin-top: 12px;">
		<a href="Settings.jsp" class="btn btn-primary menu">Settings</a>
	</p>
</div>
