<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<div class="card card-flush page-head">
    <div class="card-head">
        <h3>Timeline</h3>
    </div>
    <div class="timeline-tabs-bar">
        <a href="Timeline?mode=following"
           class="timeline-tab <c:if test="${timelineMode != 'trending'}">timeline-tab-active</c:if>"
           onclick="event.preventDefault(); window.loadContent('Timeline?mode=following');">
            <img src="assets/icons/seguir-terracota.png" alt="" class="timeline-tab-ico">
            Following
        </a>
        <a href="Timeline?mode=trending"
           class="timeline-tab <c:if test="${timelineMode == 'trending'}">timeline-tab-active</c:if>"
           onclick="event.preventDefault(); window.loadContent('Timeline?mode=trending');">
            <img src="assets/icons/trending-terracota.png" alt="" class="timeline-tab-ico">
            Trending
        </a>
    </div>
    <div class="card-body">
        <c:choose>
            <c:when test="${empty posts}">
                <c:choose>
                    <c:when test="${timelineMode == 'trending'}">
                        <p class="text-muted">No trending posts right now.</p>
                    </c:when>
                    <c:otherwise>
                        <p class="text-muted">No posts yet. Join a group or follow people to see their activity!</p>
                    </c:otherwise>
                </c:choose>
            </c:when>
            <c:otherwise>
                <c:forEach var="post" items="${posts}">
                    <%@ include file="TimelinePostCard.jsp" %>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>
</div>
