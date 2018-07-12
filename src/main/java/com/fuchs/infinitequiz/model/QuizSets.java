package com.fuchs.infinitequiz.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class holds QuizSets for the purpose of serialization.
 *
 * @author Tobias Fuchs
 * @version 07/18/17
 */
@XmlRootElement
public class QuizSets {
    private QuizSet[] quizSets;

    /**
     * Constructor for JAXB
     */
    public QuizSets() {
    }

    /**
     * Initializes a new QuizSets.
     *
     * @param quizSets
     */
    public QuizSets(final QuizSet[] quizSets) {
        this.quizSets = quizSets;
    }

    /**
     * Returns the quizSets.
     *
     * @return the quizSets
     */
    @XmlElement(name = "quizSet")
    public QuizSet[] getQuizSets() {
        return this.quizSets;
    }

    /**
     * Sets the quizSets.
     *
     * @param quizSets the quizSets to set
     */
    public void setQuizSets(final QuizSet[] quizSets) {
        this.quizSets = quizSets;
    }
}
