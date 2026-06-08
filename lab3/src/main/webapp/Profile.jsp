<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<c:choose>
<c:when test="${not empty user}">
<div id="${user.id}" class="w3-container w3-card w3-round w3-white w3-section w3-center">
  <h1 class="w3-text-theme">${user.username}</h1>
  <p><img src="${not empty user.picture ? user.picture : 'assets/default-avatar.svg'}" class="w3-circle" style="height:150px;width:150px" alt="Avatar"></p>
  <hr>
  <p class="w3-left-align" title="Name"> <img src="assets/icons/perfil-terracota.png" alt="" class="ico ico-fw">${user.username}</p>
  <p class="w3-left-align" title="Email"> <img src="assets/icons/mail-terracota.png" alt="" class="ico ico-fw">${user.email}</p>
  <p class="w3-left-align" title="Age"> <img src="assets/icons/calendar-terracota.png" alt="" class="ico ico-fw">${user.age}</p>
  <p class="w3-left-align" title="Description"> <img src="assets/icons/description-terracota.png" alt="" class="ico ico-fw">${user.description != null ? user.description : 'No descripción'}</p>
  <p class="w3-left-align" title="Gender">
  <c:choose>
    <c:when test="${user.gender == 'male'}">
      <img src="assets/icons/gender-terracota.png" alt="" class="ico ico-fw">male
    </c:when>
    <c:when test="${user.gender == 'female'}">
      <img src="assets/icons/gender-terracota.png" alt="" class="ico ico-fw">female
    </c:when>
    <c:when test="${user.gender == 'other'}">
      <img src="assets/icons/gender-terracota.png" alt="" class="ico ico-fw">other
    </c:when>
    <c:otherwise>
      <img src="assets/icons/gender-terracota.png" alt="" class="ico ico-fw">No gender
    </c:otherwise>
  </c:choose>
</p>
<p class="w3-left-align" title="Country"> <img src="assets/icons/country-terracota.png" alt="" class="ico ico-fw">${user.country != null ? user.country : 'No country'}</p>
<hr>
<p class="w3-center" style="margin-bottom:16px"><button class="w3-button w3-theme" onclick="$('#lcolumn').load('EditProfile')"><img src="assets/icons/edit-blanco.png" alt="" class="ico ico-fw">Edit profile</button></p>
</div>
<br>
</c:when>
<c:otherwise>
<p/>
</c:otherwise>
</c:choose>
