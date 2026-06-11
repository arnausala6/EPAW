package epaw.lab3.util;

import epaw.lab3.model.User;
import epaw.lab3.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public final class BannedUserGuard {

    private BannedUserGuard() {
    }

    public static boolean redirectIfBanned(User user, UserService userService,
            HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (user == null || !userService.isPlatformBanned(user)) {
            return false;
        }

        String reason = userService.getPlatformBanReason(user);
        request.setAttribute("banReason",
                reason != null && !reason.isBlank() ? reason : "No reason was provided.");
        request.getRequestDispatcher("BannedWelcome.jsp").forward(request, response);
        return true;
    }
}
