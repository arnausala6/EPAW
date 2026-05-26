package epaw.lab4.repository;

import epaw.lab4.util.DBManager;

public abstract class BaseRepository {

    protected DBManager db;

    protected BaseRepository() {
        this.db = DBManager.getInstance();
    }
}
