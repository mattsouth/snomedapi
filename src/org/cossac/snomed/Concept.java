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
     */
    long getId();

    enum Status {
        CURRENT(0), RETIRED(1), DUPLICATE(2), OUTDATED(3), ERRONEOUS(5), LIMITED(6), MOVED_ELSEWHERE(10), PENDING_MOVE(11);

        public final int codeValue;

        Status(int codeValue) {
            this.codeValue = codeValue;
        }

        public static Status enumFromValue(int codeValue) {
            switch (codeValue) {
                case 0:
                    return CURRENT;
                case 1:
                    return RETIRED;
                case 2:
                    return DUPLICATE;
                case 3:
                    return OUTDATED;
                case 5:
                    return ERRONEOUS;
                case 6:
                    return LIMITED;
                case 10:
                    return MOVED_ELSEWHERE;
                case 11:
                    return PENDING_MOVE;
                default:
                    return null;
            }
        }
    }

    /**
     * 0 - Current
     * 1 - Retired
     * 2 - Duplicate
     * 3 - Outdated
     * 4 - Ambiguous
     * 5 - Erroneous
     * 6 - Limited
     * 10 - Moved Elsewhere
     * 11 - Pending Move
     */
    Status getStatus();

    /**
     * A human readable name for the concept.
     * Note that this field is replicated in Description.
     */
    String getFullySpecifiedName();

    /**
     * Original Clinical Terms v3 identifier.
     */
    String getCtv3Id();

    /**
     * Original SNOMED RT identifier.
     */
    String getSnomedId();

    /**
     * Indicates whether or not a concept has been flagged as primitive during the modelling
     * process. This flag can be useful in advanced applications that take advantage of the description logic
     * features of SNOMED CT.
     *
     * @return true if concept is primitive
     */
    boolean isPrimitive();

    /**
     * A concept maybe associated with more than one description.
     *
     * @return descriptions associated with this concept
     */
    Set<Description> getDescriptions();

    /**
     * Return the relationship types that link to this concept.
     *
     * @param direction
     */
    Set<Long> getRelationshipTypeIds(Direction direction);

    Set<Relationship> getRelationships(long typeId, Direction direction);

    /**
     * A relationship defines a directed edge in a graph of concepts.  When one is filtering the list of relationships
     * by concept, then it is useful to define a filter based on inwards or outwards relations.
     *
     * @author matt
     */
    enum Direction {
        INWARDS, OUTWARDS;
    }

}