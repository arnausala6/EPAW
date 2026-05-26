package epaw.lab4.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import epaw.lab4.model.User;
import epaw.lab4.service.UserService;

import java.io.IOException;

/**
 * Servlet implementation class Unfollow
 */
@WebServlet("/Unfollow")
public class Unfollow extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public Unfollow() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession(false);
		
		if (session != null) {
			User user = (User) session.getAttribute("user");
			if (user!= null) {
				try {
					UserService userService = UserService.getInstance();
					userService.unfollow(user.getId(), Integer.parseInt(request.getParameter("id")));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
