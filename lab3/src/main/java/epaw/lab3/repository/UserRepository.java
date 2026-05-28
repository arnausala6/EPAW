package epaw.lab3.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;

import epaw.lab3.model.User;

public class UserRepository extends BaseRepository {

    private static UserRepository instance;

    private UserRepository() {
        super();
    }

    public static synchronized UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository();
        }
        return instance;
    }

    public boolean existsByUsername(String username) {
        String query = "SELECT COUNT(*) FROM User WHERE username = ?";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setString(1, username);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

        public boolean existsEmail(String email) {
        String query = "SELECT COUNT(*) FROM User WHERE email = ?";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setString(1, email);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean checkLogin(User user) {
        String query = "SELECT user_id, username, email, age, gender, description, country, profile_picture, role, password FROM User WHERE username = ?";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setString(1, user.getUsername());
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    String storedHash = rs.getString("password");
                    if (BCrypt.checkpw(user.getPassword(), storedHash)) {
                        
                        user.setId(rs.getInt("user_id"));
                        user.setUsername(rs.getString("username"));
                        user.setEmail(rs.getString("email"));
                        user.setAge(rs.getInt("age"));
                        user.setGender(rs.getString("gender"));
                        user.setDescription(rs.getString("description"));
                        user.setCountry(rs.getString("country"));
                        user.setPicture(rs.getString("profile_picture"));
                        user.setRole(rs.getString("role"));
                        return true; 
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void save(User user) {
        String query = "INSERT INTO User (username, email, password, description, profile_picture, country, age, gender) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        String queryGroup = "INSERT INTO UserInGroup (user_id, group_id) VALUES (?, ?)";
        try (PreparedStatement statement = db.prepareStatement(query, java.sql.Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.setString(4, user.getDescription());
            statement.setString(5, user.getPicture());
            statement.setString(6, user.getCountry());
            statement.setObject(7, user.getAge());
            statement.setString(8, user.getGender());
            statement.executeUpdate();

            if (user.getGroupId() != null && user.getGroupId() != 0) {
                int generatedUserId = 0;
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        generatedUserId = generatedKeys.getInt(1);
                    }
                }
                try (PreparedStatement stmtGroup = db.prepareStatement(queryGroup)) {
                    stmtGroup.setInt(1, generatedUserId); // El ID recién creado
                    stmtGroup.setInt(2, user.getGroupId()); // El grupo que venía del desplegable
                    stmtGroup.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Optional<User> findByName(String name) {
        String query = "SELECT user_id, username, email, age, gender, description, country, password, profile_picture, role FROM User WHERE name = ?";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setString(1, name);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setAge(rs.getInt("age"));
                user.setGender(rs.getString("gender"));
                user.setDescription(rs.getString("description"));
                user.setCountry(rs.getString("country"));
                user.setPassword(rs.getString("password"));
                user.setPicture(rs.getString("picture"));
                user.setRole(rs.getString("role"));
                return Optional.of(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
