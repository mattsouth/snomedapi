package org.cossac.snomed;

/**
 * Relationships link concepts in SNOMED CT. There are four types of relationships that can be assigned to
 * concepts in SNOMED CT:
 * • Defining
 * • Qualifying
 * • Historical
 * • Additional
 **/
public interface Relationship {
	
	/**
	 * A unique ID for every relationship.
	 * 
	 * @return
	 */
	public abstract long getId();

	/**
	 * Source concept id of the relationship.
	 * 
	 * @return
	 */
	public abstract int getSourceConceptId();

	/**
	 * The concept id that types the relationship
	 * 
	 * @return
	 */
	public abstract int getTypeId();

	/**
	 * target concept id of the relationship.
	 * 
	 * @return
	 */
	public abstract int getTargetConceptId();

	/**
	 * An indication of whether a Relationship specifies a defining characteristic of the source Concept or 
	 * a possible qualification of that Concept.
	 * 
	 * 0 - Defining
	 * 1 - Qualifier
	 * 2 - Historical
	 * 3 - Additional
	 * 
	 * @return
	 */
	public abstract int getChatacteristic();

	/**
	 * An indication of whether it is possible to refine the target concept when this Relationship is used 
	 * as a template for clinical data entry.
	 * 
	 * 0 - Not refinable
	 * 1 - Optional
	 * 2 - Mandatory
	 * 
	 * @return
	 */
	public abstract int getRefinability();

	/**
	 * An integer value that expresses an association between two or more Relationships.
	 * 
	 * The default Relationship group value is zero and this applies to all Relationships that have not been stated
	 * to be associated with any other Relationships. All Relationships that share the same ConceptId1 and the
	 * same non-zero Relationship group value are associated with one another. Any Relationships that share the
	 * same ConceptId1 but have different Relationship group values are not associated with one another. 
	 * 
	 * @return
	 */
	public abstract int getGroup();
}