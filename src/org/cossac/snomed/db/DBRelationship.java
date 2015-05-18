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
	private long leftConceptId;
	private long typeId;
	private long rightConceptId;
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
			this.leftConceptId = rs.getLong(2);
			this.typeId = rs.getLong(3);
			this.rightConceptId = rs.getLong(4);
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
	public long getSourceConceptId() {
		return leftConceptId;
	}

	/* (non-Javadoc)
	 * @see org.cossac.snomed.Relationship#getTypeId()
	 */
	@Override
	public long getTypeId() {
		return typeId;
	}

	/* (non-Javadoc)
	 * @see org.cossac.snomed.Relationship#getRightConceptId()
	 */
	@Override
	public long getTargetConceptId() {
		return rightConceptId;
	}

	/* (non-Javadoc)
	 * @see org.cossac.snomed.Relationship#getChatacteristic()
	 */
	@Override
	public int getCharacteristic() {
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
