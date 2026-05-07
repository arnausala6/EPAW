package epaw.lab2.repository;

import epaw.lab2.util.DBManager;

public abstract class BaseRepository {
	
	protected DBManager db;

    protected BaseRepository() {
    	this.db = DBManager.getInstance();
    }
}
