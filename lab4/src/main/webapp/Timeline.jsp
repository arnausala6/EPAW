<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<script type="text/javascript">
$(document).ready(function(){
	$('#rcolumn').load('NotFollowed');
	$('#lcolumn').load('Profile');
	$('#iterator').load('Tweets');
});
</script>

<div class="w3-container w3-card w3-round w3-white w3-section">
	<h6 class="w3-opacity"> ${user.name}, what are you thinking? </h6>
	<p id="tweetContent" contenteditable="true" class="w3-border w3-padding"> </p>
	<button id="addTweet" type="button" class="w3-button w3-theme w3-section"><i class="fa fa-pencil"></i> &nbsp;Post</button> 
</div>
 
<div id="iterator">
<!-- Tweets will be loaded here -->
</div>




