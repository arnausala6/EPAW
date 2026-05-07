package epaw.lab2.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import epaw.lab2.model.User;

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
		String query = "SELECT COUNT(*) FROM users WHERE username = ?";
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

	public boolean existsByEmail(String email) {
		String query = "SELECT COUNT(*) FROM users WHERE LOWER(email) = LOWER(?)";
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

	public void save(User user) {
		String query = "INSERT INTO users (username, name, email, gender, description, profile_picture_path, group_id, age, country, password) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try (PreparedStatement statement = db.prepareStatement(query)) {
			statement.setString(1, user.getUsername());
			statement.setString(2, user.getName());
			statement.setString(3, user.getEmail());
			statement.setString(4, user.getGender());
			statement.setString(5, user.getDescription());
			statement.setString(6, user.getProfilePicturePath());
			if (user.getGroupId() == null) {
				statement.setNull(7, java.sql.Types.INTEGER);
			} else {
				statement.setInt(7, user.getGroupId());
			}
			if (user.getAge() == null) {
				statement.setNull(8, java.sql.Types.INTEGER);
			} else {
				statement.setInt(8, user.getAge());
			}
			statement.setString(9, user.getCountry());
			statement.setString(10, user.getPassword());
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Optional<User> findByUsername(String username) {
		String query = "SELECT id, username, name, email, gender, description, profile_picture_path, group_id, age, country, password "
				+ "FROM users WHERE username = ?";
		try (PreparedStatement statement = db.prepareStatement(query)) {
			statement.setString(1, username);
			ResultSet rs = statement.executeQuery();
			if (rs.next()) {
				return Optional.of(mapRow(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return Optional.empty();
	}

	private User mapRow(ResultSet rs) throws SQLException {
		User user = new User();
		user.setId(rs.getInt("id"));
		user.setUsername(rs.getString("username"));
		user.setName(rs.getString("name"));
		user.setEmail(rs.getString("email"));
		user.setGender(rs.getString("gender"));
		user.setDescription(rs.getString("description"));
		user.setProfilePicturePath(rs.getString("profile_picture_path"));
		int gid = rs.getInt("group_id");
		user.setGroupId(rs.wasNull() ? null : gid);
		int age = rs.getInt("age");
		user.setAge(rs.wasNull() ? null : age);
		user.setCountry(rs.getString("country"));
		user.setPassword(rs.getString("password"));
		return user;
	}
}
