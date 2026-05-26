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
        String query = "SELECT user_id, username, email, encrypted_password, register_date, gender, description, profile_picture, country, role FROM User WHERE username = ?";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setString(1, user.getUsername());
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    String storedHash = rs.getString("encrypted_password");
                    if (BCrypt.checkpw(user.getConfirmPassword(), storedHash)) {
                        
                        user.setUserId(rs.getInt("user_id"));
                        user.setEmail(rs.getString("email"));
                        user.setRegisterDate(rs.getTimestamp("register_date"));
                        user.setGender(rs.getString("gender")); // Añadido
                        user.setDescription(rs.getString("description"));
                        user.setCountry(rs.getString("country"));
                        user.setProfilePicture(rs.getString("profile_picture"));
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
        String query = "INSERT INTO User (username, email, encrypted_password, gender, description, profile_picture, country, role) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getEncryptedPassword());
            statement.setString(4, user.getGender()); // Añadido
            statement.setString(5, user.getDescription());
            statement.setString(6, user.getProfilePicture());
            statement.setString(7, user.getCountry());
            statement.setString(8, user.getRole());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Optional<User> findByUsername(String username) {
        String query = "SELECT user_id, username, email, encrypted_password, register_date, gender, description, profile_picture, country, role FROM User WHERE username = ?";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setString(1, username);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setRegisterDate(rs.getTimestamp("register_date"));
                user.setGender(rs.getString("gender")); // Añadido
                user.setDescription(rs.getString("description"));
                user.setCountry(rs.getString("country"));
                user.setEncryptedPassword(rs.getString("encrypted_password"));
                user.setProfilePicture(rs.getString("profile_picture"));
                user.setRole(rs.getString("role"));
                return Optional.of(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }