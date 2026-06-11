package epaw.lab3.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import epaw.lab3.model.Post;
import epaw.lab3.model.User;

import java.util.List;
import java.util.ArrayList;
import java.sql.Timestamp;

public class PostRepository extends BaseRepository {
    private static PostRepository instance;

    private static final String POST_SELECT = """
            SELECT p.post_id, p.content, p.post_picture, p.votes, p.upvotes, p.downvotes,
                   p.comment_count, p.blocked, p.block_reason, p.author_banned,
                   p.date_of_creation, p.user_id, p.group_id, p.response_id,
                   u.username, u.profile_picture, g.group_name
        """;

    public PostRepository() {
        super();
    }

    public static synchronized PostRepository getInstance() {
        if (instance == null) {
            instance = new PostRepository();
        }
        return instance;
    }

    public void publishPost(Post post) {
        String query = "INSERT INTO Post (content, user_id, group_id, response_id) VALUES (?, ?, ?, ?)";

        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setString(1, post.getContent());
            statement.setInt(2, post.getUserId());
            statement.setInt(3, post.getGroupId());
            if(post.getResponseId() != null){
                statement.setInt(4, post.getResponseId());
            }
            else{
                statement.setNull(4, java.sql.Types.INTEGER);
            }
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean postExistsById(Integer id){
        String query = "SELECT post_id FROM Post WHERE post_id = ?";
        try(PreparedStatement statement = db.prepareStatement(query)){
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if(rs.next()){
                return true;
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public List<Post> getTimelineByUserId(Integer userId) {
        List<Post> posts = new ArrayList<>();
        String query = POST_SELECT + """
            FROM Post p
            JOIN User u ON p.user_id = u.user_id
            JOIN "Group" g ON p.group_id = g.group_id
            WHERE p.response_id IS NULL
              AND p.group_id IN (
                SELECT group_id FROM UserInGroup WHERE user_id = ?
            )
            ORDER BY p.date_of_creation DESC
        """;
        try (PreparedStatement stmt = db.prepareStatement(query)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    posts.add(mapPost(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return posts;
    }

    public Optional<Post> findById(int postId) {
        String query = POST_SELECT + """
            FROM Post p
            JOIN User u ON p.user_id = u.user_id
            JOIN "Group" g ON p.group_id = g.group_id
            WHERE p.post_id = ?
        """;
        try (PreparedStatement stmt = db.prepareStatement(query)) {
            stmt.setInt(1, postId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapPost(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public void blockPost(int postId, String reason, boolean authorBanned) {
        String query = """
            UPDATE Post
            SET blocked = 1, block_reason = ?, author_banned = ?
            WHERE post_id = ?
        """;
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setString(1, reason);
            statement.setInt(2, authorBanned ? 1 : 0);
            statement.setInt(3, postId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Post> findByGroupId(int groupId) {
        List<Post> posts = new ArrayList<>();
        String query = POST_SELECT + """
            FROM Post p
            JOIN User u ON p.user_id = u.user_id
            JOIN "Group" g ON p.group_id = g.group_id
            WHERE p.group_id = ? AND p.response_id IS NULL
            ORDER BY p.date_of_creation DESC
        """;
        try (PreparedStatement stmt = db.prepareStatement(query)) {
            stmt.setInt(1, groupId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    posts.add(mapPost(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return posts;
    }

    private Post mapPost(ResultSet rs) throws SQLException {
        Post p = new Post();
        p.setPostId(rs.getInt("post_id"));
        p.setContent(rs.getString("content"));
        p.setPostPicture(rs.getString("post_picture"));
        p.setVotes(rs.getInt("votes"));
        p.setUpvotes(rs.getInt("upvotes"));
        p.setDownvotes(rs.getInt("downvotes"));
        p.setCommentCount(rs.getInt("comment_count"));
        p.setBlocked(rs.getInt("blocked") == 1);
        p.setBlockReason(rs.getString("block_reason"));
        p.setAuthorBanned(rs.getInt("author_banned") == 1);
        p.setUserId(rs.getInt("user_id"));
        p.setGroupId(rs.getInt("group_id"));
        p.setUsername(rs.getString("username"));
        p.setProfilePicture(rs.getString("profile_picture"));
        p.setGroupName(rs.getString("group_name"));
        Timestamp ts = rs.getTimestamp("date_of_creation");
        if (ts != null) {
            p.setDateOfCreation(ts.toLocalDateTime());
        }
        int responseId = rs.getInt("response_id");
        if (!rs.wasNull()) {
            p.setResponseId(responseId);
        }
        return p;
    }

}
