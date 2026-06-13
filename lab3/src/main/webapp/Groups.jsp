<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<c:set var="isAdmin" value="${not empty sessionScope.user and sessionScope.user.role eq 'admin'}" />

<div class="card card-flush page-head groups-page${not empty group and group.blocked ? ' groups-page-banned' : ''}">
	<div class="card-head panel-head">
		<c:choose>
			<c:when test="${not empty group}">
				<h3>${group.groupName}</h3>
			</c:when>
			<c:otherwise>
				<h3><img src="assets/icons/grupos-blanco.png" alt="" class="ico"> Groups</h3>
			</c:otherwise>
		</c:choose>
		<c:choose>
			<c:when test="${not empty group}">
				<div class="groups-head-actions">
					<c:choose>
					<c:when test="${group.blocked}">
						<c:if test="${isGroupMember}">
							<button type="button" class="btn-icon-flat btn-leave-group" data-group-id="${group.groupId}" title="Leave group">
								<img src="assets/icons/log-out-blanco.png" alt="" class="ico-act">
							</button>
						</c:if>
					</c:when>
					<c:otherwise>
						<c:if test="${isGroupOwner}">
							<c:if test="${group.privacy == 'private'}">
								<button type="button" class="btn-icon-flat btn-group-join-requests" data-group-id="${group.groupId}" title="Join requests">
									<img src="assets/icons/mail-terracota.png" alt="" class="ico-act ico-act-header-invert">
								</button>
							</c:if>
							<button type="button" class="btn-icon-flat btn-edit-group" data-group-id="${group.groupId}" title="Edit group">
								<img src="assets/icons/edit-blanco.png" alt="" class="ico-act">
							</button>
						</c:if>
						<c:if test="${isGroupMember and not isGroupOwner}">
							<button type="button" class="btn-icon-flat btn-leave-group" data-group-id="${group.groupId}" title="Leave group">
								<img src="assets/icons/log-out-blanco.png" alt="" class="ico-act">
							</button>
						</c:if>
						<c:if test="${not isGroupMember and not isGroupOwner and group.privacy != 'private'}">
							<button type="button" class="btn-icon-flat btn-join-group" data-group-id="${group.groupId}" title="Join group">
								<img src="assets/icons/unirse-blanco.png" alt="" class="ico-act">
							</button>
						</c:if>
						<c:if test="${not isGroupMember and not isGroupOwner and group.privacy == 'private'}">
							<c:choose>
								<c:when test="${hasPendingJoinRequest}">
									<span class="btn-icon-flat btn-icon-flat-static" title="Join request pending">
										<img src="assets/icons/clock.png" alt="" class="ico-act ico-act-header-invert">
									</span>
								</c:when>
								<c:otherwise>
									<button type="button" class="btn-icon-flat btn-request-join" data-group-id="${group.groupId}" title="Request to join">
										<img src="assets/icons/mail-terracota.png" alt="" class="ico-act ico-act-header-invert">
									</button>
								</c:otherwise>
							</c:choose>
						</c:if>
						<c:if test="${isGroupMember}">
							<button type="button" class="btn-icon-flat btn-new-post-group" data-group-id="${group.groupId}" title="New post">
								<img src="assets/icons/publicar-blanco.png" alt="" class="ico-act">
							</button>
						</c:if>
						<c:if test="${isAdmin}">
							<button type="button" class="btn-icon-flat btn-block-group-admin" data-group-id="${group.groupId}" title="Block group">
								<img src="assets/icons/block-blanco.png" alt="" class="ico-act">
							</button>
						</c:if>
					</c:otherwise>
					</c:choose>
					<button type="button" class="btn-icon-flat btn-back-generic" title="Go back">
						<img src="assets/icons/back.png" alt="" class="ico-act">
					</button>
				</div>
			</c:when>
			<c:otherwise>
				<button type="button" class="btn btn-accent btn-sm btn-create-group btn-create-group-head" title="Create group">
					<img src="assets/icons/crear-grupo-terracota.png" alt="" class="ico-act">
				</button>
			</c:otherwise>
		</c:choose>
	</div>
	<div class="card-body${not empty group and group.blocked ? ' groups-detail-banned' : ''}">
		<c:choose>
		<c:when test="${not empty group}">
			<c:if test="${not empty errors.groupId}">
				<p class="field-error">${errors.groupId}</p>
			</c:if>
			<%@ include file="GroupDetail.jsp" %>
		</c:when>
		<c:otherwise>
			<c:if test="${not empty errors.groupId}">
				<p class="field-error">${errors.groupId}</p>
			</c:if>
			<div class="groups-section">
				<div class="side-title">Your groups</div>

				<c:if test="${empty userGroups}">
					<p class="hint">You are not a member of any group yet. Create one or join a suggestion.</p>
				</c:if>

				<c:forEach var="g" items="${userGroups}">
					<article class="card group-item group-item-clickable${g.blocked ? ' group-item-banned' : ''}" data-group-id="${g.groupId}">
						<div class="group-item-content">
							<div class="group-item-photo<c:if test="${not empty g.groupPicture and g.groupPicture ne 'assets/icons/imagen-suave.png'}"> has-image</c:if>">
								<img src="${not empty g.groupPicture ? g.groupPicture : 'assets/icons/imagen-suave.png'}" alt="">
							</div>
							<div class="group-item-body">
								<h4>${g.groupName}</h4>
								<p class="group-desc">${g.description}</p>
								<div class="group-item-meta">
									<span>${g.memberCount} member${g.memberCount == 1 ? '' : 's'}</span>
									<span>·</span>
									<c:choose>
										<c:when test="${g.privacy == 'private'}">
											<img src="assets/icons/privado-terracota.png" alt="Private" class="ico-vis" title="Private">
										</c:when>
										<c:otherwise>
											<img src="assets/icons/grupos-terracota.png" alt="Public" class="ico-vis" title="Public">
										</c:otherwise>
									</c:choose>
								</div>
							</div>
						</div>
						<c:choose>
							<c:when test="${g.blocked}">
								<span class="group-item-banned-label">Banned</span>
							</c:when>
							<c:otherwise>
								<img src="assets/icons/next.png" alt="" class="group-item-enter-ico" width="30" height="30">
							</c:otherwise>
						</c:choose>
					</article>
				</c:forEach>
			</div>

			<div class="groups-section">
				<div class="side-title">Suggestions</div>

				<c:if test="${empty suggestedGroups}">
					<p class="hint">No group suggestions available right now.</p>
				</c:if>

				<c:forEach var="g" items="${suggestedGroups}">
					<article class="card group-item group-item-clickable${g.blocked ? ' group-item-banned' : ''}" data-group-id="${g.groupId}">
						<div class="group-item-content">
							<div class="group-item-photo<c:if test="${not empty g.groupPicture and g.groupPicture ne 'assets/icons/imagen-suave.png'}"> has-image</c:if>">
								<img src="${not empty g.groupPicture ? g.groupPicture : 'assets/icons/imagen-suave.png'}" alt="">
							</div>
							<div class="group-item-body">
								<h4>${g.groupName}</h4>
								<p class="group-desc">${g.description}</p>
								<div class="group-item-meta">
									<span>${g.memberCount} member${g.memberCount == 1 ? '' : 's'}</span>
									<span>·</span>
									<c:choose>
										<c:when test="${g.privacy == 'private'}">
											<img src="assets/icons/privado-terracota.png" alt="Private" class="ico-vis" title="Private">
										</c:when>
										<c:otherwise>
											<img src="assets/icons/grupos-terracota.png" alt="Public" class="ico-vis" title="Public">
										</c:otherwise>
									</c:choose>
								</div>
							</div>
						</div>
						<c:choose>
							<c:when test="${g.blocked}">
								<span class="group-item-banned-label">Banned</span>
							</c:when>
							<c:when test="${g.privacy == 'private'}">
								<c:choose>
									<c:when test="${g.pendingJoinRequest}">
										<img src="assets/icons/clock-terracota.png" alt="" class="group-item-enter-ico" width="30" height="30" title="Join request pending">
									</c:when>
									<c:otherwise>
										<img src="assets/icons/privado-terracota.png" alt="" class="group-item-enter-ico" width="30" height="30" title="Private group">
									</c:otherwise>
								</c:choose>
							</c:when>
							<c:otherwise>
								<div class="group-item-actions">
									<button type="button" class="btn-icon-flat btn-join-group" data-group-id="${g.groupId}" data-list-view="true" title="Join">
										<img src="assets/icons/unirse-terracota.png" alt="Join" class="ico-act">
									</button>
								</div>
							</c:otherwise>
						</c:choose>
					</article>
				</c:forEach>
			</div>
		</c:otherwise>
		</c:choose>
	</div>
</div>

<script>
$('#rcolumn').html('<p/>');

document.querySelectorAll('.btn-create-group').forEach(function(btn) {
	btn.addEventListener('click', function() {
		$('#rcolumn').load('CreateGroup');
	});
});

document.querySelectorAll('.group-item-clickable').forEach(function(card) {
    card.addEventListener('click', function(e) {
        if (e.target.closest('.group-item-actions')) return;
        window.loadContent('Groups?id=' + card.dataset.groupId);
    });
});

document.querySelectorAll('.btn-group-join-requests').forEach(function(btn) {
	btn.addEventListener('click', function() {
		$('#rcolumn').load('GroupJoinRequests?id=' + btn.dataset.groupId);
	});
});

document.querySelectorAll('.btn-edit-group').forEach(function(btn) {
	btn.addEventListener('click', function() {
		$('#rcolumn').load('EditGroup?id=' + btn.dataset.groupId);
	});
});

document.querySelectorAll('.btn-request-join').forEach(function(btn) {
	btn.addEventListener('click', function() {
		$.post('RequestJoinGroup', { groupId: btn.dataset.groupId }).done(function(html) {
			$('#content').html(html);
		});
	});
});

document.querySelectorAll('.btn-leave-group').forEach(function(btn) {
	btn.addEventListener('click', function() {
		$('#rcolumn').load('LeaveGroup?id=' + btn.dataset.groupId);
	});
});

document.querySelectorAll('.btn-join-group').forEach(function(btn) {
	btn.addEventListener('click', function(e) {
		e.stopPropagation();
		const data = { groupId: btn.dataset.groupId };
		if (btn.dataset.listView) {
			data.listView = 'true';
		}
		$.post('JoinGroup', data).done(function(html) {
			$('#content').html(html);
		});
	});
});

document.querySelectorAll('.btn-back-generic').forEach(function(btn) {
    btn.addEventListener('click', function() {
        window.goBackAjax ? window.goBackAjax() : window.history.back();
    });
});

document.querySelectorAll('.btn-new-post-group').forEach(function(btn) {
    btn.addEventListener('click', function() {
        window.loadContent('NewPost?groupId=' + btn.dataset.groupId);
    });
});

document.querySelectorAll('.btn-block-group-admin').forEach(function(btn) {
	btn.addEventListener('click', function() {
		$('#rcolumn').load('BlockGroup?id=' + btn.dataset.groupId);
	});
});

document.querySelectorAll('.btn-block-post-admin').forEach(function(btn) {
	btn.addEventListener('click', function() {
		$('#rcolumn').load('BlockPost?id=' + btn.dataset.postId);
	});
});

<c:if test="${not empty focusPostId}">
setTimeout(function() {
	var target = document.querySelector('.group-post-card[data-post-id="${focusPostId}"]');
	if (!target) {
		return;
	}
	target.scrollIntoView({ behavior: 'smooth', block: 'center' });
	target.style.outline = '2px solid var(--acc)';
	target.style.outlineOffset = '2px';
	setTimeout(function() {
		target.style.outline = '';
		target.style.outlineOffset = '';
	}, 1600);
}, 60);
</c:if>
</script>
