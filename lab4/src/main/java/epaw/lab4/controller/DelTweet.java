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

import org.apache.commons.beanutils.BeanUtils;

@WebServlet("/DelTweet")
public class DelTweet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public DelTweet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);

		if (session != null) {
			User user = (User) session.getAttribute("user");
			if (user != null) {
				try {
					Tweet tweet = new Tweet();
					BeanUtils.populate(tweet, request.getParameterMap());
					TweetService tweetService = TweetService.getInstance();
					tweetService.delete(tweet.getId(),user.getId());
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
