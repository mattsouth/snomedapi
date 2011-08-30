package org.cossac.snomed.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.cossac.snomed.Relationship;


/**
 * Database implementation of Relationship Interface from Snomed API.  
 * TODO: implement hooks for SQL so that variations between SQL flavours can be handled.
 * 
 * @author matt
 *
 */
public class DBRelationship implements Relationship {
	private Statement stat;
	private long id;
	private int leftConceptId;
	private int typeId;
	private int rightConceptId;
	private int characteristic; // characteristic type
	private int refinability;
	private int group;
	
	public DBRelationship(long id, Connection conn) throws SQLException {
		super();
		this.id = id;
		this.stat = conn.createStatement();
		loadRelationship();
	}
	
	private void loadRelationship() throws SQLException {
		ResultSet rs = stat.executeQuery("select * from Relationship where RELATIONSHIPID=" + this.id);
		if (rs.next()) {
			this.leftConceptId = rs.getInt(2);
			this.typeId = rs.getInt(3);
			this.rightConceptId = rs.getInt(4);
			this.characteristic = rs.getInt(5);
			this.refinability = rs.getInt(6);
			this.group = rs.getInt(7);
		}
		rs.close();
	}

	/* (non-Javadoc)
	 * @see org.cossac.snomed.Relationship#getId()
	 */
	@Override
	public long getId() {
		return id;
	}

	/* (non-Javadoc)
	 * @see org.cossac.snomed.Relationship#getLeftConceptId()
	 */
	@Override
	public int getSourceConceptId() {
		return leftConceptId;
	}

	/* (non-Javadoc)
	 * @see org.cossac.snomed.Relationship#getTypeId()
	 */
	@Override
	public int getTypeId() {
		return typeId;
	}

	/* (non-Javadoc)
	 * @see org.cossac.snomed.Relationship#getRightConceptId()
	 */
	@Override
	public int getTargetConceptId() {
		return rightConceptId;
	}

	/* (non-Javadoc)
	 * @see org.cossac.snomed.Relationship#getChatacteristic()
	 */
	@Override
	public int getChatacteristic() {
		return characteristic;
	}

	/* (non-Javadoc)
	 * @see org.cossac.snomed.Relationship#getRefinability()
	 */
	@Override
	public int getRefinability() {
		return refinability;
	}

	/* (non-Javadoc)
	 * @see org.cossac.snomed.Relationship#getGroup()
	 */
	@Override
	public int getGroup() {
		return group;
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
		DBRelationship other = (DBRelationship) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
