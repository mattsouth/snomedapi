package org.cossac.snomed.loader;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Custom loader for Stated Relationships table.  Note that in the 20100731 release, the headings of the CSV file had 
 * to be edited - the first stated column wasnt actually in the data.
 * 
 * @author matt
 *
 */
public class SnomedStatedRelationshipsLoader extends CSVtoSQLite {

	public SnomedStatedRelationshipsLoader(String metaFilepath, String dbFilepath)
			throws IOException, ClassNotFoundException, SQLException {
		super(metaFilepath, dbFilepath);
		meta.get(0).sqlCreate = meta.get(0).sqlCreate.replace(" PRIMARY KEY", "");
	}
	
	public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {
		SnomedStatedRelationshipsLoader loader = new SnomedStatedRelationshipsLoader("./etc/meta_stated_relationships.txt", "./etc/snomed.sqlite");
		loader.generate();
	}
}
