package org.cossac.snomed;

/**
 * Concept descriptions are the terms or names assigned to a SNOMED CT concept. “Term” in this context
 * means a phrase used to name a concept. A unique DescriptionId identifies a description. Multiple descriptions
 * might be associated with a concept identified by a ConceptId.
 *
 * @author matt
 *
 */
public interface Description {

	/**
	 * A unique ID for every description.
	 * 
	 * @return
	 */
	public abstract long getId();

	/**
	 * The status of a Description indicates whether it is in active use and, if not,
	 * indicates the reason for withdrawal from current use.
	 * 
	 * 0 - current
	 * 1 - Non-Current
	 * 2* - Duplicate
	 * 3* - Outdated
	 * 5* - Erroneous
	 * 6 - Limited
	 * 7 - Inappropriate
	 * 8 - Concept non-current
	 * 10* - Moved Elsewhere
	 * 11* - Pending Move
	 * 
	 * * - not used in july 2010 version.
	 * 
	 * Note
	 * A Description is only marked as current (DescriptionStatus = 0) if the associated Concept is also current
(ConceptStatus = 0).
	 * A Concept made inactive (with ConceptStatus = 1, 2, 3, 4, 5 or 10) retains its previous current Descriptions
but these are marked " Concept non-current" (DescriptionStatus = 8).
	 * An inactive"limited" Concept (ConceptStatus = 6) has valid usable Descriptions but these are also marked
"limited" (DescriptionStatus = 6).
     *
	 * @return
	 */
	public abstract int getStatus();

	/**
	 * The unique SNOMED CT Identifier of the associated Concept.
	 * 
	 * @return
	 */
	public abstract int getConceptId();

	/**
	 * The text of a Term used to describe the associated Concept.
	 * 
	 * @return
	 */
	public abstract String getTerm();

	/**
	 * An indication of whether the capitalization status of the first character of the Term
	 * is significant.  If true the capitalization of the first character of the Term must not
	 * be changed.
	 * 
	 * @return
	 */
	public abstract boolean isInitialCapitalStatus();

	/**
	 * Indicates if field is one of three types: Fully Specified Name, Preferred Term or Synonym.
	 * 
	 * 0 - Unspecified
	 * 1 - Preferred
	 * 2 - Synonym
	 * 3 - FullySpecifiedName
	 *  
	 * @return
	 */
	public abstract int getType();

	/**
	 * An indication of a Language or Dialect in which this Description is valid. The
	 * language or dialect subset ultimately defines the descriptions for each concept.
	 * 
	 * @return
	 */
	public abstract String getLanguageCode();

}