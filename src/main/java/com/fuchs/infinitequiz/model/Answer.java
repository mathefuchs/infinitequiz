package com.fuchs.infinitequiz.model;

import javax.xml.bind.annotation.XmlElement;

/**
 * This class represents a Answer.
 *
 * @author Tobias Fuchs
 * @version 09/10/16
 */
public class Answer {
    private String ending;
    private boolean isTrue;

    /**
     * Initializes a new empty Answer.
     */
    public Answer() {
        this.ending = "";
        this.isTrue = false;
    }

    /**
     * Initializes a new Answer.
     *
     * @param ending the ending of the answer
     * @param isTrue whether the answer is true
     */
    public Answer(String ending, boolean isTrue) {
        this.ending = ending;
        this.isTrue = isTrue;
    }

    /**
     * Returns the ending.
     *
     * @return the ending
     */
    @XmlElement(name = "ending")
    public String getEnding() {
        return this.ending;
    }

    /**
     * Sets the ending.
     *
     * @param ending the ending to set
     */
    public void setEnding(String ending) {
        this.ending = ending;
    }

    /**
     * @return whether the answer is true
     */
    @XmlElement(name = "isTrue")
    public boolean getIsTrue() {
        return this.isTrue;
    }

    /**
     * Sets the isTrue.
     *
     * @param isTrue the isTrue to set
     */
    public void setIsTrue(boolean isTrue) {
        this.isTrue = isTrue;
    }

    /**
     * Returns a string representation.
     *
     * @return a string representation
     */
    @Override
    public String toString() {
        return this.ending;
    }

    /**
     * Returns a logging string representation.
     *
     * @return a logging string representation
     */
    public String toLoggingString() {
        return this.ending + " (" + this.isTrue + ")";
    }
}
