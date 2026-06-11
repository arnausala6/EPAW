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
 * Servlet implementation class Menu
 */
@WebServlet("/Menu")
public class Menu extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserService userService;

	@Override
	public void init() throws ServletException {
		userService = UserService.getInstance();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession(false);
		String view = "MenuNotLogged.html";
		if (session != null){ // Si hay una sesión activa	
			User user = (User) session.getAttribute("user");
			if (user != null) { // Si hay un usuario en la sesión
				if (userService.isPlatformBanned(user)) {
					view = "MenuBanned.html";
				} else if (user.getRole().equals("admin")) {
					view = "MenuAdmin.html";
				} else {
					view = "MenuLogged.html";
				}
			}
		}
		request.getRequestDispatcher(view).forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
