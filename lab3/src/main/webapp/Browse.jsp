<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<div class="card card-flush page-head">
    <div class="card-head">
        <h3><img src="assets/icons/browse-blanco.png" alt="" class="ico"> Public content</h3>
    </div>
    <div class="timeline-tabs-bar">
        <a href="Browse?mode=timeline"
           class="timeline-tab <c:if test="${browseMode != 'search'}">timeline-tab-active</c:if>"
           onclick="event.preventDefault(); window.loadContent('Browse?mode=timeline');">
            <img src="assets/icons/marketing.png" alt="" class="timeline-tab-ico">
            Timeline
        </a>
        <a href="Browse?mode=search"
           class="timeline-tab <c:if test="${browseMode == 'search'}">timeline-tab-active</c:if>"
           onclick="event.preventDefault(); window.loadContent('Browse?mode=search');">
            <img src="assets/icons/perfil-terracota.png" alt="" class="timeline-tab-ico">
            Search
        </a>
    </div>
    <div class="card-body">
        <div class="anonymous-browse-hint" aria-live="polite">
            <img src="assets/icons/perfil-terracota.png" alt="" class="ico">
            <span>You are browsing as an anonymous user. Log in to follow, vote, comment or create posts.</span>
        </div>

        <c:choose>
            <c:when test="${browseMode == 'search'}">
                <div class="search-row">
                    <input id="browseSearch" class="input" type="text" value="${query}"
                        placeholder="Search by username, email or description"
                        onkeydown="if (event.key === 'Enter') { event.preventDefault(); searchBrowseUsers(); }">
                    <button type="button" class="btn btn-primary" onclick="return searchBrowseUsers();">
                        <img src="assets/icons/buscar-blanco.png" alt="" class="ico"> Search
                    </button>
                </div>

                <p class="post-meta">
                    <c:choose>
                        <c:when test="${hasQuery}">
                            Showing public profiles for "${query}". Log in if you want to follow someone.
                        </c:when>
                        <c:otherwise>
                            Search public profiles. You can view profiles now, but following requires login.
                        </c:otherwise>
                    </c:choose>
                </p>

                <c:choose>
                    <c:when test="${empty users}">
                        <article class="card">
                            <p class="post-body">No public profiles found.</p>
                        </article>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="user" items="${users}">
                            <article class="card">
                                <h4>@${user.username}</h4>
                                <p class="post-body">
                                    <c:choose>
                                        <c:when test="${not empty user.description}">${user.description}</c:when>
                                        <c:otherwise>No description yet.</c:otherwise>
                                    </c:choose>
                                </p>
                                <div class="btn-row">
                                    <button type="button" class="btn btn-muted btn-sm"
                                        onclick="window.loadContent('PublicProfile?userId=${user.id}');">
                                        <i class="fa fa-id-card-o ico-missing"></i> View public profile
                                    </button>
                                </div>
                            </article>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </c:when>
            <c:otherwise>
                <p class="post-meta">Trending public posts from the community. Interactions are disabled until you log in.</p>
                <c:set var="readOnlyPublic" value="true" scope="request" />
                <c:choose>
                    <c:when test="${empty posts}">
                        <article class="card">
                            <p class="post-body">No public posts available right now.</p>
                        </article>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="post" items="${posts}">
                            <%@ include file="TimelinePostCard.jsp" %>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<script>
    function searchBrowseUsers() {
        var q = document.getElementById('browseSearch').value;
        window.loadContent('Browse?mode=search&q=' + encodeURIComponent(q));
        return false;
    }
</script>