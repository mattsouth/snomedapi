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
 * Descriptions are the terms or names assigned to a SNOMED CT {@link Concept}. “Term” in this context
 * means a phrase used to name a concept. Every Concept should have a Fully Qualified Name description,
 * a preferred name description and may have multiple synonym descriptions.
 *
 * @author matt
 */
public interface Description {

    /**
     * A unique ID for every description.
     */
    long getId();

    /**
     * The status of a Description indicates whether it is in active use and, if not indicates the reason for withdrawal from current use.
     * <p/>
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
     * <p/>
     * * - not used in july 2010 version.
     * <p/>
     * Note
     * A Description is only marked as current (DescriptionStatus = 0) if the associated Concept is also current
     * (ConceptStatus = 0).
     * A Concept made inactive (with ConceptStatus = 1, 2, 3, 4, 5 or 10) retains its previous current Descriptions
     * but these are marked " Concept non-current" (DescriptionStatus = 8).
     * An inactive"limited" Concept (ConceptStatus = 6) has valid usable Descriptions but these are also marked
     * "limited" (DescriptionStatus = 6).
     */
    enum Status {
        CURRENT(0), NON_CURRENT(1), DUPLICATE(2), OUTDATED(3), ERRONEOUS(5), LIMITED(6), INAPPROPRIATE(7), CONCEPT_NON_CURRENT(8), MOVED_ELSEWHERE(10);

        public final int codeValue;

        Status(int codeValue) {
            this.codeValue = codeValue;
        }

        public static Status enumFromValue(int codeValue) {
            switch (codeValue) {
                case 0:
                    return CURRENT;
                case 1:
                    return NON_CURRENT;
                case 2:
                    return DUPLICATE;
                case 3:
                    return OUTDATED;
                case 5:
                    return ERRONEOUS;
                case 6:
                    return LIMITED;
                case 7:
                    return INAPPROPRIATE;
                case 8:
                    return CONCEPT_NON_CURRENT;
                case 10:
                    return MOVED_ELSEWHERE;
                default:
                    return null;
            }
        }

    }

    /**
     * Status is an indication of active use.
     */
    Status getStatus();

    /**
     * The unique SNOMED CT Identifier of the associated Concept.
     */
    long getConceptId();

    /**
     * This descriptions's text.
     */
    String getTerm();

    /**
     * An indication of whether the capitalization status of the first character of the Term
     * is significant.  If true the capitalization of the first character of the Term must not
     * be changed.
     */
    boolean isInitialCapitalStatus();

    /**
     * Indicates if description is one of three types: Fully Specified Name, Preferred Term or Synonym.
     */
    enum Type {
        UNSPECIFIED(0), PREFERRED(1), SYNONYM(2), FULLY_SPECIFIED_NAME(3);

        public final int codeValue;

        Type(int codeValue) {
            this.codeValue = codeValue;
        }

        public static Type enumFromValue(int codeValue) {
            switch (codeValue) {
                case 0:
                    return UNSPECIFIED;
                case 1:
                    return PREFERRED;
                case 2:
                    return SYNONYM;
                case 3:
                    return FULLY_SPECIFIED_NAME;
                default:
                    return null;
            }
        }
    }

    /**
     * Unspecified, Fully Specified Name, Preferred Term or Synonym.
     */
    Type getType();

    /**
     * An indication of a Language or Dialect in which this Description is valid. The
     * language or dialect subset ultimately defines the descriptions for each concept.
     */
    String getLanguageCode();

}