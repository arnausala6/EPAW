package epaw.lab3.controller;

import java.io.IOException;
import java.util.Map;

import epaw.lab3.model.User;
import epaw.lab3.service.UserService;
import epaw.lab3.util.BannedUserGuard;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

@MultipartConfig
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

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("user") == null) {
			response.sendRedirect("Login");
			return;
		}

		User sessionUser = (User) session.getAttribute("user");
		if (BannedUserGuard.redirectIfBanned(sessionUser, userService, request, response)) {
			return;
		}
		
		Part filePart = request.getPart("profilePicture");
		User updatedUser = new User();
		updatedUser.setUsername(request.getParameter("username"));
		updatedUser.setDescription(request.getParameter("description"));
		int id = sessionUser.getId();
		updatedUser.setId(id);

		Map<String, String> errors = userService.updateProfile(sessionUser, updatedUser, filePart);

		if(errors.isEmpty()) {
			// Si no hay errores, actualizamos el usuario en la sesión
			sessionUser.setUsername(updatedUser.getUsername());
			sessionUser.setDescription(updatedUser.getDescription());
			sessionUser.setPicture(updatedUser.getPicture());
			request.setAttribute("success", true);
			request.setAttribute("countries", userService.getAvailableCountries());
			request.getRequestDispatcher("EditProfile.jsp").forward(request, response);
		} else {
			// Si hay errores, los mostramos en el formulario
			request.setAttribute("errors", errors);
			request.setAttribute("countries", userService.getAvailableCountries());
			request.getRequestDispatcher("EditProfile.jsp").forward(request, response);
		}
}
}