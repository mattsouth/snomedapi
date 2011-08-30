package org.cossac.snomed;

import java.sql.SQLException;
import java.util.Set;

import org.cossac.snomed.Concept;
import org.cossac.snomed.Relationship;
import org.cossac.snomed.Snomed;
import org.cossac.snomed.Concept.Direction;
import org.cossac.snomed.db.SQLiteSnomed;


/**
 * Example that prints out the top level concepts.
 * 
 * @author matt
 *
 */
public class ExampleTopLevel {
	static int CONCEPT_ID_OBJECT = 138875005;
	static int CONCEPT_ID_ISA_RELATIONSHIP = 116680003;
	
	static String FILEPATH_SQLITE = "./etc/snomed.sqlite";

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		Snomed snomed = new SQLiteSnomed(FILEPATH_SQLITE);
		Concept root = snomed.getConcept(CONCEPT_ID_OBJECT);
		Set<Relationship> children = root.getRelationships(CONCEPT_ID_ISA_RELATIONSHIP, Direction.INWARDS);
		for (Relationship relation : children ) {
			Concept child = snomed.getConcept(relation.getSourceConceptId());
			System.out.println(child.getId() + " :: " + child.getFullySpecifiedName());
		}
	}
}
