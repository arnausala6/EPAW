package epaw.lab3.repository;

import epaw.lab3.model.Group;
import epaw.lab3.util.DBManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class GroupRepository extends BaseRepository {

    private static GroupRepository instance;

    public GroupRepository() {
        super();
    }

    public static synchronized GroupRepository getInstance() {
        if (instance == null) {
            instance = new GroupRepository();
        }
        return instance;
    }

    public boolean groupExists(String name) {
        String query = "SELECT COUNT(*) FROM \"Group\" WHERE group_name = ?";

        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setString(1, name);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean groupNameExistsForOther(String name, int groupId) {
        String query = "SELECT COUNT(*) FROM \"Group\" WHERE group_name = ? AND group_id != ?";

        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setString(1, name);
            statement.setInt(2, groupId);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean groupIdExists(Integer id) {
        String query = "SELECT COUNT(*) FROM \"Group\" WHERE group_id = ?";

        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int getNextGroupId() {
        String sql = "SELECT COALESCE(MAX(group_id), 0) + 1 FROM \"Group\"";
        try (PreparedStatement stmt = db.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 1;
    }

    public List<Group> getTopTenGroups() {
        List<Group> groups = new ArrayList<>();

        String sql = """
            SELECT g.group_id, g.group_name, g.description, g.creator_id, g.participants,
                   g.group_picture, g.date_of_creation, g.owner, g.privacy, g.blocked, g.block_reason,
                   (SELECT COUNT(*) FROM UserInGroup uig WHERE uig.group_id = g.group_id) AS member_count
            FROM "Group" g
            WHERE COALESCE(g.blocked, 0) = 0
            ORDER BY g.participants DESC
            LIMIT 10
        """;

        try (PreparedStatement stmt = db.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                groups.add(mapGroup(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return groups;
    }

    public List<Group> findByUserId(int userId) {
        List<Group> groups = new ArrayList<>();

        String sql = """
            SELECT DISTINCT g.group_id, g.group_name, g.description, g.creator_id, g.participants,
                   g.group_picture, g.date_of_creation, g.owner, g.privacy, g.blocked, g.block_reason,
                   (SELECT COUNT(*) FROM UserInGroup uig2 WHERE uig2.group_id = g.group_id) AS member_count
            FROM "Group" g
            JOIN User u ON u.user_id = ?
            LEFT JOIN UserInGroup uig ON g.group_id = uig.group_id AND uig.user_id = u.user_id
            WHERE uig.user_id IS NOT NULL OR g.owner = u.username
            ORDER BY g.group_name
        """;

        try (PreparedStatement stmt = db.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    groups.add(mapGroup(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return groups;
    }

    public List<Group> findSuggestionsForUser(int userId) {
        List<Group> groups = new ArrayList<>();

        String sql = """
            SELECT g.group_id, g.group_name, g.description, g.creator_id, g.participants,
                   g.group_picture, g.date_of_creation, g.owner, g.privacy, g.blocked, g.block_reason,
                   (SELECT COUNT(*) FROM UserInGroup uig WHERE uig.group_id = g.group_id) AS member_count
            FROM "Group" g
            WHERE COALESCE(g.blocked, 0) = 0
              AND g.group_id NOT IN (
                SELECT group_id FROM UserInGroup WHERE user_id = ?
            )
            ORDER BY g.participants DESC
        """;

        try (PreparedStatement stmt = db.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    groups.add(mapGroup(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return groups;
    }

    public Group findById(int id) {
        String sql = """
            SELECT g.group_id, g.group_name, g.description, g.creator_id, g.participants,
                   g.group_picture, g.date_of_creation, g.owner, g.privacy, g.blocked, g.block_reason,
                   ou.country AS owner_country,
                   (SELECT COUNT(*) FROM UserInGroup uig WHERE uig.group_id = g.group_id) AS member_count,
                   (SELECT COUNT(*) FROM Post p WHERE p.group_id = g.group_id AND p.response_id IS NULL) AS post_count
            FROM "Group" g
            LEFT JOIN User ou ON ou.username = g.owner
            WHERE g.group_id = ?
        """;

        try (PreparedStatement stmt = db.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapGroup(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void insert(Group g) {
        String sql = """
            INSERT INTO "Group"
            (group_id, group_name, description, creator_id, participants, group_picture,
             date_of_creation, owner, privacy)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (PreparedStatement stmt = db.prepareStatement(sql)) {
            stmt.setInt(1, g.getGroupId());
            stmt.setString(2, g.getGroupName());
            stmt.setString(3, g.getDescription());
            if (g.getCreatorId() != null) {
                stmt.setInt(4, g.getCreatorId());
            } else {
                stmt.setNull(4, java.sql.Types.INTEGER);
            }
            stmt.setInt(5, g.getParticipants() != null ? g.getParticipants() : 1);
            stmt.setString(6, g.getGroupPicture());
            stmt.setTimestamp(7, Timestamp.valueOf(
                    g.getDateOfCreation() != null ? g.getDateOfCreation() : LocalDateTime.now()));
            stmt.setString(8, g.getOwner());
            stmt.setString(9, g.getPrivacy());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void save(Group g) {
        insert(g);
    }

    public void update(Group g) {
        String sql = """
            UPDATE "Group"
            SET group_name = ?,
                description = ?,
                creator_id = ?,
                participants = ?,
                group_picture = ?,
                owner = ?,
                privacy = ?
            WHERE group_id = ?
        """;

        try (PreparedStatement stmt = db.prepareStatement(sql)) {
            stmt.setString(1, g.getGroupName());
            stmt.setString(2, g.getDescription());
            stmt.setInt(3, g.getCreatorId());
            stmt.setInt(4, g.getParticipants());
            stmt.setString(5, g.getGroupPicture());
            stmt.setString(6, g.getOwner());
            stmt.setString(7, g.getPrivacy());
            stmt.setInt(8, g.getGroupId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        String sql = """
            DELETE FROM "Group"
            WHERE group_id = ?
        """;

        try (PreparedStatement stmt = db.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Group> findingGroup(String search) {
        List<Group> groups = new ArrayList<>();
        String sql = """
            SELECT g.group_id, g.group_name, g.description, g.creator_id, g.participants,
                   g.group_picture, g.date_of_creation, g.owner, g.privacy, g.blocked, g.block_reason,
                   (SELECT COUNT(*) FROM UserInGroup uig WHERE uig.group_id = g.group_id) AS member_count
            FROM "Group" g
            WHERE g.group_name LIKE ?
            ORDER BY g.participants DESC
            LIMIT 8
        """;
        try (PreparedStatement statement = db.prepareStatement(sql)) {
            statement.setString(1, "%" + search + "%");
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    groups.add(mapGroup(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return groups;
    }

    private Group mapGroup(ResultSet rs) throws SQLException {
        Group g = new Group();
        g.setGroupId(rs.getInt("group_id"));
        g.setGroupName(rs.getString("group_name"));
        g.setDescription(rs.getString("description"));
        g.setCreatorId(rs.getInt("creator_id"));
        g.setParticipants(rs.getInt("participants"));
        g.setGroupPicture(rs.getString("group_picture"));
        Timestamp ts = rs.getTimestamp("date_of_creation");
        if (ts != null) {
            g.setDateOfCreation(ts.toLocalDateTime());
        }
        g.setOwner(rs.getString("owner"));
        g.setPrivacy(rs.getString("privacy"));
        g.setMemberCount(rs.getInt("member_count"));
        try {
            g.setPostCount(rs.getInt("post_count"));
        } catch (SQLException ignored) {
            g.setPostCount(0);
        }
        try {
            g.setOwnerCountry(rs.getString("owner_country"));
        } catch (SQLException ignored) {
            g.setOwnerCountry(null);
        }
        try {
            int blocked = rs.getInt("blocked");
            g.setBlocked(!rs.wasNull() && blocked == 1);
            g.setBlockReason(rs.getString("block_reason"));
        } catch (SQLException ignored) {
            g.setBlocked(false);
            g.setBlockReason(null);
        }
        return g;
    }

    public void blockGroup(int groupId, String reason) {
        String sql = """
            UPDATE "Group"
            SET blocked = 1, block_reason = ?
            WHERE group_id = ?
        """;
        try (PreparedStatement stmt = db.prepareStatement(sql)) {
            stmt.setString(1, reason);
            stmt.setInt(2, groupId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
