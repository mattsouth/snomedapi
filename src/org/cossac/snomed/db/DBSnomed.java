package org.cossac.snomed.db;
/*
The MIT License (MIT)

Copyright (c) 2015 The Chancellor, Masters and Scholars of the University of Oxford.

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

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
	
	private Set<Long> idsQuery(String query) {
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
	public Set<Long> getConceptIds() {
		return idsQuery("select CONCEPTID from Concept");
	}

	@Override
	public Concept getConcept(long conceptId) {
		Concept result=null;
		try {
			result = new DBConcept(conceptId, conn);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public Set<Long> getConceptIds(int statusId) {
		return idsQuery("select CONCEPTID from Concept where CONCEPTSTATUS=" + statusId);
	}

	@Override
	public Set<Long> getConceptIds(String match) {
		return idsQuery("select CONCEPTID from Concept where FULLYSPECIFIEDNAME LIKE '%" + match + "%'");
	}

	@Override
	public Set<Long> getConceptIds(boolean isPrimitive) {
		return idsQuery("select CONCEPTID from Concept where ISPRIMITIVE=" + (isPrimitive?1:0));
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
	public Description getDescription(long id) {
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
	public Set<Long> getRelationshipTypeIds() {
		return idsQuery("select Concept.CONCEPTID FROM Concept WHERE Concept.CONCEPTID IN (SELECT DISTINCT Relationship.RELATIONSHIPTYPE FROM Relationship)");
	}

	@Override
	public Set<Long> getRelationshipIds(long typeId) {
		return longIdsQuery("select RELATIONSHIPID from Relationship where RELATIONSHIPTYPE=" + typeId);
	}

	@Override
	public Set<Relationship> getRelationships(long conceptID) {
		Set<Relationship> result = new HashSet<Relationship>();
		ResultSet rs;
		try {
			rs = stat.executeQuery("select RELATIONSHIPID from Relationship where CONCEPTID1=" + conceptID + " OR CONCEPTID2=" + conceptID);
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
