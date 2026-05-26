package epaw.lab3.repository;

import epaw.lab3.model.Group;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GroupRepository extends BaseRepository {

    private static GroupRepository instance;

    private GroupRepository() {
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

    public boolean groupIdExists(Integer id){
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

    public List<Group> findTopTen() {
        List<Group> list = new ArrayList<>();
        String query = "SELECT group_id, group_name, description, group_picture, date_of_creation, creator_id FROM \"Group\" ORDER BY group_id ASC LIMIT 10";
        try (PreparedStatement statement = db.prepareStatement(query);
                ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                Group g = new Group();
                g.setGroupId(rs.getInt("group_id"));
                g.setGroupName(rs.getString("group_name"));
                g.setDescription(rs.getString("description"));
                g.setGroupPicture(rs.getString("group_picture"));
                g.setDateOfCreation(rs.getTimestamp("date_of_creation"));
                g.setCreatorId(rs.getInt("creator_id"));
                list.add(g);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}