package org.cossac.snomed;

import org.cossac.snomed.db.SQLiteSnomed;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

/**
 * Check that all concepts have a FSN and a PN.
 */
public class ExampleCheckDescriptions {

    static String FILEPATH_SQLITE = "./etc/snomed.sqlite";
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Snomed snomed = new SQLiteSnomed(FILEPATH_SQLITE);
        Set<Long> conceptIDs = snomed.getConceptIds();
        long currentConcepts = 0L;
        long missingFSN = 0L;
        long mismatchFSN =0L;
        long missingPN=0L;
        long missingBoth=0L;
        for (Long conceptID: conceptIDs) {
            Concept concept = snomed.getConcept(conceptID);
            if (concept.getStatus()==Concept.Status.CURRENT) {
                currentConcepts++;
                Set<Description> descs = concept.getDescriptions();
                boolean hasFSN = false;
                boolean hasPN = false;
                Set<String> fsns = new HashSet<String>();
                for (Description desc : descs) {
                    if (desc.getType() == Description.Type.FULLY_SPECIFIED_NAME && desc.getStatus() == Description.Status.CURRENT) {
                        hasFSN = true;
                        fsns.add(desc.getTerm());
                        if (!desc.getTerm().equals(concept.getFullySpecifiedName())) {
                            mismatchFSN++;
                        }
                    }
                    if (desc.getType() == Description.Type.PREFERRED && desc.getStatus() == Description.Status.CURRENT)
                        hasPN = true;
                }
                if (fsns.size() > 1) {
                    System.out.println("multiple fsns: " + concept.getId() + " :: " + fsns.toString());
                }
                if (!hasFSN) {
                    missingFSN++;
                }
                if (!hasPN) {
                    missingPN++;
                }
                if (!(hasFSN && hasPN)) {
                    missingBoth++;
                }
            }
        }
        System.out.println("total concepts: " + conceptIDs.size());
        System.out.println("current concepts: " + currentConcepts);
        System.out.println("missingFSN: " + missingFSN);
        System.out.println("missingPN: " + missingPN);
        System.out.println("mismatchFSN: " + mismatchFSN);
        System.out.println("missingBoth: " + missingBoth);
    }
}
