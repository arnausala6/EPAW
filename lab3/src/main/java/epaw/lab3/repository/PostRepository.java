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
            SELECT p.post_id, p.content, p.post_picture,
                   (SELECT COALESCE(SUM(v.type_of_vote), 0) FROM Vote v WHERE v.post_id = p.post_id) AS votes,
                   (SELECT COUNT(*) FROM Vote v WHERE v.post_id = p.post_id AND v.type_of_vote = 1) AS upvotes,
                   (SELECT COUNT(*) FROM Vote v WHERE v.post_id = p.post_id AND v.type_of_vote = -1) AS downvotes,
                   (SELECT COUNT(*) FROM Post c WHERE c.response_id = p.post_id) AS comment_count,
                   p.date_of_creation, p.user_id, p.group_id, p.response_id,
                   COALESCE(p.edited, 0) AS edited,
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

    public int publishPost(Post post) {
        String query = "INSERT INTO Post (content, user_id, group_id, response_id) VALUES (?, ?, ?, ?)";

        try (PreparedStatement statement = db.prepareStatement(query, java.sql.Statement.RETURN_GENERATED_KEYS)) {
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
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
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
            AND p.user_id != ?
            -- 🛡️ Excluir posts de usuarios bloqueados por el usuario actual
            AND p.user_id NOT IN (
                SELECT blocked_id FROM Block WHERE blocker_id = ?
            )
            AND (
                p.group_id IN (
                SELECT group_id FROM UserInGroup WHERE user_id = ?
                )
                OR (
                p.user_id IN (
                    SELECT followed_id FROM Follows WHERE follower_id = ?
                )
                AND g.privacy = 'public'
                )
            )
            ORDER BY p.date_of_creation DESC
        """;
        try (PreparedStatement stmt = db.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, userId);
            stmt.setInt(3, userId);
            stmt.setInt(4, userId);
            
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

        public List<Post> getTrendingPosts(Integer userId) {
        List<Post> posts = new ArrayList<>();
        String query = POST_SELECT + """
            FROM Post p
            JOIN User u ON p.user_id = u.user_id
            JOIN "Group" g ON p.group_id = g.group_id
            WHERE p.response_id IS NULL
              AND g.privacy = 'public'
                            AND (p.upvotes - p.downvotes) > 10
                            AND p.group_id NOT IN (
                                SELECT group_id FROM UserInGroup WHERE user_id = ?
                            )
                            AND p.user_id NOT IN (
                                SELECT followed_id FROM Follows WHERE follower_id = ?
                            )
                            AND p.user_id != ?
                        ORDER BY p.date_of_creation DESC
            LIMIT 30
        """;
        try (PreparedStatement stmt = db.prepareStatement(query)) {
                        stmt.setInt(1, userId);
                        stmt.setInt(2, userId);
                        stmt.setInt(3, userId);
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

    public List<Post> getPublicTrendingPosts() {
        List<Post> posts = new ArrayList<>();
        String query = POST_SELECT + """
            FROM Post p
            JOIN User u ON p.user_id = u.user_id
            JOIN "Group" g ON p.group_id = g.group_id
            WHERE p.response_id IS NULL
              AND g.privacy = 'public'
            ORDER BY p.date_of_creation DESC
            LIMIT 30
        """;
        try (PreparedStatement stmt = db.prepareStatement(query)) {
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


    public List<Post> findByGroupId(int groupId, int user_id) {
        List<Post> posts = new ArrayList<>();
        String query = POST_SELECT + """
            FROM Post p
            JOIN User u ON p.user_id = u.user_id
            JOIN "Group" g ON p.group_id = g.group_id
            WHERE p.group_id = ? 
            AND p.response_id IS NULL
            AND p.user_id NOT IN (
                SELECT blocked_id FROM Block WHERE blocker_id = ?
            )
            ORDER BY p.date_of_creation DESC
        """;
        try (PreparedStatement stmt = db.prepareStatement(query)) {
            stmt.setInt(1, groupId);
            stmt.setInt(2, user_id);
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
        String postPicture = rs.getString("post_picture");
        if (postPicture != null && postPicture.isBlank()) {
            postPicture = null;
        }
        p.setPostPicture(postPicture);
        p.setVotes(rs.getInt("votes"));
        p.setUpvotes(rs.getInt("upvotes"));
        p.setDownvotes(rs.getInt("downvotes"));
        p.setCommentCount(rs.getInt("comment_count"));
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
        p.setEdited(rs.getInt("edited") == 1);
        return p;
    }

    public List<Post> findCommentsByPostId(int postId) {
        List<Post> posts = new ArrayList<>();
        String query = POST_SELECT + """
            FROM Post p
            JOIN User u ON p.user_id = u.user_id
            JOIN "Group" g ON p.group_id = g.group_id
            WHERE p.response_id = ?
            ORDER BY p.date_of_creation ASC
        """;
        try (PreparedStatement stmt = db.prepareStatement(query)) {
            stmt.setInt(1, postId);
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

    public void incrementCommentCount(int postId) {
        String query = "UPDATE Post SET comment_count = comment_count + 1 WHERE post_id = ?";
        try (PreparedStatement stmt = db.prepareStatement(query)) {
            stmt.setInt(1, postId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateComment(int commentId, String content) {
        String query = "UPDATE Post SET content = ?, edited = 1 WHERE post_id = ?";
        try (PreparedStatement stmt = db.prepareStatement(query)) {
            stmt.setString(1, content);
            stmt.setInt(2, commentId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updatePostContent(int postId, String content) {
        String query = """
            UPDATE Post SET content = ?, edited = 1
            WHERE post_id = ? AND response_id IS NULL
            """;
        try (PreparedStatement stmt = db.prepareStatement(query)) {
            stmt.setString(1, content);
            stmt.setInt(2, postId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void clearPostPicture(int postId) {
        String query = """
            UPDATE Post SET post_picture = NULL, edited = 1
            WHERE post_id = ? AND response_id IS NULL
            """;
        try (PreparedStatement stmt = db.prepareStatement(query)) {
            stmt.setInt(1, postId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setPostPicture(int postId, String postPicture) {
        String query = """
            UPDATE Post SET post_picture = ?, edited = 1
            WHERE post_id = ? AND response_id IS NULL
            """;
        try (PreparedStatement stmt = db.prepareStatement(query)) {
            stmt.setString(1, postPicture);
            stmt.setInt(2, postId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean deletePost(int postId) {
        try {
            try (PreparedStatement deleteComments = db.prepareStatement(
                    "DELETE FROM Post WHERE response_id = ?")) {
                deleteComments.setInt(1, postId);
                deleteComments.executeUpdate();
            }
            try (PreparedStatement deletePost = db.prepareStatement(
                    "DELETE FROM Post WHERE post_id = ? AND response_id IS NULL")) {
                deletePost.setInt(1, postId);
                return deletePost.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteComment(int commentId) {
        String query = "DELETE FROM Post WHERE post_id = ? AND response_id IS NOT NULL";
        try (PreparedStatement stmt = db.prepareStatement(query)) {
            stmt.setInt(1, commentId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void decrementCommentCount(int postId) {
        String query = """
            UPDATE Post
            SET comment_count = CASE WHEN comment_count > 0 THEN comment_count - 1 ELSE 0 END
            WHERE post_id = ?
            """;
        try (PreparedStatement stmt = db.prepareStatement(query)) {
            stmt.setInt(1, postId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Post> findVisiblePostsByUserIdPaginated(int authorUserId, Integer viewerUserId, int limit, int offset) {
        List<Post> posts = new ArrayList<>();
        String query = POST_SELECT + """
            FROM Post p
            JOIN User u ON p.user_id = u.user_id
            JOIN "Group" g ON p.group_id = g.group_id
            WHERE p.user_id = ?
              AND p.response_id IS NULL
              AND (
                    g.privacy = 'public'
                    OR (
                        ? IS NOT NULL AND EXISTS (
                            SELECT 1
                            FROM UserInGroup uig
                            WHERE uig.user_id = ? AND uig.group_id = p.group_id
                        )
                    )
              )
            ORDER BY p.date_of_creation DESC
            LIMIT ? OFFSET ?
        """;

        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setInt(1, authorUserId);
            if (viewerUserId != null) {
                statement.setInt(2, viewerUserId);
                statement.setInt(3, viewerUserId);
            } else {
                statement.setNull(2, java.sql.Types.INTEGER);
                statement.setNull(3, java.sql.Types.INTEGER);
            }
            statement.setInt(4, limit);
            statement.setInt(5, offset);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    posts.add(mapPost(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return posts;
    }

}
