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
				<textarea class="input" id="postBody" name="content" style="height: 140px;" placeholder="Write something for the community..."></textarea>
			</div>
			
			<!-- campo: groupId (select con los grupos del usuario - de momento pon una opción fija) -->
			<div class="form-field">
				<label for="postGroup">Group</label>
				<select class="input" id="postGroup" name="groupId">
					<c:forEach var="g" items="${groups}">
						<option value="${g.groupId}">${g.groupName}</option>
					</c:forEach>
				</select>
			</div>

			<div class="btn-row">
				<button type="submit" class="btn btn-primary">
					<img src="assets/icons/publicar-blanco.png" alt="" class="ico"> Publish post
				</button>
			</div>
		</form>
	</div>
</div>
