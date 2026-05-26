<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<script type="text/javascript">
// Versión híbrida compatible con index.html (jQuery) e index_fetch.html (Vanilla JS)
(function() {
    function loadProfile() {
        if (window.$ && typeof $.fn.load === 'function') {
            $('#lcolumn').load('Profile');
        } else if (window.App && typeof App.loadContent === 'function') {
            App.loadContent('Profile', 'lcolumn');
        }
    }
    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', loadProfile);
    } else {
        loadProfile();
    }
})();
</script>

<div class="w3-container w3-padding-24 w3-white">
	<p class="w3-large">Login successful!</p>
	<p>Hello <strong>${user.username}</strong>, you can now enjoy all the features.</p>
</div>