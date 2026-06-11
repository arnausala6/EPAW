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
                    <%@ include file="TimelinePostCard.jsp" %>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>
</div>
