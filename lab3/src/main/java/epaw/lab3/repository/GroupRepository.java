package epaw.lab3.repository;

import epaw.lab3.model.Group;
import epaw.lab3.util.DBManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Timestamp;

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

    public boolean groupExists(String name){
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

    public boolean groupIdExists(Integer Id){
        String query = "SELECT COUNT(*) FROM \"Group\" WHERE group_id = ?";
        
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setInt(1, Id);;
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

    public List<Group> getTopTenGroups() {
        List<Group> groups = new ArrayList<>();

        String sql = """
            SELECT group_id, group_name, description, creator_id, participants, group_picture, date_of_creation
            FROM "Group"
            ORDER BY participants DESC
            LIMIT 10
        """;

        try (PreparedStatement stmt = db.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Group g = new Group();
                g.setGroupId(rs.getInt("group_id"));
                g.setGroupName(rs.getString("group_name"));
                g.setDescription(rs.getString("description"));
                g.setCreatorId(rs.getInt("creator_id"));
                g.setParticipants(rs.getInt("participants"));
                g.setGroupPicture(rs.getString("group_picture"));
                g.setDateOfCreation(rs.getTimestamp("date_of_creation").toLocalDateTime());

                groups.add(g);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return groups;
    }

    public Group findById(int id) {
        String sql = """
            SELECT group_id, group_name, description, creator_id,
                participants, group_picture, date_of_creation
            FROM "Group"
            WHERE group_id = ?
        """;

        try (PreparedStatement stmt = db.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
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

                    return g;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void save(Group g) {
        String sql = """
            INSERT INTO "Group"
            (group_name, description, creator_id, participants, group_picture)
            VALUES (?, ?, ?, ?, ?)
        """;

        try (PreparedStatement stmt = db.prepareStatement(sql)) {

            stmt.setString(1, g.getGroupName());
            stmt.setString(2, g.getDescription());
            stmt.setInt(3, g.getCreatorId());
            stmt.setInt(4, g.getParticipants());
            stmt.setString(5, g.getGroupPicture());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Group g) {
        String sql = """
            UPDATE "Group"
            SET group_name = ?,
                description = ?,
                creator_id = ?,
                participants = ?,
                group_picture = ?
            WHERE group_id = ?
        """;

        try (PreparedStatement stmt = db.prepareStatement(sql)) {

            stmt.setString(1, g.getGroupName());
            stmt.setString(2, g.getDescription());
            stmt.setInt(3, g.getCreatorId());
            stmt.setInt(4, g.getParticipants());
            stmt.setString(5, g.getGroupPicture());
            stmt.setInt(6, g.getGroupId());

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

}
