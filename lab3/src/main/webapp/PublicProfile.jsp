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
            <div class="profile-head" style="display: flex; justify-content: space-between; align-items: center; width: 100%;">
                <div style="display: flex; gap: 1rem; align-items: center;">
                    <div class="avatar lg avatar-photo<c:if test="${not empty profileUser.picture}"> has-image</c:if>">
                        <img src="${not empty profileUser.picture ? profileUser.picture : 'assets/default-avatar.svg'}" alt="Avatar">
                    </div>
                    <div>
                        <div class="name">@${profileUser.username}</div>
                        <p class="post-meta">Viewing a public profile</p>
                    </div>
                </div>

                <div>
                    <button type="button" id="followBtn${profileUser.id}" 
                            class="btn ${profileUser.following ? 'btn-muted' : 'btn-primary'} btn-sm" 
                            data-following="${profileUser.following}" 
                            onclick="toggleFollow('${profileUser.id}', this.getAttribute('data-following') === 'true');">
                        <img src="${profileUser.following ? 'assets/icons/unirse-terracota.png' : 'assets/icons/seguir-blanco.png'}" alt="" class="ico"> 
                        <span>${profileUser.following ? 'Following' : 'Follow'}</span>
                    </button>
                    <button type="button" class="btn btn-muted btn-sm" style="color: #d9534f;"
                            onclick="executeBlock('${profileUser.id}');">
                        <i class="fa fa-ban ico"></i> Block
                    </button>
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
                        <span class="profile-icon"><img src="assets/icons/description-terracota.png" alt="" class="ico"></span>
                        <div>
                            <div class="profile-label">Description</div>
                            <div class="profile-value">${not empty profileUser.description ? profileUser.description : 'No description yet.'}</div>
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

                    <hr class="separator" style="margin: 1.5rem 0; border: 0; border-top: 1px solid var(--border-color, #eee);">

                    <div id="userPostsContainer"></div>

                    <div id="loadMoreContainer" style="text-align: center; margin-top: 1rem;">
                        <button type="button" id="btnLoadPosts" class="btn btn-muted btn-block" data-page="0" onclick="loadUserPosts('${profileUser.id}')">
                            Load posts
                        </button>
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

<script>
        function toggleFollow(userId, currentlyFollowing) {
            var btn = document.getElementById('followBtn' + userId);
            var action = currentlyFollowing ? 'unfollow' : 'follow';

            $.post('Follow', { userId: userId, action: action }, function(response) {
                var following = response && response.following === true;
                btn.setAttribute('data-following', following ? 'true' : 'false');

                if (following) {
                    btn.className = "btn btn-muted btn-sm";
                    btn.innerHTML = '<img src="assets/icons/unirse-terracota.png" alt="" class="ico"> Following';
                } else {
                    btn.className = "btn btn-primary btn-sm";
                    btn.innerHTML = '<img src="assets/icons/seguir-blanco.png" alt="" class="ico"> Follow';
                }

                btn.onclick = function () {
                    toggleFollow(userId, following);
                };
            });
        }

        function executeBlock(userId) {
            if (confirm("Are you sure you want to block this user?")) {
                $.post('Block', { userId: userId }, function() {

                    window.goBackAjax ? window.goBackAjax() : window.history.back();
                });
            }
        }

        function loadUserPosts(userId) {
            var btn = document.getElementById('btnLoadPosts');
            var container = document.getElementById('userPostsContainer');
            var currentPage = parseInt(btn.getAttribute('data-page'));

            btn.innerText = "Loading...";
            btn.disabled = true;

            $.get('GetUserPosts', { userId: userId, page: currentPage }, function(htmlResponse) {
                var trimmedResponse = htmlResponse.trim();

                if (trimmedResponse === "" || trimmedResponse.includes("NO_MORE_POSTS")) {
                    document.getElementById('loadMoreContainer').innerHTML = '<p class="post-meta" style="text-align:center; margin-top:1rem;">No more tweets</p>';
                } else {
                    $(container).append(trimmedResponse);
                    btn.setAttribute('data-page', currentPage + 1);
                    btn.innerText = "Load more posts";
                    btn.disabled = false;
                }
            });
        }
    </script>
