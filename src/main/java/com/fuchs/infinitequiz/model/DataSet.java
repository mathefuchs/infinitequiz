package com.fuchs.infinitequiz.model;

import com.fuchs.infinitequiz.generator.Configuration;

/**
 * This abstract class represents a DataSet.
 *
 * @author Tobias Fuchs
 * @version 09/07/16
 */
public abstract class DataSet {
    private String objectPrefix;
    private String object;
    private String verb;
    private String descriptionPrefix;
    private String description;

    /**
     * Initializes a new DataSet.
     *
     * @param objectPrefix      the object prefix
     * @param object            the object
     * @param verb              the verb
     * @param descriptionPrefix the description prefix
     * @param description       the description
     */
    public DataSet(String objectPrefix, String object, String verb, String descriptionPrefix, String description) {
        this.objectPrefix = objectPrefix;
        this.object = object;
        this.verb = verb;
        this.descriptionPrefix = descriptionPrefix;
        this.description = description;
    }

    /**
     * Returns the objectPrefix.
     *
     * @return the objectPrefix
     */
    public String getObjectPrefix() {
        return this.objectPrefix;
    }

    /**
     * Sets the objectPrefix.
     *
     * @param objectPrefix the objectPrefix to set
     */
    public void setObjectPrefix(String objectPrefix) {
        this.objectPrefix = objectPrefix;
    }

    /**
     * Returns the object.
     *
     * @return the object
     */
    public String getObject() {
        return this.object;
    }

    /**
     * Sets the object.
     *
     * @param object the object to set
     */
    public void setObject(String object) {
        this.object = object;
    }

    /**
     * Returns the verb.
     *
     * @return the verb
     */
    public String getVerb() {
        return this.verb;
    }

    /**
     * Sets the verb.
     *
     * @param verb the verb to set
     */
    public void setVerb(String verb) {
        this.verb = verb;
    }

    /**
     * Returns the descriptionPrefix.
     *
     * @return the descriptionPrefix
     */
    public String getDescriptionPrefix() {
        return this.descriptionPrefix;
    }

    /**
     * Sets the descriptionPrefix.
     *
     * @param descriptionPrefix the descriptionPrefix to set
     */
    public void setDescriptionPrefix(String descriptionPrefix) {
        this.descriptionPrefix = descriptionPrefix;
    }

    /**
     * Returns the description.
     *
     * @return the description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Sets the description.
     *
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Indicates whether this data set is too long to show.
     *
     * @return a boolean representing whether this data set is too long to show
     */
    public boolean isTooLong() {
        return (this.getBeginning().length() > Configuration.MaxQuestionLength
                || this.getEnding().length() > Configuration.MaxAnswerLength);
    }

    /**
     * Returns the beginning of the data set.
     *
     * @return the beginning of the data set
     */
    public String getBeginning() {
        return (this.objectPrefix != null ? this.objectPrefix + " " : "")
                + (this.object != null ? this.object + " " : "") + (this.verb != null ? this.verb + " " : "")
                + (this.descriptionPrefix != null ? this.descriptionPrefix : "");
    }

    /**
     * Returns the ending of the data set.
     *
     * @return the ending of the data set
     */
    public String getEnding() {
        return (this.description != null ? this.description : "") + ".";
    }

    /**
     * Returns a string representation.
     *
     * @return a string representation
     */
    @Override
    public String toString() {
        return this.getBeginning() + " " + this.getEnding();
    }
}
