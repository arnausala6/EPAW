<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:choose>
<c:when test="${empty users}">
<p> </p>
</c:when>
<c:otherwise>
<c:forEach var="u" items="${users}">      
 <div id="${u.id}" class="w3-container w3-card w3-section w3-white w3-round w3-animate-opacity"><br>
   <img src="${u.picture}" alt="Avatar" class="w3-left w3-circle w3-margin-right" style="width:60px">
   <h4> ${u.name} </h4><br>
   <hr class="w3-clear">
   <button type="button" class="userInfo w3-button w3-theme w3-margin-bottom"><i class="fa fa-info-circle"></i> &nbsp;Info</button>
   <button type="button" class="unfollowUser w3-button w3-red w3-margin-bottom"><i class="fa fa-trash"></i> &nbsp;Unfollow</button> 
 </div>
</c:forEach>
</c:otherwise>
</c:choose>