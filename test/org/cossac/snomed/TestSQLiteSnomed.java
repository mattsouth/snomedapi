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

import org.cossac.snomed.db.SQLiteSnomed;

import junit.framework.TestCase;

public class TestSQLiteSnomed extends TestCase {
	static String FILEPATH_SQLITE = "./etc/snomed.sqlite";
	
	Snomed impl;
	
	public void setUp() throws ClassNotFoundException, SQLException, IOException {
		impl = new SQLiteSnomed(FILEPATH_SQLITE);
	}

	public void testConcepts() {
		Set<Long> concepts = impl.getConceptIds();
		System.out.println(concepts.size() + " Concepts");
		assertEquals(true, concepts.size()>400000);
		concepts = impl.getConceptIds(Concept.Status.CURRENT);
		System.out.println(concepts.size() + " live Concepts");
		assertEquals(true, concepts.size()>300000);
		concepts = impl.getConceptIds("injection");
		System.out.println(concepts.size() + " injection Concepts");
		assertEquals(true, concepts.size()>4000);
		concepts = impl.getConceptIds(true);
		System.out.println(concepts.size() + " primitive Concepts");
		assertEquals(true, concepts.size()>300000);
	}
	
	public void testDescriptions() {
		Set<Long> descriptions = impl.getDescriptionIds();
		System.out.println(descriptions.size() + " Descriptions");
		assertEquals(true, descriptions.size()>1000000);		
		descriptions = impl.getDescriptionIds("injection");
		System.out.println(descriptions.size() + " injection Descriptions");
		assertEquals(true, descriptions.size()>15000);
		descriptions = impl.getDescriptionIdsByStatus(Description.Status.CURRENT);
		System.out.println(descriptions.size() + " live Descriptions");
		assertEquals(true, descriptions.size()>800000);
		descriptions = impl.getDescriptionIdsByType(Description.Type.SYNONYM);
		System.out.println(descriptions.size() + " synonyms");
		assertEquals(true, descriptions.size()>200000);
	}
	
	public void testRelationships() {
		Set<Long> relationshipsIds = impl.getRelationshipIds();
		System.out.println(relationshipsIds.size() + " Relationships");
		assertEquals(true, relationshipsIds.size()>1500000);		
		relationshipsIds = impl.getRelationshipTypeIds(); // NB this is a slow query
		System.out.println(relationshipsIds.size() + " Relationship types");
		assertEquals(true, relationshipsIds.size()>60);
		long type = relationshipsIds.toArray(new Long[] {})[0];
		relationshipsIds = impl.getRelationshipIds(type);
		System.out.println(relationshipsIds.size() + " Relationships of type: " + impl.getConcept(type).getFullySpecifiedName());
		assertEquals(true, relationshipsIds.size()>70000);
		Set<Relationship> relationships = impl.getRelationships(type);
		System.out.println(relationships.size() + " Relationships associated with " + impl.getConcept(type).getFullySpecifiedName());
		assertEquals(1, relationships.size());		
	}
	
	public void testConcept() {
		Concept concept = impl.getConcept(100000000);
		assertEquals(100000000,concept.getId());
		assertEquals(Concept.Status.MOVED_ELSEWHERE, concept.getStatus());
		assertEquals("BITTER-3 (product)", concept.getFullySpecifiedName());
		assertEquals("XU000",concept.getCtv3Id());
		assertEquals("C-D1619", concept.getSnomedId());
		assertEquals(true, concept.isPrimitive());
	}
	
	public void testRelationship() {
		Relationship relationship = impl.getRelationship(100000028);
		assertEquals(100000028, relationship.getId());
		assertEquals(280844000, relationship.getSourceConceptId());
		assertEquals(116680003, relationship.getTypeId());
		assertEquals(71737002, relationship.getTargetConceptId());
		assertEquals(Relationship.Characteristic.DEFINING,relationship.getCharacteristic());
		assertEquals(Relationship.Refinability.NOT_REFINABLE,relationship.getRefinability());
		assertEquals(0,relationship.getGroup());
	}
	
	public void testDescription() {
		Description description = impl.getDescription(1473011013);
		assertEquals(1473011013, description.getId());
		assertEquals(Description.Status.CONCEPT_NON_CURRENT, description.getStatus());
		assertEquals(100000000, description.getConceptId());
		assertEquals("BITTER-3 (product)",description.getTerm());
		assertEquals(true,description.isInitialCapitalStatus());
		assertEquals(Description.Type.FULLY_SPECIFIED_NAME,description.getType());
		assertEquals("en", description.getLanguageCode());
	}
}
