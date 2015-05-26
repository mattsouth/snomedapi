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
	private long conceptId;
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
		ResultSet rs = stat.executeQuery("select * from Description where DESCRIPTIONID=" + this.id);
		if (rs.next()) {
			this.status = rs.getInt(2);
			this.conceptId = rs.getLong(3);
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
	public Status getStatus() {
		return Status.enumFromValue(status);
	}
	/* (non-Javadoc)
	 * @see org.cossac.snomed.Description#getConceptId()
	 */
	@Override
	public long getConceptId() {
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
	public Description.Type getType() {
		return Type.enumFromValue(type);
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
