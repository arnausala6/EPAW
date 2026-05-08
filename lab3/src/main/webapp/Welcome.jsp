<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<script type="text/javascript">
$(document).ready(function(){
	$('#lcolumn').load('Profile');
});
</script>

<div class="w3-container w3-padding-24 w3-white">
	<p class="w3-large">Login successful!</p>
	<p>Hello <strong>${user.name}</strong>, you can now enjoy all the features.</p>
</div>
