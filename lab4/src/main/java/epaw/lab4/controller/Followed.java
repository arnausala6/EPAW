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
import java.util.List;


/**
 * Servlet implementation class GetFollowed
 */
@WebServlet("/Followed")
public class Followed extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public Followed() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		List<User> users = null;
		
		HttpSession session = request.getSession(false);
		
		if (session != null) {
			User user = (User) session.getAttribute("user");
			if (user!= null) {
				try {
					UserService userService = UserService.getInstance();
					users = userService.getFollowedUsers(user.getId(),0,4);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		request.setAttribute("users",users);
		request.getRequestDispatcher("Followed.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
