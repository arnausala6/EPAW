<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<div class="card card-flush page-head">
	<div class="card-head panel-head">
		<h3><img src="assets/icons/grupos-blanco.png" alt="" class="ico"> Groups</h3>
		<button type="button" class="btn btn-accent btn-sm btn-create-group">
			<img src="assets/icons/publicar-blanco.png" alt="" class="ico"> Create group
		</button>
	</div>
	<div class="card-body">
		<div class="groups-section">
			<div class="side-title">Your groups</div>

			<c:if test="${empty userGroups}">
				<p class="hint">You are not a member of any group yet. Create one or join a suggestion.</p>
			</c:if>

			<c:forEach var="g" items="${userGroups}">
				<article class="card group-item">
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
					<div class="group-item-actions">
						<button type="button" class="btn-icon-flat" title="Leave">
							<img src="assets/icons/log-out-suave.png" alt="Leave" class="ico-act">
						</button>
					</div>
				</article>
			</c:forEach>
		</div>

		<div class="groups-section">
			<div class="side-title">Suggestions</div>

			<c:if test="${empty suggestedGroups}">
				<p class="hint">No group suggestions available right now.</p>
			</c:if>

			<c:forEach var="g" items="${suggestedGroups}">
				<article class="card group-item">
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
					<div class="group-item-actions">
						<button type="button" class="btn-icon-flat" title="Join">
							<img src="assets/icons/unirse-terracota.png" alt="Join" class="ico-act">
						</button>
					</div>
				</article>
			</c:forEach>
		</div>
	</div>
</div>

<script>
$('#rcolumn').html('<p/>');
document.querySelector('.btn-create-group').addEventListener('click', function() {
	$('#rcolumn').load('CreateGroup');
});
</script>
