package org.cossac.snomed;

import java.util.Set;

/**
 * A “concept” is a clinical meaning identified by a unique numeric identifier
 * (ConceptId) that never changes. Concepts are represented by a unique human-readable Fully Specified
 * Name (FSN). The concepts are formally defined in terms of their relationships with other concepts. These
 * logical definitions give explicit meaning which a computer can process and query on. Every concept also has
 * a set of terms that name the concept in a human-readable way.
 * 
 * @author matt
 */
public interface Concept {

	/** 
	 * A permanent unique numeric identifier which does not convey any information about the meaning 
	 * or nature of the Concept.
	 * @return
	 */
	public abstract int getId();

	/**
	 * This field flags concepts that have been retired so that data encoded with these concepts can be 
	 * properly accessed and retrieved long after it has been coded.  A value of 0 (Current) indicates that
	 * the concept is in active use.
	 * 
	 * 0 - Current
	 * 1 - Retired
	 * 2 - Duplicate
	 * 3 - Outdated
	 * 4 - Ambiguous
	 * 5 - Erroneous
	 * 6 - Limited
	 * 10 - Moved Elsewhere
	 * 11 - Pending Move
	 * 
	 * @return
	 */
	public abstract int getStatus();

	/**
	 * A human readable name for the concept.  
	 * Note that this field is replicated in Description.
	 * 
	 * @return
	 */
	public abstract String getFullySpecifiedName();

	/**
	 * Original Clinical Terms v3 identifier.
	 * 
	 * @return
	 */
	public abstract String getCtv3Id();

	/**
	 * Original SNOMED RT identifier.
	 * 
	 * @return
	 */
	public abstract String getSnomedId();

	/**
	 * Indicates whether or not a concept has been flagged as primitive during the modelling
	 * process. This flag can be useful in advanced applications that take advantage of the description logic
	 * features of SNOMED CT.
	 * 
	 * @return true if concept is primitive
	 */
	public abstract boolean isPrimitive();
	
	/**
	 * A concept maybe associated with more than one description.
	 * @return descriptions associated with this concept
	 */
	public abstract Set<Description> getDescriptions();
	
	/**
	 * Return the relationship types that link to this concept.
	 * @param direction
	 * @return
	 */
	public abstract Set<Integer> getRelationshipTypeIds(Direction direction);

	public abstract Set<Relationship> getRelationships(int typeId, Direction direction);
	
	/**
	 * A relationship defines a directed edge in a graph of concepts.  When one is filtering the list of relationships 
	 * by concept, then it is useful to define a filter based on inwards or outwards relations.
	 * 
	 * @author matt
	 */
	public enum Direction {
		INWARDS, OUTWARDS;
	}

}