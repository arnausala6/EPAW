<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<script type="text/javascript">
$(document).ready(function(){
	$('#lcolumn').load('Profile');
	$('#rcolumn').html('<p/>');
});
</script>

<div class="card card-flush banned-notice-card">
	<div class="card-head panel-head">
		<h3><img src="assets/icons/block-blanco.png" alt="" class="ico"> Account banned</h3>
	</div>
	<div class="card-body banned-notice-body">
		<p class="banned-notice-title">Your account has been banned from the platform.</p>
		<p class="banned-notice-reason-label">Reason</p>
		<p class="banned-notice-reason">${banReason}</p>
		<p class="hint">You can still view your profile data, but you cannot use any other features.</p>
	</div>
</div>
