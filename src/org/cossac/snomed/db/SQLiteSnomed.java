package org.cossac.snomed.db;

import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * A version of DBSnomed that takes a filepath for a SQLite database.
 * 
 * @author matt
 *
 */
public class SQLiteSnomed extends DBSnomed {
	private String filepath;
	
	public SQLiteSnomed(String filepath) throws ClassNotFoundException, SQLException {
		super();
		this.filepath = filepath;
	    Class.forName("org.sqlite.JDBC");
	    super.setConnection(DriverManager.getConnection("jdbc:sqlite:" + filepath));
	}
	
	public String getFilepath() {
		return filepath;
	}
}
