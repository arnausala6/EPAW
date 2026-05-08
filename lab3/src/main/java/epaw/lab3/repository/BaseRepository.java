package epaw.lab3.repository;

import epaw.lab3.util.DBManager;

public abstract class BaseRepository {

    protected DBManager db;

    protected BaseRepository() {
        this.db = DBManager.getInstance();
    }
}
