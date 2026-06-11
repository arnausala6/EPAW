package epaw.lab3.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import epaw.lab3.model.User;
import epaw.lab3.service.UserService;
import epaw.lab3.util.BannedUserGuard;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/EditProfile")
public class EditProfile extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private UserService userService;

	@Override
	public void init() throws ServletException {
		userService = UserService.getInstance();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession(false);
		if (session != null) {
			User user = (User) session.getAttribute("user");
			if (BannedUserGuard.redirectIfBanned(user, userService, request, response)) {
				return;
			}
		}

		request.setAttribute("countries", userService.getAvailableCountries());
		request.getRequestDispatcher("EditProfile.jsp").forward(request, response);
	}
}
