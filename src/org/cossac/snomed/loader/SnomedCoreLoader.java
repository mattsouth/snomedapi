package org.cossac.snomed.loader;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Custom loader for Snomed CT core tables.  Edit ./etc/meta_core.txt.dist and rename to ./etc/meta_core.txt before
 * running this.
 * 
 * TODO: Add FK constraints.
 * 
 * @author matt
 *
 */
public class SnomedCoreLoader extends CSVtoSQLite {

	public SnomedCoreLoader(String metaFilepath, String dbFilepath)
			throws IOException, ClassNotFoundException, SQLException {
		super(metaFilepath, dbFilepath);
	}

	public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {
		CSVtoSQLite loader = new CSVtoSQLite("./etc/meta_core.txt", "./etc/snomed.sqlite");
		loader.generate();
		executeSQL(loader, "create index i1 on Relationship(CONCEPTID1);");
		executeSQL(loader, "create index i2 on Relationship(CONCEPTID2);");
		executeSQL(loader, "create index i3 on Description(CONCEPTID);");
		loader.conn.commit();
		// populate a map of suffices
		Map<String, Integer> map = new HashMap<String, Integer>();
		ResultSet rs;
		int increment=0;
		try {
			rs = loader.stat.executeQuery("select * from Concept where CONCEPTSTATUS=0"); // NB Note stricter criteria than next step to filter out some crud
			while (rs.next()) {
				String fsn = rs.getString(3);
				Pattern pattern = Pattern.compile("\\s\\([\\w\\s]+\\)+$");
				Matcher matcher = pattern.matcher(fsn);
				while (matcher.find()) {
					String suffix = matcher.group().trim().replace("(", "").replace(")", "");
					Integer key = map.get(suffix);
					if (key==null) map.put(suffix, increment++);
				}
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// populate the suffix table
		executeSQL(loader, "create table Suffix ('SUFFIXID' PRIMARY KEY, 'VALUE');");
		PreparedStatement prep = loader.conn.prepareStatement("INSERT INTO Suffix(SUFFIXID, VALUE) VALUES (?, ?);");
		for (String suffix : map.keySet()) {
			prep.setInt(1, map.get(suffix));
			prep.setString(2, suffix);
			prep.execute();
		}
		// populate the conceptsuffix join table
		executeSQL(loader, "create table ConceptSuffix ('CONCEPTID', 'SUFFIXID');");
		prep = loader.conn.prepareStatement("INSERT INTO ConceptSuffix(CONCEPTID, SUFFIXID) VALUES (?, ?);");
		try {
			rs = loader.stat.executeQuery("select * from Concept");
			while (rs.next()) {
				Integer conceptid = rs.getInt(1);
				String fsn = rs.getString(3);
				Pattern pattern = Pattern.compile("\\s\\([\\w\\s]+\\)+$");
				Matcher matcher = pattern.matcher(fsn);
				while (matcher.find()) {
					String suffix = matcher.group().trim().replace("(", "").replace(")", "");
					Integer key = map.get(suffix);
					if (key!=null) {
						prep.setInt(1, conceptid);
						prep.setInt(2, key);
						prep.execute();
					}
				}
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		loader.conn.commit();
	}
	
	private static void executeSQL(CSVtoSQLite loader, String sql) throws SQLException {
		System.out.print(sql + " ... ");
		long time = System.currentTimeMillis();
		loader.stat.execute(sql);
		System.out.println("done (" + (System.currentTimeMillis()-time) + " milliseconds)");
	}
}
