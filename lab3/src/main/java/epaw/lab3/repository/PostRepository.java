package epaw.lab3.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;

import epaw.lab3.model.Post;
import epaw.lab3.model.User;

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
}
