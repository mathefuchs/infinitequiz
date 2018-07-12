package com.fuchs.infinitequiz.model;

/**
 * This class represents a GermanDataSet.
 *
 * @author Tobias Fuchs
 * @version 11/27/16
 */
public class GermanDataSet extends DataSet {
    /**
     * The amount of possible combinations of verbs, articles, tenses ...
     */
    public static final int Combinations = 20;

    /**
     * Initializes a new GermanDataSet.
     *
     * @param objectPrefix      the object prefix
     * @param object            the object
     * @param verb              the verb
     * @param descriptionPrefix the description prefix
     * @param description       the description
     */
    public GermanDataSet(final String objectPrefix, final String object, final String verb, final String descriptionPrefix,
                         final String description) {
        super(objectPrefix, object, verb, descriptionPrefix, description);
    }

    /**
     * Returns an identifier of the object.
     *
     * @return an identifier of the object
     */
    @Override
    public int hashCode() {
        if (this.getVerb() != null && this.getDescriptionPrefix() != null) {
            int code = 0;

            if (this.getVerb().equals("sind")) {
                code = 5;
            } else if (this.getVerb().equals("war")) {
                code = 10;
            } else if (this.getVerb().equals("waren")) {
                code = 15;
            }

            if (this.getDescriptionPrefix().equals("die")) {
                code += 1;
            } else if (this.getDescriptionPrefix().equals("das")) {
                code += 2;
            } else if (this.getDescriptionPrefix().equals("ein")) {
                code += 3;
            } else if (this.getDescriptionPrefix().equals("eine")) {
                code += 4;
            }

            return code;
        } else {
            return super.hashCode();
        }
    }
}
