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


    // Método para obtener el timeline de un usuario
    public List<Post> getTimelineByUserId(Integer userId) {
        List<Post> posts = new ArrayList<>();
        String query = """
            SELECT p.post_id, p.content, p.votes, p.date_of_creation,
                p.user_id, p.group_id, u.username, g.group_name
            FROM Post p
            JOIN User u ON p.user_id = u.user_id
            JOIN "Group" g ON p.group_id = g.group_id
            WHERE p.group_id IN (
                SELECT group_id FROM UserInGroup WHERE user_id = ?
            )
            ORDER BY p.date_of_creation DESC
        """;
        try (PreparedStatement stmt = db.prepareStatement(query)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Post p = new Post();
                    p.setPostId(rs.getInt("post_id"));
                    p.setContent(rs.getString("content"));
                    p.setVotes(rs.getInt("votes"));
                    p.setUserId(rs.getInt("user_id"));
                    p.setGroupId(rs.getInt("group_id"));
                    p.setUsername(rs.getString("username"));
                    p.setGroupName(rs.getString("group_name"));
                    Timestamp ts = rs.getTimestamp("date_of_creation");
                    if (ts != null) p.setDateOfCreation(ts.toLocalDateTime());
                    posts.add(p);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return posts;
    }

}
