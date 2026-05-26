package epaw.lab4.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import epaw.lab4.model.User;

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
        String query = "SELECT id, picture from users where name=? AND password=?";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getPassword());
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    user.setId(rs.getInt("id"));
                    user.setPicture(rs.getString("picture"));
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void save(User user) {
        String query = "INSERT INTO users (name, password, picture) VALUES (?, ?, ?)";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getPicture());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Optional<User> findByName(String name) {
        String query = "SELECT id, name, password, picture FROM users WHERE name = ?";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setString(1, name);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setPassword(rs.getString("password"));
                user.setPicture(rs.getString("picture"));
                return Optional.of(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    // Find a user by their name
    public Optional<User> findById(Integer id) {
        String query = "SELECT id, name, picture FROM users WHERE id = ?";
        try (PreparedStatement statement = db.prepareStatement(query)) {
        	statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()) {
	            if (rs.next()) {
	                User user = new User();
	                user.setId(rs.getInt("id"));
	                user.setName(rs.getString("name"));
	                user.setPicture(rs.getString("picture"));
	                return Optional.of(user);
	            }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
    
    // Retrieve all users from the database
    public Optional<List<User>> findAll() {
        List<User> users = new ArrayList<>();
        String query = "SELECT id, name, picture FROM users";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            try (ResultSet rs = statement.executeQuery()) {
	            while (rs.next()) {
	                User user = new User();
	                user.setId(rs.getInt("id"));
	                user.setName(rs.getString("name"));
	                user.setPicture(rs.getString("picture"));
	                users.add(user);
	            }
	            return Optional.of(users);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return Optional.empty();
    }
    
	// Follow a user
	public void followUser(Integer uid, Integer fid) {
		String query = "INSERT INTO follows (uid,fid) VALUES (?,?)";
		try (PreparedStatement statement = db.prepareStatement(query)) {
			statement.setInt(1, uid);
			statement.setInt(2, fid);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Unfollow a user
	public void unfollowUser(Integer uid, Integer fid) {
		String query = "DELETE FROM follows WHERE uid = ? AND fid = ?";
		try (PreparedStatement statement = db.prepareStatement(query)) {
			statement.setInt(1, uid);
			statement.setInt(2, fid);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}    
    
	public Optional<List<User>> findNotFollowed(Integer id, Integer start, Integer end) {
		String query = "SELECT id,name,picture FROM users WHERE id NOT IN (SELECT id FROM users,follows WHERE id = fid AND uid = ?) AND id <> ? ORDER BY name LIMIT ?,?;";
		try (PreparedStatement statement = db.prepareStatement(query)) {
			statement.setInt(1, id);
			statement.setInt(2, id);
			statement.setInt(3, start);
			statement.setInt(4, end);
			try (ResultSet rs = statement.executeQuery()) {
				List<User> users = new ArrayList<User>();
				while (rs.next()) {
					User user = new User();
					user.setId(rs.getInt("id"));
					user.setName(rs.getString("name"));
					user.setPicture(rs.getString("picture"));
					users.add(user);
				}
				return Optional.of(users);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return Optional.empty();
	}

	public Optional<List<User>> findFollowed(Integer id, Integer start, Integer end) {
		String query = "SELECT id,name,picture FROM users,follows WHERE id = fid AND uid = ? ORDER BY name LIMIT ?,?;";
		try (PreparedStatement statement = db.prepareStatement(query)) {
			statement.setInt(1, id);
			statement.setInt(2, start);
			statement.setInt(3, end);
			try (ResultSet rs = statement.executeQuery()) {
				List<User> users = new ArrayList<User>();
				while (rs.next()) {
					User user = new User();
					user.setId(rs.getInt("id"));
					user.setName(rs.getString("name"));
					user.setPicture(rs.getString("picture"));
					users.add(user);
				}
				return Optional.of(users);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return Optional.empty();
	}
}
