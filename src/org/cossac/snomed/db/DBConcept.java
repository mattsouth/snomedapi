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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.cossac.snomed.Concept;
import org.cossac.snomed.Description;
import org.cossac.snomed.Relationship;


/**
 * Database implementation of Concept Interface from Snomed API.  
 * TODO: implement hooks for SQL so that variations between SQL flavours can be handled.
 * 
 * @author matt
 *
 */
public class DBConcept implements Concept {
	private Connection conn;
	private Statement stat;
	private long id;
	private int status;
	private String fullySpecifiedName;
	private String ctv3Id;
	private String snomedId;
	private boolean primitive;
	private Map<Long, Set<Relationship>> inwards;
	private Map<Long, Set<Relationship>> outwards;

	private Set<Description> descriptions = null;
	
	public DBConcept(long id, Connection conn) throws SQLException {
		super();
		this.id = id;
		this.conn = conn;
		this.stat = conn.createStatement();
		loadConcept();
	}
	
	private void loadConcept() throws SQLException {
		ResultSet rs = stat.executeQuery("select * from Concept where CONCEPTID=" + this.id);
		if (rs.next()) {
			this.status = rs.getInt(2);
			this.fullySpecifiedName = rs.getString(3);
			this.ctv3Id = rs.getString(4);
			this.snomedId = rs.getString(5);
			this.primitive = rs.getBoolean(6);
		}
		rs.close();
	}

	/* (non-Javadoc)
	 * @see org.cossac.snomed.Concept#getId()
	 */
	@Override
	public long getId() {
		return id;
	}

	/* (non-Javadoc)
	 * @see org.cossac.snomed.Concept#getStatus()
	 */
	@Override
	public Status getStatus() {
		return Status.enumFromValue(status);
	}

	/* (non-Javadoc)
	 * @see org.cossac.snomed.Concept#getFullySpecifiedName()
	 */
	@Override
	public String getFullySpecifiedName() {
		return fullySpecifiedName;
	}

	/* (non-Javadoc)
	 * @see org.cossac.snomed.Concept#getCtv3Id()
	 */
	@Override
	public String getCtv3Id() {
		return ctv3Id;
	}

	/* (non-Javadoc)
	 * @see org.cossac.snomed.Concept#getSnomedId()
	 */
	@Override
	public String getSnomedId() {
		return snomedId;
	}

	/* (non-Javadoc)
	 * @see org.cossac.snomed.Concept#isPrimitive()
	 */
	@Override
	public boolean isPrimitive() {
		return primitive;
	}

	/* (non-Javadoc)
	 * @see org.cossac.snomed.Concept#getDescriptions()
	 */
	@Override
	public Set<Description> getDescriptions() {
		if (descriptions==null) {
			loadDescriptions();
		}
		return descriptions;
	}
	
	private void loadDescriptions()  {
		descriptions = new HashSet<Description>();
		ResultSet rs;
		try {
			rs = stat.executeQuery("SELECT DESCRIPTIONID FROM Description WHERE CONCEPTID=" + id);
			while (rs.next()) {
				descriptions.add(new DBDescription(rs.getLong(1), conn));
			}
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		DBConcept other = (DBConcept) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public Set<Long> getRelationshipTypeIds(Direction direction) {
		if (direction==Direction.INWARDS) {
			if (inwards==null) {
				loadAllInwards();
			}
			return inwards.keySet();
		} else {
			if (outwards==null) {
				loadAllOutwards();
			}
			return outwards.keySet();			
		}
	}

	public Set<Relationship> getRelationships(long typeId, Direction direction) {
		if (direction==Direction.INWARDS) {
			if (inwards==null) {
				loadAllInwards();
			}
			return inwards.get(typeId);
		} else {
			if (outwards==null) {
				loadAllOutwards();
			}
			return outwards.get(typeId);
		}
	}
	private void loadAllOutwards() {
		outwards= loadAllRelationships("select RELATIONSHIPID from Relationship where CONCEPTID1=" + id);
	}
	
	private void loadAllInwards() {
		inwards = loadAllRelationships("select RELATIONSHIPID from Relationship where CONCEPTID2=" + id);
	}
	
	private Map<Long, Set<Relationship>> loadAllRelationships(String query) {
		Map<Long, Set<Relationship>> map = new HashMap<Long, Set<Relationship>>();
		ResultSet rs;
		try {
			rs = stat.executeQuery(query);
			while (rs.next()) {
				Relationship rel = new DBRelationship(rs.getLong(1), conn);
				if (map.containsKey(rel.getTypeId())) {
					map.get(rel.getTypeId()).add(rel);
				} else {
					Set<Relationship> relationshipSet = new HashSet<Relationship>();
					relationshipSet.add(rel);
					map.put(rel.getTypeId(), relationshipSet);
				}
			}
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}

	
	private void loadPartialOutwards(long typeId) {
		outwards.put(typeId, getRelationships("select RELATIONSHIPID from Relationship where CONCEPTID1=" + id + " AND RELATIONSHIPTYPE=" + typeId));
	}
	
	private void loadPartialInwards(long typeId) {
		inwards.put(typeId, getRelationships("select RELATIONSHIPID from Relationship where CONCEPTID2=" + id + " AND RELATIONSHIPTYPE=" + typeId));
	}

	private Set<Relationship> getRelationships(String query) {
		Set<Relationship> result = new HashSet<Relationship>();
		ResultSet rs;
		try {
			rs = stat.executeQuery(query);
			while (rs.next()) {
				result.add(new DBRelationship(rs.getLong(1), conn));
			}
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
}
