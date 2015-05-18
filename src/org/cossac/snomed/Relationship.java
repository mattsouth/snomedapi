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
	public abstract long getSourceConceptId();

	/**
	 * The concept id that types the relationship
	 * 
	 * @return
	 */
	public abstract long getTypeId();

	/**
	 * target concept id of the relationship.
	 * 
	 * @return
	 */
	public abstract long getTargetConceptId();

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
	public abstract int getCharacteristic();

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