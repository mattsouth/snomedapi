package org.cossac.snomed;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Set;

import org.cossac.snomed.Concept;
import org.cossac.snomed.Relationship;
import org.cossac.snomed.Snomed;
import org.cossac.snomed.Concept.Direction;
import org.cossac.snomed.db.SQLiteSnomed;


/**
 * An example walking of the concept is-a hierarchy starting from "Gamma Ray Therapy". 
 * 
 * @author matt
 *
 */
public class ExampleHierarchy {
	static Long CONCEPT_ID_GAMMA_RAY_THERAPY = new Long(65952009);
	static Long CONCEPT_ID_ISA_RELATIONSHIP = new Long(116680003);
	
	static String FILEPATH_SQLITE = "./etc/snomed.sqlite";
	
	Snomed impl;
	
	public ExampleHierarchy(Snomed impl) {
		super();
		this.impl = impl;
	}
	
	
	public void showHierarchy(Long conceptId, Long relationshipConceptId) throws SQLException {
		long time = System.currentTimeMillis();
		showHierarchy(conceptId, relationshipConceptId, 0);
		System.out.println("completed in " + (System.currentTimeMillis()-time) + " milliseconds");
	}

	protected void showHierarchy(Long conceptId, Long relationshipConceptId, int depth) throws SQLException {
		StringBuffer buff = new StringBuffer();
		for (int i=0; i<depth; i++) {
			buff.append("\t");
		}
		Concept concept = impl.getConcept(conceptId);
		buff.append(concept.getId());
		buff.append(" :: ");
		buff.append(concept.getFullySpecifiedName());
		System.out.println(buff.toString());
		Set<Relationship> rels = concept.getRelationships(relationshipConceptId, Direction.OUTWARDS);
		if (rels!=null) {
			for (Relationship rel : rels) {
				showHierarchy(rel.getTargetConceptId(), relationshipConceptId, depth+1);
			}			
		}
	}

	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
		ExampleHierarchy example = new ExampleHierarchy(new SQLiteSnomed(FILEPATH_SQLITE));
		example.showHierarchy(CONCEPT_ID_GAMMA_RAY_THERAPY, CONCEPT_ID_ISA_RELATIONSHIP);
	}
}
