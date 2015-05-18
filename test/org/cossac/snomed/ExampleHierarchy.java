package org.cossac.snomed;
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
