package org.cossac.snomed.loader;
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * A class that creates a SQLite database from a list of CSV files provided by a meta file.
 * 
 * Created for the SNOMED TRUD dump.  Assumes: 
 * * a tab delimiter in the consumed meta and data CSV files.
 * * first column needs a primary key
 * 
 * @author matt
 *
 */
public class CSVtoSQLite {
	protected String metaFilepath = null;
	protected String dbFilepath = null;
	protected List<Table> meta = null;
	protected String DELIMITER_TAB = "\\t";
	protected String COMMENT_INDICATOR = "#";
	Connection conn = null;
	Statement stat = null;
	String delimiter = DELIMITER_TAB;
	
	public CSVtoSQLite(String metaFilepath, String dbFilepath) throws IOException, ClassNotFoundException, SQLException {
		super();
		this.metaFilepath = metaFilepath;
		this.dbFilepath = dbFilepath;
		loadMeta();
	    Class.forName("org.sqlite.JDBC");
	    this.conn = DriverManager.getConnection("jdbc:sqlite:" + dbFilepath);
	    conn.setAutoCommit(false);
	    this.stat = conn.createStatement();
	}
	
	public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {
		CSVtoSQLite loader = new CSVtoSQLite(args[0], args[1]);
		loader.generate();
	}
	
	private void loadMeta() throws IOException {
		meta = new ArrayList<Table>();
		File file = new File(metaFilepath);
		FileReader reader = new FileReader(file);
		BufferedReader buff = new BufferedReader(reader);
		String line;
		while ((line = buff.readLine()) != null) {
			if (!line.startsWith(COMMENT_INDICATOR)) {
				meta.add(new Table(line.split("\\t")));				
			}
		}
		buff.close();
		reader.close();
	}
	
	public void generate() throws ClassNotFoundException, SQLException, IOException {
		for (Table table : meta) {
			if (table.ok) {
				// open CSV file
				File file = new File(table.filepath);
				FileReader reader = new FileReader(file);
				BufferedReader buff = new BufferedReader(reader);
				// ignore first line (column headings)
				String line = buff.readLine();
			    stat.executeUpdate("drop table if exists " + table.name + ";");
			    System.out.println(table.sqlCreate);
				stat.executeUpdate(table.sqlCreate);
				// process data
				System.out.print("Adding " + table.name + " data ... ");
				long time = System.currentTimeMillis();
			    PreparedStatement prep = conn.prepareStatement(table.sqlInsert);
			    int lineno=0;
				while((line=buff.readLine())!=null) {
					if (line.length()>0) {
						try {
							String[] values = line.trim().split(delimiter);
							for (int i=0; i<values.length; i++) {
									switch(table.types[i]) {
									case BOOLEAN: prep.setBoolean(i+1, values[i].equals("1")); break;
									case INT: prep.setInt(i+1, new Integer(values[i])); break; 
									case STRING: prep.setString(i+1, values[i]); break;
									case LONG: prep.setLong(i+1, new Long(values[i])); break;
									}
							}
							prep.execute();
							lineno++;
						} catch (Exception e) {
							System.err.println("Error occurred on line: " + lineno);
							e.printStackTrace();
							System.exit(1);
						}
					}
				}
				conn.commit();
				System.out.println("done (" + (System.currentTimeMillis()-time) + " milliseconds)");
				// close file
				buff.close();
				reader.close();
			} else {				
				System.out.println("problem with meta data for " + table.name + " table");				
			}
		}
	}
}

/**
 * Each line of the meta data file points to a CSV file that will have a table in the generated database.
 * 
 * It assumed that the first line in each data CSV file contains the column headings.
 * 
 * Each meta line consists of tab delimited values where the first value is the name of the table, the second
 * the location of the CSV file and all subsequent values correspond to the type of the table's columns.
 */
class Table {
	String filepath=null;
	String name=null;
	Type[] types=null;
	boolean ok=false;
	String sqlCreate=null;
	String sqlInsert=null;
	
	Table(String[] meta) throws IOException {
		super();
		if (meta.length>2) {
			this.name = meta[0];
			this.filepath = meta[1];
			File file = new File(filepath);
			if (new File(filepath).exists()) {
				this.types = new Type[meta.length-2];
				for (int i=0; i<meta.length-2; i++) {
					Type type = Type.valueOf(meta[i+2].toUpperCase()); 
					types[i] = type;
				}
				// examine first line of meta file for details of create table statement.
				BufferedReader buff = new BufferedReader(new FileReader(file));
				// process first line (column headings)
				String line = buff.readLine();
			    this.sqlCreate = "create table " + name + " (";
			    this.sqlInsert = "insert into " + name + " values (";
			    String[] columns = line.split("\\t");
				for (int i=0; i<columns.length; i++) {
					sqlInsert = sqlInsert + "?";
					sqlCreate = sqlCreate + "'" + columns[i] + "'";
					if (i==0) sqlCreate = sqlCreate + " PRIMARY KEY";
					if (i<(columns.length-1)) {
						sqlInsert = sqlInsert + ", ";
						sqlCreate = sqlCreate + ", ";
					}
				}
				sqlCreate = sqlCreate + ");";				
				sqlInsert = sqlInsert + "); ";
				buff.close();
				ok = true;
			} // ok=false
		} // ok=false
	}
}

/**
 * These are the types I've used to map columns in the TRUD CSVs.  Longs are only used as needed as they take up twice as much RAM.
 * 
 * @author matt
 *
 */
enum Type {
	INT, LONG, STRING, BOOLEAN;
}