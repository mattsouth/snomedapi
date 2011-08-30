package org.cossac.snomed.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import org.cossac.snomed.Concept;
import org.cossac.snomed.Description;
import org.cossac.snomed.Relationship;
import org.cossac.snomed.Snomed;


/**
 * Database implementation of Snomed Interface from Snomed API.  
 * TODO: implement hooks for SQL so that variations between SQL flavours can be handled.
 * 
 * @author matt
 *
 */
public abstract class DBSnomed implements Snomed {
	protected Connection conn;
	protected Statement stat;
	
	void setConnection(Connection conn) throws SQLException {
		this.conn = conn;
		this.stat = conn.createStatement();
	}
	
	private Set<Integer> idsQuery(String query) {
		Set<Integer> result = new HashSet<Integer>();
		ResultSet rs;
		try {
			rs = stat.executeQuery(query);
			while (rs.next()) {
				result.add(rs.getInt(1));
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;		
	}
	
	@Override
	public Set<Integer> getConceptIds() {
		return idsQuery("select CONCEPTID from Concept");
	}

	@Override
	public Concept getConcept(int conceptId) {
		Concept result=null;
		try {
			result = new DBConcept(conceptId, conn);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public Set<Integer> getConceptIds(int statusId) {
		return idsQuery("select CONCEPTID from Concept where CONCEPTSTATUS=" + statusId);
	}

	@Override
	public Set<Integer> getConceptIds(String match) {
		return idsQuery("select CONCEPTID from Concept where FULLYSPECIFIEDNAME LIKE '%" + match + "%'");
	}

	@Override
	public Set<Integer> getConceptIds(boolean isPrimitive) {
		return idsQuery("select CONCEPTID from Concept where ISPRIMITIVE=" + isPrimitive);
	}
	
	private Set<Long> longIdsQuery(String query) {
		Set<Long> result = new HashSet<Long>();
		ResultSet rs;
		try {
			rs = stat.executeQuery(query);
			while (rs.next()) {
				result.add(rs.getLong(1));
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;		
	}

	@Override
	public Set<Long> getDescriptionIds() {
		return longIdsQuery("select DESCRIPTIONID from Description");
	}

	@Override
	public Description getDescription(Long id) {
		Description result = null;
		try {
			result = new DBDescription(id, conn);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public Set<Long> getDescriptionIds(String match) {
		return longIdsQuery("select DESCRIPTIONID from Description where TERM like '%" + match +"%'");
	}

	@Override
	public Set<Long> getDescriptionIdsByStatus(int status) {
		return longIdsQuery("select DESCRIPTIONID from Description where DESCRIPTIONSTATUS=" + status);
	}

	@Override
	public Set<Long> getDescriptionIdsByType(int type) {
		return longIdsQuery("select DESCRIPTIONID from Description where DESCRIPTIONTYPE=" + type);
	}

	// Relationships
	
	@Override
	public Set<Long> getRelationshipIds() {
		return longIdsQuery("select RELATIONSHIPID from Relationship");
	}

	@Override
	public Relationship getRelationship(long id) {
		Relationship result=null;
		try {
			result = new DBRelationship(id, conn);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@Override
	public Set<Integer> getRelationshipTypeIds() {
		return idsQuery("select Concept.CONCEPTID FROM Concept WHERE Concept.CONCEPTID IN (SELECT DISTINCT Relationship.RELATIONSHIPTYPE FROM Relationship)");
	}

	@Override
	public Set<Long> getRelationshipIds(int typeId) {
		return longIdsQuery("select RELATIONSHIPID from Relationship where RELATIONSHIPTYPE=" + typeId);
	}

	@Override
	public Set<Relationship> getRelationships(int conceptID) {
		Set<Relationship> result = new HashSet<Relationship>();
		ResultSet rs;
		try {
			rs = stat.executeQuery("select RELATIONSHIPID form Relationship where CONCEPTID1=" + conceptID + " OR CONCEPTID2=" + conceptID);
			while (rs.next()) {
				result.add(new DBRelationship(rs.getLong(1), conn));
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

}
