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
