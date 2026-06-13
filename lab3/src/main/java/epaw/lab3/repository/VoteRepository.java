package epaw.lab3.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class VoteRepository extends BaseRepository {

    private static VoteRepository instance;

    public VoteRepository() {
        super();
    }

    public static synchronized VoteRepository getInstance() {
        if (instance == null) {
            instance = new VoteRepository();
        }
        return instance;
    }

    public Optional<Integer> findUserVote(int userId, int postId) {
        String query = "SELECT type_of_vote FROM Vote WHERE user_id = ? AND post_id = ?";
        try (PreparedStatement stmt = db.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, postId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(rs.getInt("type_of_vote"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Map<Integer, Integer> findUserVotesForPosts(int userId, List<Integer> postIds) {
        Map<Integer, Integer> votes = new HashMap<>();
        if (postIds == null || postIds.isEmpty()) {
            return votes;
        }
        String placeholders = String.join(",", postIds.stream().map(id -> "?").toList());
        String query = "SELECT post_id, type_of_vote FROM Vote WHERE user_id = ? AND post_id IN ("
                + placeholders + ")";
        try (PreparedStatement stmt = db.prepareStatement(query)) {
            stmt.setInt(1, userId);
            for (int i = 0; i < postIds.size(); i++) {
                stmt.setInt(i + 2, postIds.get(i));
            }
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    votes.put(rs.getInt("post_id"), rs.getInt("type_of_vote"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return votes;
    }

    public void insertVote(int userId, int postId, int voteType) {
        String query = "INSERT INTO Vote (user_id, post_id, type_of_vote) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = db.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, postId);
            stmt.setInt(3, voteType);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateVote(int userId, int postId, int voteType) {
        String query = "UPDATE Vote SET type_of_vote = ? WHERE user_id = ? AND post_id = ?";
        try (PreparedStatement stmt = db.prepareStatement(query)) {
            stmt.setInt(1, voteType);
            stmt.setInt(2, userId);
            stmt.setInt(3, postId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteVote(int userId, int postId) {
        String query = "DELETE FROM Vote WHERE user_id = ? AND post_id = ?";
        try (PreparedStatement stmt = db.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, postId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void refreshPostCounts(int postId) {
        String query = """
            UPDATE Post
            SET upvotes = (SELECT COUNT(*) FROM Vote WHERE post_id = ? AND type_of_vote = 1),
                downvotes = (SELECT COUNT(*) FROM Vote WHERE post_id = ? AND type_of_vote = -1),
                votes = (
                    SELECT COALESCE(SUM(type_of_vote), 0)
                    FROM Vote WHERE post_id = ?
                )
            WHERE post_id = ?
        """;
        try (PreparedStatement stmt = db.prepareStatement(query)) {
            stmt.setInt(1, postId);
            stmt.setInt(2, postId);
            stmt.setInt(3, postId);
            stmt.setInt(4, postId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void applyVoteDeltas(int postId, int upvotesDelta, int downvotesDelta, int votesDelta) {
        String query = """
            UPDATE Post
            SET upvotes = CASE
                    WHEN upvotes + ? < 0 THEN 0
                    ELSE upvotes + ?
                END,
                downvotes = CASE
                    WHEN downvotes + ? < 0 THEN 0
                    ELSE downvotes + ?
                END,
                votes = votes + ?
            WHERE post_id = ?
        """;
        try (PreparedStatement stmt = db.prepareStatement(query)) {
            stmt.setInt(1, upvotesDelta);
            stmt.setInt(2, upvotesDelta);
            stmt.setInt(3, downvotesDelta);
            stmt.setInt(4, downvotesDelta);
            stmt.setInt(5, votesDelta);
            stmt.setInt(6, postId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int[] getPostCounts(int postId) {
        String query = "SELECT upvotes, downvotes, votes FROM Post WHERE post_id = ?";
        try (PreparedStatement stmt = db.prepareStatement(query)) {
            stmt.setInt(1, postId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new int[] {
                            rs.getInt("upvotes"),
                            rs.getInt("downvotes"),
                            rs.getInt("votes")
                    };
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new int[] { 0, 0, 0 };
    }
}
