package epaw.lab3.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import epaw.lab3.service.UserService;

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

		request.setAttribute("countries", userService.getAvailableCountries());
		request.getRequestDispatcher("EditProfile.jsp").forward(request, response);
	}
}
