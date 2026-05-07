package epaw.lab2.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import epaw.lab2.model.ValidationError;
import epaw.lab2.service.UserService;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@WebServlet("/Login")
public class Login extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private UserService userService;

	@Override
	public void init() throws ServletException {
		userService = UserService.getInstance();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setAttribute("errors", Collections.emptyList());
		request.getRequestDispatcher("LoginForm.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String username = request.getParameter("loginUsername");
		String password = request.getParameter("loginPassword");

		List<ValidationError> errors = userService.login(username, password);
		if (errors.isEmpty()) {
			userService.findByUsername(username).ifPresent(u -> request.setAttribute("user", u));
			request.getRequestDispatcher("LoginSuccess.jsp").forward(request, response);
		} else {
			request.setAttribute("errors", errors);
			request.setAttribute("loginUsername", username);
			request.getRequestDispatcher("LoginForm.jsp").forward(request, response);
		}
	}
}
