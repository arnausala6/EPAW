package epaw.lab2.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class DBManager {

	private static DBManager instance;
	private Connection connection = null;
	private static final String DB_FILE = "lab2.db";

	private DBManager() {
		try {
			Class.forName("org.sqlite.JDBC");
			boolean dbExists = Files.exists(Paths.get(DB_FILE));
			connection = DriverManager.getConnection("jdbc:sqlite:" + DB_FILE);

			try (Statement stmt = connection.createStatement()) {
				stmt.execute("PRAGMA foreign_keys = ON;");
			}

			if (!dbExists) {
				initDatabase();
			}
			ensureSchema();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static synchronized DBManager getInstance() {
		if (instance == null) {
			instance = new DBManager();
		}
		return instance;
	}

	private void initDatabase() throws Exception {
		String schemaPath = "DB.txt";
		try (BufferedReader reader = new BufferedReader(new FileReader(schemaPath))) {
			String schema = reader.lines().collect(Collectors.joining("\n"));
			String[] statements = schema.split(";");
			try (Statement stmt = connection.createStatement()) {
				for (String sql : statements) {
					if (!sql.trim().isEmpty()) {
						stmt.execute(sql);
					}
				}
			}
		}
	}

	private void ensureGroupsSeeded() throws SQLException {
		if (!tableExists("groups")) {
			try (Statement stmt = connection.createStatement()) {
				stmt.execute("CREATE TABLE groups (id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR(100) NOT NULL)");
			}
		}
		int count = 0;
		try (PreparedStatement ps = connection.prepareStatement("SELECT COUNT(*) FROM groups");
				ResultSet rs = ps.executeQuery()) {
			if (rs.next()) {
				count = rs.getInt(1);
			}
		}
		if (count > 0) {
			return;
		}
		String[] names = { "Tech Community", "Gaming Hub", "Music Lovers", "Sports Fans", "Travel Tips", "Foodies",
				"Art & Design", "Science News", "Memes", "Local Meetups" };
		String insert = "INSERT INTO groups (name) VALUES (?)";
		for (String name : names) {
			try (PreparedStatement ps = connection.prepareStatement(insert)) {
				ps.setString(1, name);
				ps.executeUpdate();
			}
		}
	}

	private boolean tableExists(String tableName) throws SQLException {
		try (PreparedStatement ps = connection.prepareStatement(
				"SELECT 1 FROM sqlite_master WHERE type='table' AND name=?")) {
			ps.setString(1, tableName);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		}
	}

	private void ensureSchema() throws SQLException {
		ensureGroupsSeeded();

		Set<String> columns = getUserTableColumns();
		try (Statement stmt = connection.createStatement()) {
			if (!columns.contains("username")) {
				stmt.execute("ALTER TABLE users ADD COLUMN username VARCHAR(30)");
			}
			if (!columns.contains("name")) {
				stmt.execute("ALTER TABLE users ADD COLUMN name VARCHAR(30)");
			}
			if (!columns.contains("email")) {
				stmt.execute("ALTER TABLE users ADD COLUMN email VARCHAR(255)");
			}
			if (!columns.contains("gender")) {
				stmt.execute("ALTER TABLE users ADD COLUMN gender VARCHAR(20)");
			}
			if (!columns.contains("description")) {
				stmt.execute("ALTER TABLE users ADD COLUMN description VARCHAR(300)");
			}
			if (!columns.contains("profile_picture_path")) {
				stmt.execute("ALTER TABLE users ADD COLUMN profile_picture_path VARCHAR(255)");
			}
			if (!columns.contains("group_id")) {
				stmt.execute("ALTER TABLE users ADD COLUMN group_id INTEGER");
			}
			if (!columns.contains("age")) {
				stmt.execute("ALTER TABLE users ADD COLUMN age INTEGER");
			}
			if (!columns.contains("country")) {
				stmt.execute("ALTER TABLE users ADD COLUMN country VARCHAR(100)");
			}
		}
		try (Statement stmt = connection.createStatement()) {
			stmt.execute("CREATE UNIQUE INDEX IF NOT EXISTS idx_users_username ON users(username)");
			stmt.execute("CREATE UNIQUE INDEX IF NOT EXISTS idx_users_email ON users(email)");
		}
	}

	private Set<String> getUserTableColumns() throws SQLException {
		Set<String> columns = new HashSet<>();
		if (!tableExists("users")) {
			return columns;
		}
		try (Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery("PRAGMA table_info(users)")) {
			while (rs.next()) {
				columns.add(rs.getString("name"));
			}
		}
		return columns;
	}

	public PreparedStatement prepareStatement(String query) throws SQLException {
		return connection.prepareStatement(query);
	}

	public void close() {
		try {
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
