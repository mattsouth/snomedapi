package org.cossac.snomed.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.cossac.snomed.Description;


/**
 * Database implementation of Description Interface from Snomed API.  
 * TODO: implement hooks for SQL so that variations between SQL flavours can be handled.
 * 
 * @author matt
 *
 */
public class DBDescription implements Description {
	private long id;
	private int status;
	private int conceptId;
	private String term;
	private boolean initialCapitalStatus;
	private int type;
	private String languageCode;
	private Statement stat;
	
	public DBDescription(long id, Connection conn) throws SQLException {
		super();
		this.id = id;
		this.stat = conn.createStatement();
		loadDescription();
	}

	private void loadDescription() throws SQLException {
		ResultSet rs = stat.executeQuery("select * from Relationship where RELATIONSHIPID=" + this.id);
		if (rs.next()) {
			this.status = rs.getInt(2);
			this.conceptId = rs.getInt(3);
			this.term = rs.getString(4);
			this.initialCapitalStatus = rs.getBoolean(5);
			this.type = rs.getInt(6);
			this.languageCode = rs.getString(7);
		}
		rs.close();
	}
	/* (non-Javadoc)
	 * @see org.cossac.snomed.Description#getId()
	 */
	@Override
	public long getId() {
		return id;
	}
	/* (non-Javadoc)
	 * @see org.cossac.snomed.Description#getStatus()
	 */
	@Override
	public int getStatus() {
		return status;
	}
	/* (non-Javadoc)
	 * @see org.cossac.snomed.Description#getConceptId()
	 */
	@Override
	public int getConceptId() {
		return conceptId;
	}
	/* (non-Javadoc)
	 * @see org.cossac.snomed.Description#getTerm()
	 */
	@Override
	public String getTerm() {
		return term;
	}
	/* (non-Javadoc)
	 * @see org.cossac.snomed.Description#isInitialCapitalStatus()
	 */
	@Override
	public boolean isInitialCapitalStatus() {
		return initialCapitalStatus;
	}
	/* (non-Javadoc)
	 * @see org.cossac.snomed.Description#getType()
	 */
	@Override
	public int getType() {
		return type;
	}
	/* (non-Javadoc)
	 * @see org.cossac.snomed.Description#getLanguageCode()
	 */
	@Override
	public String getLanguageCode() {
		return languageCode;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DBDescription other = (DBDescription) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
