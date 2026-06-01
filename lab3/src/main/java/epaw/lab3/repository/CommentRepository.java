package epaw.lab3.repository;

import epaw.lab3.model.Comment;
import epaw.lab3.util.DBManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Timestamp;

public class CommentRepository extends BaseRepository {

    private static CommentRepository instance;

    public CommentRepository() {
        super();
    }

    public static synchronized CommentRepository getInstance() {
        if (instance == null) {
            instance = new CommentRepository();
        }
        return instance;
    }

}
