package epaw.lab4.util;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.SessionCookieConfig;

/**
 * Listener que s'executa en iniciar Jetty.
 * Configura les sessions per a que siguin persistents (es guardin en una cookie
 * amb temps de vida).
 */
@WebListener
public class SessionConfigListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // Obtenim la configuració de cookies del context de l'aplicació
        SessionCookieConfig cookieConfig = sce.getServletContext().getSessionCookieConfig();

        // Establim el temps de vida de la cookie JSESSIONID a 1 hora (3600
        // segons)
        // Això fa que la cookie NO es borri en tancar el navegador.
        cookieConfig.setMaxAge(3600);

        // Per seguretat, la marquem com HttpOnly per a que no sigui accessible via JS
        cookieConfig.setHttpOnly(true);

        System.out.println("✅ [Jetty] Sesión persistente configurada correctamente (MaxAge: 1h)");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // No es necesario realizar acciones al cerrar
    }
}
