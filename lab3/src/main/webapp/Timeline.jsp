<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<div class="card card-flush page-head">
    <div class="card-head">
        <h3>Timeline</h3>
    </div>
    <div class="card-body">
        <c:choose>
            <c:when test="${empty posts}">
                <p class="text-muted">No posts yet. Join a group and start sharing!</p>
            </c:when>
            <c:otherwise>
                <c:forEach var="post" items="${posts}">
                    <article class="card">
                        <div class="post-head">
                            <div>
                                <div class="post-group">${post.groupName}</div>
                                <div class="post-meta">@${post.username}</div>
                            </div>
                        </div>
                        <p class="post-body">${post.content}</p>
                        <div class="post-actions">
                            <button class="act">▲ ${post.votes}</button>
                            <button class="act">▼</button>
                        </div>
                    </article>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>
</div>
