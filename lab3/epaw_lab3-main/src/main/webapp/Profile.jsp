<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<c:choose>
<c:when test="${not empty user}">
<div id="${user.id}" class="w3-container w3-card w3-round w3-white w3-section w3-center">
  <h1 class="w3-text-theme">U</h1>
  <p><img src="${user.picture}" class="w3-circle" style="height:106px;width:106px" alt="Avatar"></p>
  <hr>
  <p class="w3-left-align"> <i class="fa fa-id-card fa-fw w3-margin-right"></i> ${user.name} </p>
 </div>
<br>
</c:when>
<c:otherwise>
<p/>
</c:otherwise>
</c:choose>