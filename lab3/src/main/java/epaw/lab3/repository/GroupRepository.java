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
        String query = "SELECT COUNT(*) FROM groups WHERE name = ?";
        
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
        String query = "SELECT COUNT(*) FROM groups WHERE id = ?";
        
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setString(1, String.valueOf(Id));
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
        String query = "SELECT id, name FROM groups ORDER BY id ASC LIMIT 10";
        try (PreparedStatement statement = db.prepareStatement(query);
                ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                Group g = new Group();
                g.setId(rs.getInt("id"));
                g.setName(rs.getString("name"));
                list.add(g);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
