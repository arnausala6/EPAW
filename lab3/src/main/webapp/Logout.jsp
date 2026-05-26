<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>\r\n

<script type="text/javascript">
document.addEventListener("DOMContentLoaded", function() {
    // Código compatible tanto con Fetch API como con jQuery
    var rcol = document.getElementById('rcolumn');
    var lcol = document.getElementById('lcolumn');
    if (rcol) rcol.innerHTML = '<p/>';
    if (lcol) lcol.innerHTML = '<p/>';
});
</script>

<div class="w3-container w3-padding-24 w3-white">
	<p class="w3-large">You have successfully logged out.</p>
	<p>Thank you for your visit. See you soon!</p>
</div>