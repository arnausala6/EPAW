package epaw.lab3.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import epaw.lab3.model.User;
import epaw.lab3.model.Group;

public class UserRepository extends BaseRepository {

    private static UserRepository instance;

    public UserRepository() {
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

    public boolean existsById(Integer userId) {
        String query = "SELECT COUNT(*) FROM User WHERE user_id = ?";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setInt(1, userId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean verifyPassword(int userId, String password) {
        String query = "SELECT password FROM User WHERE user_id = ?";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setInt(1, userId);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    String storedPassword = rs.getString("password");
                    return storedPassword != null && storedPassword.equals(password);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isUserBlocked(int blockerId, int blockedId) {
        String query = "SELECT 1 FROM Block WHERE blocker_id = ? AND blocked_id = ?";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setInt(1, blockerId);
            statement.setInt(2, blockedId);
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next();
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
                    String storedPassword = rs.getString("password");
                    if (user.getPassword().equals(storedPassword)) {
                        
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

    public Optional<User> findById(int userId) {
        String query = "SELECT user_id, username, email, age, gender, description, country, profile_picture, role FROM User WHERE user_id = ?";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setInt(1, userId);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("user_id"));
                    user.setUsername(rs.getString("username"));
                    user.setEmail(rs.getString("email"));
                    user.setAge(rs.getInt("age"));
                    user.setGender(rs.getString("gender"));
                    user.setDescription(rs.getString("description"));
                    user.setCountry(rs.getString("country"));
                    user.setPicture(rs.getString("profile_picture"));
                    user.setRole(rs.getString("role"));
                    return Optional.of(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
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

    public boolean checkUserInGroup(Integer user_id, Integer group_id){
        String query = "SELECT 1 FROM UserInGroup WHERE user_id = ? AND group_id = ?";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setInt(1, user_id);
            statement.setInt(2, group_id);
            
            // 3. Uso de try-with-resources para el ResultSet
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void saveBlock(Integer blockerId, Integer blockedId, String reason) {
        String query = "INSERT INTO Block(blocker_id, blocked_id, reason) VALUES (?, ?, ?)";
        String del_query = "DELETE FROM Follows WHERE follower_id = ? AND followed_id = ?";
        try (PreparedStatement statement = db.prepareStatement(query)){
            statement.setObject(1, blockerId);
            statement.setObject(2, blockedId);
            statement.setString(3, reason);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //Si el servidor se apaga aqui hay un desync pero no se como hacerlo bien
        try (PreparedStatement statement = db.prepareStatement(del_query)){
            statement.setObject(1, blockerId);
            statement.setObject(2, blockedId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isPlatformBanned(int userId) {
        String query = """
            SELECT 1 FROM Block b
            INNER JOIN User u ON b.blocker_id = u.user_id
            WHERE b.blocked_id = ? AND u.role = 'admin'
            LIMIT 1
        """;
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setInt(1, userId);
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String findPlatformBanReason(int userId) {
        String query = """
            SELECT b.reason FROM Block b
            INNER JOIN User u ON b.blocker_id = u.user_id
            WHERE b.blocked_id = ? AND u.role = 'admin'
            LIMIT 1
        """;
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setInt(1, userId);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("reason");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void saveUnblock(Integer blockerId, Integer blockedId){
        String query = "DELETE FROM Block WHERE blocker_id = ? AND blocked_id = ?";
        try (PreparedStatement statement = db.prepareStatement(query)){
            statement.setObject(1, blockerId);
            statement.setObject(2, blockedId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveFollow(Integer followerId, Integer followedId){
        String query = "INSERT OR IGNORE INTO Follows(follower_id, followed_id) VALUES (?, ?)";
        try (PreparedStatement statement = db.prepareStatement(query)){
            statement.setObject(1, followerId);
            statement.setObject(2, followedId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveUnfollow(Integer followerId, Integer followedId){
        String query = "DELETE FROM Follows WHERE follower_id = ? AND followed_id = ?";
        try (PreparedStatement statement = db.prepareStatement(query)){
            statement.setObject(1, followerId);
            statement.setObject(2, followedId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveUserJoiningGroup(Integer userId, Integer groupId){
        String query = "INSERT INTO UserInGroup(user_id, group_id) VALUES (?, ?)";
        try (PreparedStatement statement = db.prepareStatement(query)){
            statement.setObject(1, userId);
            statement.setObject(2, groupId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveUserLeavingGroup(Integer userId, Integer groupId){
        String query = "DELETE FROM UserInGroup WHERE user_id = ? AND group_id = ?";
        try (PreparedStatement statement = db.prepareStatement(query)){
            statement.setObject(1, userId);
            statement.setObject(2, groupId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<User> findRecommendedUsers(Integer currentUserId, String searchTerm) {
        List<User> users = new ArrayList<>();
        StringBuilder query = new StringBuilder("""
            SELECT user_id, username, email, description, profile_picture
            FROM User
            WHERE user_id <> ?
        """);

        String normalized = searchTerm == null ? "" : searchTerm.trim();
        if (!normalized.isEmpty()) {
            String likeTerm = "%" + normalized.toLowerCase() + "%";
            query.append("\n              AND (LOWER(username) LIKE ? OR LOWER(description) LIKE ?)");
        }

        query.append("\nORDER BY username\nLIMIT 30");

        try (PreparedStatement statement = db.prepareStatement(query.toString())) {
            statement.setInt(1, currentUserId != null ? currentUserId : -1);

            if (!normalized.isEmpty()) {
                String likeTerm = "%" + normalized.toLowerCase() + "%";
                statement.setString(2, likeTerm);
                statement.setString(3, likeTerm);
            }

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("user_id"));
                    user.setUsername(rs.getString("username"));
                    user.setEmail(rs.getString("email"));
                    user.setDescription(rs.getString("description"));
                    user.setProfilePicturePath(rs.getString("profile_picture"));
                    users.add(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    public List<User> findUsersByGroupId(int groupId) {
        String query = """
            SELECT u.user_id, u.username, u.profile_picture
            FROM User u
            JOIN UserInGroup uig ON u.user_id = uig.user_id
            WHERE uig.group_id = ?
            ORDER BY u.username
        """;
        List<User> users = new ArrayList<>();
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setInt(1, groupId);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("user_id"));
                    user.setUsername(rs.getString("username"));
                    user.setProfilePicturePath(rs.getString("profile_picture"));
                    users.add(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    // Método para obtener los grupos a los que pertenece un usuario
    public List<Group> getGroupsByUserId(Integer userId) { 
        String query = """
            SELECT g.group_id, g.group_name
            FROM "Group" g
            JOIN UserInGroup uig ON g.group_id = uig.group_id
            WHERE uig.user_id = ?
              AND COALESCE(g.blocked, 0) = 0
            ORDER BY g.group_name
        """;
        List<Group> groups = new ArrayList<>();
        try (PreparedStatement statement = db.prepareStatement(query)){
            statement.setObject(1, userId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Group group = new Group();
                group.setGroupId(rs.getInt("group_id"));
                group.setGroupName(rs.getString("group_name"));
                groups.add(group);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return groups;
    }
}
