<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<c:choose>
<c:when test=\"${not empty user}\">
<div id="${user.id}" class="w3-container w3-card w3-round w3-white w3-section w3-center">
  <h1 class=\"w3-text-theme\">${user.username}</h1>
  <p><img src="${not empty user.picture ? user.picture : 'assets/default-avatar.svg'}" class="w3-circle" style="height:150px;width:150px" alt="Avatar"></p>
  <hr>
  <p class="w3-left-align" title="Username"> <i class="fa fa-user fa-fw w3-margin-right"></i>${user.username}</p>
  <p class="w3-left-align" title="Email"> <i class="fa fa-envelope fa-fw w3-margin-right"></i>${user.email}</p>
  <p class="w3-left-align" title="Age"> <i class="fa fa-calendar fa-fw w3-margin-right"></i>${user.age}</p>
  <p class="w3-left-align" title="Description"> <i class="fa fa-info-circle fa-fw w3-margin-right"></i>${user.description != null ? user.description : 'No description'}</p>
  <p class="w3-left-align" title="Gender">
  <c:choose>
    <c:when test="${user.gender == 'male'}">
      <i class="fa fa-male fa-fw w3-margin-right"></i>Male
    </c:when>
    <c:when test="${user.gender == 'female'}">
      <i class="fa fa-female fa-fw w3-margin-right"></i>Female
    </c:when>
    <c:when test="${user.gender == 'other'}">
      <i class="fa fa-genderless fa-fw w3-margin-right"></i>Other
    </c:when>
    <c:otherwise>
      <i class="fa fa-genderless fa-fw w3-margin-right"></i>—
    </c:otherwise>
  </c:choose>
  </p>
  <c:if test="${not empty user.country}">
    <p class="w3-left-align" title="Country"> <i class="fa fa-globe fa-fw w3-margin-right"></i>${user.country}</p>
  </c:if>
  
  <p>
    <button type="button" class="w3-button w3-theme w3-block w3-margin-bottom" onclick="$('#content').load('EditProfile')">
      <i class="fa fa-pencil"></i> Edit profile
    </button>
  </p>
</div>
</c:when>
<c:otherwise>
<div class="w3-container w3-card w3-round w3-white w3-section w3-center w3-padding-16">
  <p class="w3-text-grey">No user session found.</p>
</div>
</c:otherwise>
</c:choose>