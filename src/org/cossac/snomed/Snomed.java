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
import java.util.Set;

/**
 * Provides accessors for SNOMED clinical terms (SNOMED_CT).
 * 
 * @author matt
 *
 */
public interface Snomed {
	
	// Concepts
	
	public abstract Set<Long> getConceptIds();
	
	public abstract Concept getConcept(long conceptId);
	
	public abstract Set<Long> getConceptIds(int statusId); // needs getStatusIds?
	
	public abstract Set<Long> getConceptIds(String match);
	
	public abstract Set<Long> getConceptIds(boolean isPrimitive);
	
	// Descriptions
	
	/**
	 * @return *all* descriptionIds... Gulp.
	 */
	public abstract Set<Long> getDescriptionIds();
	
	/**
	 * @param id 
	 * @return description from id
	 */
	public abstract Description getDescription(long id);
	
	/**
	 * Search for matching description terms
	 * @param match partial match for description term
	 * @return matching descriptions
	 */
	public abstract Set<Long> getDescriptionIds(String match);
	
	/**
	 * 
	 * @param status
	 * @return
	 */
	public abstract Set<Long> getDescriptionIdsByStatus(int status);
	
	/**
	 * 
	 * @param type
	 * @return
	 */
	public abstract Set<Long> getDescriptionIdsByType(int type);
	
	// Relationships
	
	/**
	 * @return *All* RelationshipIds...  Gulp.
	 */
	public abstract Set<Long> getRelationshipIds();
	
	/**
	 * @return relationship from id
	 */
	public abstract Relationship getRelationship(long id);

	/**
	 * @return All conceptIds that are relationship typeIds.
	 */
	public abstract Set<Long> getRelationshipTypeIds();

	/**
	 * @return All RelationshipIds of a particular type.
	 */
	public abstract Set<Long> getRelationshipIds(long typeId);

	/**
	 * @param conceptID identifies target concept
	 * @return all Relationships associated with a particular concept.
	 */
	public Set<Relationship> getRelationships(long conceptID);
	
}
