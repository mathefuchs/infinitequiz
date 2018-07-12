package com.fuchs.infinitequiz.model;

import javax.xml.bind.annotation.XmlElement;
import java.util.Random;

/**
 * This class represents a QuizSet.
 *
 * @author Tobias Fuchs
 * @version 09/10/16
 */
public class QuizSet {
    private static final Random random = new Random();

    private String beginning;
    private Answer[] answers;
    private int rightIndex;

    /**
     * Initializes a new empty QuizSet.
     */
    public QuizSet() {
        this.beginning = "";
        this.answers = null;
        this.rightIndex = -1;
    }

    /**
     * Initializes a new QuizSet.
     *
     * @param dataSets the data sets to use for this question
     */
    public QuizSet(DataSet[] dataSets) {
        this.answers = new Answer[dataSets.length];
        this.rightIndex = random.nextInt(dataSets.length);

        String objectPrefix = dataSets[this.rightIndex].getObjectPrefix() != null
                ? dataSets[this.rightIndex].getObjectPrefix() + " "
                : "";
        String object = dataSets[this.rightIndex].getObject() != null ? dataSets[this.rightIndex].getObject() + " "
                : "";
        String verb = dataSets[this.rightIndex].getVerb() != null ? dataSets[this.rightIndex].getVerb() + " " : "";
        String descriptionPrefix = dataSets[this.rightIndex].getDescriptionPrefix() != null
                ? dataSets[this.rightIndex].getDescriptionPrefix() : "";
        this.beginning = objectPrefix + object + verb + descriptionPrefix;

        for (int i = 0; i < dataSets.length; i++) {
            this.answers[i] = new Answer(dataSets[i].getDescription() + ".", this.rightIndex == i);
        }
    }

    /**
     * Returns the rightIndex.
     *
     * @return the rightIndex
     */
    @XmlElement(name = "rightIndex")
    public int getRightIndex() {
        if (this.rightIndex != -1) {
            return this.rightIndex;
        } else {
            for (int i = 0; i < this.answers.length; i++) {
                if (this.answers[i].getIsTrue()) {
                    this.rightIndex = i;
                    break;
                }
            }

            return this.rightIndex;
        }
    }

    /**
     * Sets the rightIndex.
     *
     * @param rightIndex the rightIndex to set
     */
    public void setRightIndex(int rightIndex) {
        this.rightIndex = rightIndex;
    }

    /**
     * Returns the beginning.
     *
     * @return the beginning
     */
    @XmlElement(name = "beginning")
    public String getBeginning() {
        return this.beginning;
    }

    /**
     * Sets the beginning.
     *
     * @param beginning the beginning to set
     */
    public void setBeginning(String beginning) {
        this.beginning = beginning;
    }

    /**
     * Returns the answers.
     *
     * @return the answers
     */
    @XmlElement(name = "answer")
    public Answer[] getAnswers() {
        return this.answers;
    }

    /**
     * Sets the answers.
     *
     * @param answers the answers to set
     */
    public void setAnswers(Answer[] answers) {
        this.answers = answers;
    }

    /**
     * Returns a string representation.
     *
     * @return a string representation
     */
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.beginning);
        stringBuilder.append("\n");

        for (int i = 0; i < this.answers.length; i++) {
            stringBuilder.append(this.answers[i].toString());
            stringBuilder.append("\n");
        }

        return stringBuilder.toString();
    }

    /**
     * Returns a logging string representation.
     *
     * @return a logging string representation
     */
    public String toLoggingString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.beginning);
        stringBuilder.append("\n");

        for (int i = 0; i < this.answers.length; i++) {
            stringBuilder.append(this.answers[i].toLoggingString());
            stringBuilder.append("\n");
        }

        return stringBuilder.toString();
    }
}
