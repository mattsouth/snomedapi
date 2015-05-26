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
 * Relationships link concepts in SNOMED CT with triples of the form:
 * Source Concept is related to Target Concept by Type Concept.
 *
 * Relationship characteristic, refinability and group can be further used to understand the
 * nature of a relationship and whether the source concept is primitive or defined.
 **/
public interface Relationship {
	
	/**
	 * A unique ID for every relationship.
	 */
	long getId();

	/**
	 * Source Concept ID of the relationship.
	 */
	long getSourceConceptId();

	/**
	 * The Concept ID that types the relationship
	 */
	long getTypeId();

	/**
	 * Target Concept ID of the relationship.
	 */
	long getTargetConceptId();

    /**
     * An indication of whether a Relationship specifies a defining characteristic of the source Concept or
     * a possible qualification of that Concept.
     */
    enum Characteristic {
		DEFINING(0), QUALIFIER(1), HISTORICAL(2), ADDITIONAL(4);

		final int codeValue;

		Characteristic(int codeValue) {
			this.codeValue = codeValue;
		}

		public static Characteristic enumFromValue(int code) {
			switch (code) {
				case 0:
					return DEFINING;
				case 1:
					return QUALIFIER;
				case 2:
					return HISTORICAL;
				case 4:
					return ADDITIONAL;
				default:
					return null;
			}
		}
	}

	Characteristic getCharacteristic();

    /**
     * An indication of whether it is possible to refine the target concept when this Relationship is used
     * as a template for clinical data entry.
     */
	enum Refinability {
		NOT_REFINABLE(0), OPTIONAL(1), MANDATORY(2);

		final int codeValue;

		Refinability(int codeValue) {
			this.codeValue = codeValue;
		}

		public static Refinability enumFromValue(int code) {
			switch(code) {
				case 0:
					return NOT_REFINABLE;
				case 1:
					return OPTIONAL;
				case 2:
					return MANDATORY;
				default:
					return null;
			}
		}
	}

	Refinability getRefinability();

	/**
	 * An integer value that expresses an association between two or more Relationships.
	 * 
	 * The default Relationship group value is zero and this applies to all Relationships that have not been stated
	 * to be associated with any other Relationships. All Relationships that share the same ConceptId1 and the
	 * same non-zero Relationship group value are associated with one another. Any Relationships that share the
	 * same ConceptId1 but have different Relationship group values are not associated with one another. 
	 */
	int getGroup();
}