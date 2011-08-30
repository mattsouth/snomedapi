package org.cossac.snomed;

import java.util.Set;

/**
 * Provides accessors for SNOMED clinical terms (SNOMED_CT).
 * 
 * @author matt
 *
 */
public interface Snomed {
	
	// Concepts
	
	public abstract Set<Integer> getConceptIds();
	
	public abstract Concept getConcept(int conceptId);
	
	public abstract Set<Integer> getConceptIds(int statusId); // needs getStatusIds?
	
	public abstract Set<Integer> getConceptIds(String match);
	
	public abstract Set<Integer> getConceptIds(boolean isPrimitive);
	
	// Descriptions
	
	/**
	 * @return *all* descriptionIds... Gulp.
	 */
	public abstract Set<Long> getDescriptionIds();
	
	/**
	 * @param id 
	 * @return description from id
	 */
	public abstract Description getDescription(Long id);
	
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
	public abstract Set<Integer> getRelationshipTypeIds();

	/**
	 * @return All RelationshipIds of a particular type.
	 */
	public abstract Set<Long> getRelationshipIds(int typeId);

	/**
	 * @param conceptID identifies target concept
	 * @return all Relationships associated with a particular concept.
	 */
	public Set<Relationship> getRelationships(int conceptID);
	
}
