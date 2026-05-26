package epaw.lab4.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import epaw.lab4.model.Tweet;

public class TweetRepository extends BaseRepository {

    private static TweetRepository instance;

    private TweetRepository() {
        super();
    }

    public static synchronized TweetRepository getInstance() {
        if (instance == null) {
            instance = new TweetRepository();
        }
        return instance;
    }
	
	public void save(Tweet tweet) {
		String query = "INSERT INTO tweets (uid,postdatetime,content) VALUES (?,?,?)";
		try (PreparedStatement statement = db.prepareStatement(query)) {
			statement.setInt(1, tweet.getUid());
			statement.setTimestamp(2, tweet.getPostDateTime());
			statement.setString(3, tweet.getContent());
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/* Delete existing tweet */
	public void delete(Integer id, Integer uid) {
		String query = "DELETE FROM tweets WHERE id = ? AND uid=?";
		try (PreparedStatement statement = db.prepareStatement(query)) {
			statement.setInt(1, id);
			statement.setInt(2, uid);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/* Get tweets from a user given start and end */
	public Optional<List<Tweet>> findByUser(Integer uid, Integer start, Integer end) {
		List<Tweet> tweets = new ArrayList<Tweet>();
		String query = "SELECT tweets.id,tweets.uid,tweets.postdatetime,tweets.content,users.name FROM tweets INNER JOIN users ON tweets.uid = users.id where tweets.uid = ? ORDER BY tweets.postdatetime DESC LIMIT ?,? ;";
		try (PreparedStatement statement = db.prepareStatement(query)) {
			statement.setInt(1, uid);
			statement.setInt(2, start);
			statement.setInt(3, end);
			try (ResultSet rs = statement.executeQuery()) {
				while (rs.next()) {
					Tweet tweet = new Tweet();
					tweet.setId(rs.getInt("id"));
					tweet.setUid(rs.getInt("uid"));
					tweet.setPostDateTime(rs.getTimestamp("postdatetime"));
					tweet.setContent(rs.getString("content"));
					tweet.setUname(rs.getString("name"));
					tweets.add(tweet);
				}
				return Optional.of(tweets);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return Optional.empty();
	}

}

