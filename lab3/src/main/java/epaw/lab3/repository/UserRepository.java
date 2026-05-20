package epaw.lab3.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

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
        String query = "SELECT COUNT(*) FROM users WHERE name = ?";
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

    public boolean checkLogin(User user) {
        String query = "SELECT id, picture, role from users where name=? AND password=?";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getPassword());
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    user.setId(rs.getInt("id"));
                    user.setPicture(rs.getString("picture"));
                    user.setRole(rs.getString("role")); // Cargamos el rol del usuario
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void save(User user) {
        String query = "INSERT INTO users (name, password, picture, role) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getPicture());
            statement.setString(4, user.getRole());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Optional<User> findByName(String name) {
        String query = "SELECT id, name, password, picture, role FROM users WHERE name = ?";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setString(1, name);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setPassword(rs.getString("password"));
                user.setPicture(rs.getString("picture"));
                user.setRole(rs.getString("role")); // Cargamos el rol del usuario
                return Optional.of(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
