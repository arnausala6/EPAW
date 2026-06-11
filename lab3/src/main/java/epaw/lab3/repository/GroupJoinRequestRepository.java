package epaw.lab3.repository;

import epaw.lab3.model.GroupJoinRequest;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GroupJoinRequestRepository extends BaseRepository {

    private static GroupJoinRequestRepository instance;

    public GroupJoinRequestRepository() {
        super();
    }

    public static synchronized GroupJoinRequestRepository getInstance() {
        if (instance == null) {
            instance = new GroupJoinRequestRepository();
        }
        return instance;
    }

    public void insert(int userId, int groupId) {
        String query = "INSERT INTO GroupJoinRequest (user_id, group_id) VALUES (?, ?)";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.setInt(2, groupId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int userId, int groupId) {
        String query = "DELETE FROM GroupJoinRequest WHERE user_id = ? AND group_id = ?";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.setInt(2, groupId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean hasPendingRequest(int userId, int groupId) {
        String query = "SELECT COUNT(*) FROM GroupJoinRequest WHERE user_id = ? AND group_id = ?";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setInt(1, userId);
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

    public Set<Integer> findPendingGroupIdsForUser(int userId) {
        Set<Integer> groupIds = new HashSet<>();
        String query = "SELECT group_id FROM GroupJoinRequest WHERE user_id = ?";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setInt(1, userId);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    groupIds.add(rs.getInt("group_id"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return groupIds;
    }

    public List<GroupJoinRequest> findPendingByGroupId(int groupId) {
        List<GroupJoinRequest> requests = new ArrayList<>();
        String query = """
            SELECT gjr.user_id, gjr.group_id, u.username, u.profile_picture
            FROM GroupJoinRequest gjr
            JOIN User u ON gjr.user_id = u.user_id
            WHERE gjr.group_id = ?
            ORDER BY gjr.requested_at ASC
        """;
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setInt(1, groupId);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    GroupJoinRequest request = new GroupJoinRequest();
                    request.setUserId(rs.getInt("user_id"));
                    request.setGroupId(rs.getInt("group_id"));
                    request.setUsername(rs.getString("username"));
                    request.setProfilePicture(rs.getString("profile_picture"));
                    requests.add(request);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
    }
}
