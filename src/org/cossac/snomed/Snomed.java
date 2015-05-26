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
 * Main API accessor for SNOMED clinical terms (SNOMED_CT) in which {@link Concept}s have multiple {@link Description}s and are linked by different {@link Relationship}s.
 * <p/>
 * This class provides accessors to collections of object IDs or to individual objects via their ID.
 *
 * @author matt
 */
public interface Snomed {

    // Concepts

    /**
     * Get *all* (400K+) concept IDs
     */
    Set<Long> getConceptIds();

    Concept getConcept(long conceptId);

    /**
     * Get current/outdated etc concept IDs
     */
    Set<Long> getConceptIds(Concept.Status statusId);

    /**
     * Get concept IDs using partial string match to fully specified name.
     */
    Set<Long> getConceptIds(String match);

    /**
     * Get primitive / derived concept ids.
     */
    Set<Long> getConceptIds(boolean isPrimitive);

    // Descriptions

    /**
     * Get *all* (1.2M+) description IDs ...
     */
    Set<Long> getDescriptionIds();

    Description getDescription(long id);

    /**
     * Get description IDs using partial string match to description term
     */
    Set<Long> getDescriptionIds(String match);

    /**
     * Get current/outdated etc description IDs
     */
    Set<Long> getDescriptionIdsByStatus(Description.Status status);

    /**
     * Get Preferred Name or Synonym description IDs
     */
    Set<Long> getDescriptionIdsByType(Description.Type type);

    // Relationships

    /**
     * Get *All* (1.3M+) Relationship IDs
     */
    Set<Long> getRelationshipIds();

    Relationship getRelationship(long id);

    /**
     * Get all Concept IDs that are relationship types.
     */
    Set<Long> getRelationshipTypeIds();

    /**
     * Get all Relationship IDs of a particular type.
     */
    Set<Long> getRelationshipIds(long typeId);

    /**
     * Get all Relationships associated with a particular concept.
     */
    Set<Relationship> getRelationships(long conceptID);

}
