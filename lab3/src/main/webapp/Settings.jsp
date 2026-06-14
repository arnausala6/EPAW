<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%
    epaw.lab3.model.User settingsUser = (epaw.lab3.model.User) session.getAttribute("user");
    boolean isAdminUser = settingsUser != null && "admin".equalsIgnoreCase(settingsUser.getRole());
%>

<div class="card card-flush page-head">
	<div class="card-head">
		<h3>Settings</h3>
	</div>
	<div class="card-body">
		<% if (!isAdminUser) { %>
		<a href="BlockedUsers" class="btn btn-primary menu">Blocked users</a>
		<% } else { %>
		<p class="text-muted">Admins do not have personal settings.</p>
		<% } %>
	</div>
</div>
