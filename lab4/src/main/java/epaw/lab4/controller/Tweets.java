package epaw.lab4.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import epaw.lab4.model.Tweet;
import epaw.lab4.model.User;
import epaw.lab4.service.TweetService;

import java.io.IOException;
import java.util.List;

@WebServlet("/Tweets")
public class Tweets extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public Tweets() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		List<Tweet> tweets = null;
		User user = null;
		HttpSession session = request.getSession(false);
		
		if (session != null) {
			user = (User) session.getAttribute("user");
			if (user != null) {
				TweetService tweetService = TweetService.getInstance();
				tweets = tweetService.getTweetsByUser(user.getId(),0,4);
			}
		}

		request.setAttribute("tweets",tweets);
		request.setAttribute("user",user);
		request.getRequestDispatcher("Tweets.jsp").forward(request, response);

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
