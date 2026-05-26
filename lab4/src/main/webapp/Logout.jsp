<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<script type="text/javascript">
$(document).ready(function(){
	$('#navigation').load("Menu");
	$('#rcolumn').load("NotFollowed");
	$('#lcolumn').load("Profile");
});
</script>

<div class="w3-container w3-padding-24 w3-white">
	<p class="w3-large">You have successfully logged out.</p>
	<p>Thank you for your visit. See you soon!</p>
</div>
