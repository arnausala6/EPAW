package epaw.lab3.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import epaw.lab3.model.User;
import epaw.lab3.service.UserService;

import java.io.IOException;

/**
 * Servlet implementation class Content
 */
@WebServlet("/Content")
public class Content extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private UserService userService;

	@Override
	public void init() throws ServletException {
		userService = UserService.getInstance();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession(false);
		String view = "Login";

		if (session != null && session.getAttribute("user") != null) {
			User user = (User) session.getAttribute("user");
			if (userService.isPlatformBanned(user)) {
				String reason = userService.getPlatformBanReason(user);
				request.setAttribute("banReason",
						reason != null && !reason.isBlank() ? reason : "No reason was provided.");
				view = "BannedWelcome.jsp";
			} else {
				view = "Welcome.jsp";
			}
		}

		request.getRequestDispatcher(view).forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
