<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<div class="card card-flush page-head">
	<div class="card-head">
		<h3><img src="assets/icons/publicar-blanco.png" alt="" class="ico"> New post</h3>
	</div>
	<div class="card-body">
		<form action="NewPost" method="POST">
			<div class="form-field">
				<label for="postBody">Content</label>
				<textarea class="input${not empty errors.content ? ' input-error' : ''}" id="postBody" name="content"
					style="height: 140px;" placeholder="Write something for the community...">${content}</textarea>
				<c:if test="${not empty errors.content}">
					<p class="field-error">${errors.content}</p>
				</c:if>
			</div>

			<div class="form-field">
				<label for="postGroup">Group</label>
				<c:choose>
					<c:when test="${empty groups}">
						<p class="hint">Join a group before publishing a post.</p>
					</c:when>
					<c:otherwise>
						<select class="input${not empty errors.groupId ? ' input-error' : ''}" id="postGroup" name="groupId" required>
							<c:forEach var="g" items="${groups}">
								<option value="${g.groupId}"${selectedGroupId == g.groupId ? ' selected' : ''}>${g.groupName}</option>
							</c:forEach>
						</select>
					</c:otherwise>
				</c:choose>
				<c:if test="${not empty errors.groupId}">
					<p class="field-error">${errors.groupId}</p>
				</c:if>
			</div>

			<div class="btn-row">
				<button type="submit" class="btn btn-primary"${empty groups ? ' disabled' : ''}>
					<img src="assets/icons/publicar-blanco.png" alt="" class="ico"> Publish post
				</button>
			</div>
		</form>
	</div>
</div>
