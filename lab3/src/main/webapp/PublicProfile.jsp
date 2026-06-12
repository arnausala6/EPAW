<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<div class="card card-flush profile">
    <div class="card-head panel-head">
        <h3><img src="assets/icons/perfil-terracota.png" alt="" class="ico"> Public profile</h3>
        
        <c:if test="${not empty profileUser}">
            <div class="groups-head-actions">
                <button type="button" class="btn-icon-flat" title="Go back" 
                        onclick="window.goBackAjax ? window.goBackAjax() : window.history.back();">
                    <img src="assets/icons/back.png" alt="Back" class="ico-act">
                </button>
            </div>
        </c:if>
    </div>
    <div class="card-body">
        <c:choose>
            <c:when test="${not empty profileUser}">
                <div class="profile-head">
                    <div class="avatar lg avatar-photo<c:if test="${not empty profileUser.picture}"> has-image</c:if>">
                        <img src="${not empty profileUser.picture ? profileUser.picture : 'assets/default-avatar.svg'}" alt="Avatar">
                    </div>
                    <div>
                        <div class="name">@${profileUser.username}</div>
                        <p class="post-meta">Viewing a public profile</p>
                    </div>
                </div>

                <div class="profile-fields">
                    <div class="profile-row">
                        <span class="profile-icon"><img src="assets/icons/perfil-terracota.png" alt="" class="ico"></span>
                        <div>
                            <div class="profile-label">Name</div>
                            <div class="profile-value">${profileUser.username}</div>
                        </div>
                    </div>
                    <div class="profile-row">
                        <span class="profile-icon"><img src="assets/icons/mail-terracota.png" alt="" class="ico"></span>
                        <div>
                            <div class="profile-label">Email</div>
                            <div class="profile-value">${profileUser.email}</div>
                        </div>
                    </div>
                    <div class="profile-row">
                        <span class="profile-icon"><img src="assets/icons/calendar-terracota.png" alt="" class="ico"></span>
                        <div>
                            <div class="profile-label">Age</div>
                            <div class="profile-value">${profileUser.age}</div>
                        </div>
                    </div>
                    <div class="profile-row">
                        <span class="profile-icon"><img src="assets/icons/description-terracota.png" alt="" class="ico"></span>
                        <div>
                            <div class="profile-label">Description</div>
                            <div class="profile-value">${not empty profileUser.description ? profileUser.description : 'No description yet.'}</div>
                        </div>
                    </div>
                    <div class="profile-row">
                        <span class="profile-icon"><img src="assets/icons/gender-terracota.png" alt="" class="ico"></span>
                        <div>
                            <div class="profile-label">Gender</div>
                            <div class="profile-value">
                                <c:choose>
                                    <c:when test="${profileUser.gender == 'male'}">male</c:when>
                                    <c:when test="${profileUser.gender == 'female'}">female</c:when>
                                    <c:when test="${profileUser.gender == 'other'}">other</c:when>
                                    <c:otherwise>No gender</c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>
                    <div class="profile-row">
                        <span class="profile-icon"><img src="assets/icons/country-terracota.png" alt="" class="ico"></span>
                        <div>
                            <div class="profile-label">Country</div>
                            <div class="profile-value">${not empty profileUser.country ? profileUser.country : 'No country'}</div>
                        </div>
                    </div>
                </div>
            </c:when>
            <c:otherwise>
                <article class="card">
                    <p class="post-body">The selected profile could not be loaded.</p>
                </article>
            </c:otherwise>
        </c:choose>
    </div>
</div>
