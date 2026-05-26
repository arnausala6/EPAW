package epaw.lab4.service;

import epaw.lab4.repository.TweetRepository;
import java.util.List;
import java.util.Optional;
import epaw.lab4.model.Tweet;

public class TweetService {
	
	private static TweetService instance;
	private TweetRepository tweetRepository;
	
	private TweetService() {
        this.tweetRepository = TweetRepository.getInstance();
    }
	
	public static synchronized TweetService getInstance() {
		if (instance == null) {
			instance = new TweetService();
		}
		return instance;
	}
	
	public void add(Tweet tweet) {
		tweetRepository.save(tweet);	
	}
	
	public void delete(Integer id, Integer uid) {
		tweetRepository.delete(id, uid);
	}

	public List<Tweet> getTweetsByUser(Integer uid, Integer start, Integer end) {
		Optional<List<Tweet>> tweets = tweetRepository.findByUser(uid,start,end);
    	if (tweets.isPresent())
    	    return tweets.get();
        return null;
	}

}