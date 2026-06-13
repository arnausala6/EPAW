<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<div class="card card-flush profile">
    <div class="card-head panel-head">
        <h3><img src="assets/icons/perfil-blanco.png" alt="" class="ico"> Public profile</h3>
        
        <c:if test="${not empty profileUser}">
            <div class="groups-head-actions">
                <c:if test="${canManageRelationship}">
                    <button type="button" class="btn-icon-flat" id="followIconBtn${profileUser.id}" 
                            data-following="${profileUser.following}" 
                            title="${profileUser.following ? 'Unfollow' : 'Follow'}"
                            onclick="toggleFollow('${profileUser.id}', this.getAttribute('data-following') === 'true');">
                        <img src="${profileUser.following ? 'assets/icons/unfollow-blanco.png' : 'assets/icons/seguir-blanco.png'}" alt="Follow" class="ico-act">
                    </button>
                    <button type="button" class="btn-icon-flat" title="Block user" 
                            onclick="showBlockConfirmation('${profileUser.id}', '${profileUser.username}');">
                        <img src="assets/icons/block-blanco.png" alt="Block" class="ico-act">
                    </button>
                </c:if>
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
            <div class="profile-head" style="display: flex; gap: 1rem; align-items: center;">
                <div class="avatar lg avatar-photo<c:if test="${not empty profileUser.picture}"> has-image</c:if>">
                    <img src="${not empty profileUser.picture ? profileUser.picture : 'assets/default-avatar.svg'}" alt="Avatar">
                </div>
                <div>
                    <div class="username">@${profileUser.username}</div>
                    <p class="post-meta">Viewing a public profile</p>
                </div>
            </div>

                <div class="profile-fields">
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
                    var btn = document.getElementById('followIconBtn' + userId);
                    var action = currentlyFollowing ? 'unfollow' : 'follow';

                    $.post('Follow', { userId: userId, action: action }, function(response) {
                        var following = response && response.following === true;
                        btn.setAttribute('data-following', following ? 'true' : 'false');

                        if (following) {
                            btn.title = 'Unfollow';
                            btn.querySelector('img').src = 'assets/icons/unfollow-blanco.png';
                        } else {
                            btn.title = 'Follow';
                            btn.querySelector('img').src = 'assets/icons/seguir-blanco.png';
                        }

                        btn.onclick = function () {
                            toggleFollow(userId, following);
                        };
                    });
                }

                function showBlockConfirmation(userId, username) {
                    var confirmPanel = '<div class="card card-flush">\n' +
                        '    <div class="card-head panel-head">\n' +
                        '        <h5><img src="assets/icons/block-blanco.png" alt="" class="ico"> Block user</h5>\n' +
                        '        <button type="button" class="panel-close" onclick="$(\'#rcolumn\').html(\'<p/>\')">&times;</button>\n' +
                        '    </div>\n' +
                        '    <div class="card-body">\n' +
                        '        <p class="post-meta" style="margin-bottom: 1rem;">Are you sure you want to block <span id="blockUserConfirmUsername" style="font-weight: bold;"></span>?</p>\n' +
                        '        <p class="hint" style="margin-bottom: 1rem;">You won\'t see their posts or comments, and they won\'t be able to see your public profile.</p>\n' +
                        '        <button type="button" class="btn btn-err btn-block" id="blockUserConfirmBtn"><img src="assets/icons/block-error.png" alt="" class="ico"> Confirm block</button>\n' +
                        '        <button type="button" class="btn btn-muted btn-block" style="margin-top:8px" id="blockUserConfirmCancelBtn">Cancel</button>\n' +
                        '    </div>\n' +
                        '</div>';

                    $('#rcolumn').html(confirmPanel);
                    $('#blockUserConfirmUsername').text(username);

                    $('#blockUserConfirmBtn').on('click', function() {
                        $.post('Block', { userId: userId }, function() {
                            window.goBackAjax ? window.goBackAjax() : window.history.back();
                        });
                    });

                    $('#blockUserConfirmCancelBtn').on('click', function() {
                        $('#rcolumn').html('<p/>');
                    });
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
