<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<div class="card card-flush page-head">
    <div class="card-head">
        <h3>Blocked users</h3>
    </div>
    <div class="card-body">
        <c:choose>
            <c:when test="${empty blockedUsers}">
                <p>You have no blocked users.</p>
            </c:when>
            <c:otherwise>
                <ul style="list-style:none; padding:0; margin:0; display:grid; gap:12px;">
                    <c:forEach var="u" items="${blockedUsers}">
                        <li style="display:flex; align-items:center; justify-content:space-between; gap:12px; padding:12px; border:1px solid #e5e7eb; border-radius:8px; background:#fff;">
                            <div>
                                <strong>${u.username}</strong>
                                <c:if test="${not empty u.email}">
                                    <div style="color:#6b7280; font-size:0.95em;">${u.email}</div>
                                </c:if>
                            </div>
                            <form action="BlockedUsers" method="post" class="form-unblock-user" style="margin:0;">
                                <input type="hidden" name="blockedId" value="${u.id}">
                                <button type="submit" class="btn btn-primary">Unblock</button>
                            </form>
                        </li>
                    </c:forEach>
                </ul>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<script>
document.querySelectorAll('.form-unblock-user').forEach(function(form) {
    form.addEventListener('submit', function(event) {
        event.preventDefault(); // Detiene cualquier envío nativo o del script global
        event.stopPropagation(); // Evita que el submit suba al documento general
        
        const formData = $(this).serialize(); // Convierte los inputs a formato texto estándar (blockedId=X)
        const targetContainer = $(this).closest('#rcolumn').length ? '#rcolumn' : '#content';

        $.ajax({
            type: 'POST',
            url: 'BlockedUsers',
            data: formData, // Enviado de forma limpia
            success: function(html) {
                // Refrescamos el contenedor donde se encuentre la vista actualmente
                $(targetContainer).html(html);
                if (typeof window.loadContent === 'function') {
                    $('#navigation').load("Menu");
                }
            },
            error: function(xhr) {
                console.error("Error al desbloquear usuario:", xhr.status, xhr.statusText);
            }
        });
    });
});
</script>