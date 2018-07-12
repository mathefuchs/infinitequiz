package com.fuchs.infinitequiz.model;

/**
 * This class represents a EnglishDataSet.
 *
 * @author Tobias Fuchs
 * @version 11/27/16
 */
public class EnglishDataSet extends DataSet {
    /**
     * The amount of possible combinations of verbs, articles, tenses ...
     */
    public static final int Combinations = 12;

    /**
     * Initializes a new EnglishDataSet.
     *
     * @param objectPrefix      the object prefix
     * @param object            the object
     * @param verb              the verb
     * @param descriptionPrefix the description prefix
     * @param description       the description
     */
    public EnglishDataSet(final String objectPrefix, final String object, final String verb, final String descriptionPrefix,
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
        if (this.getVerb() != null) {
            int code = 0;

            if (this.getVerb().equals("are")) {
                code = 3;
            } else if (this.getVerb().equals("was")) {
                code = 6;
            } else if (this.getVerb().equals("were")) {
                code = 9;
            }

            if (this.getDescriptionPrefix() != null) {
                if (this.getDescriptionPrefix().equals("the")) {
                    code += 1;
                } else if (this.getDescriptionPrefix().equals("a")
                        || this.getDescriptionPrefix().equals("an")) {
                    code += 2;
                }
            }

            return code;
        } else {
            return super.hashCode();
        }
    }
}
