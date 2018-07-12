package com.fuchs.infinitequiz.generator;

/**
 * This class stores constants.
 *
 * @author Tobias Fuchs
 * @version 11/04/16
 */
public final class Configuration {
    /**
     * The count of displayed answers per question.
     */
    public static final int AnswerCount = 3;

    /**
     * The amount of quiz sets which are stored for use.
     */
    public static final int StoredQuizSets = 50;

    /**
     * The count of threads which are retrieving data from the Internet.
     */
    public static final int RetrieverThreads = 10;

    /**
     * The time to wait if waiting for capacity for example.
     */
    public static final int TimeToWait = 500;

    /**
     * The maximum time to wait if waiting for capacity for example.
     */
    public static final int MaxTimeToWait = 5000;

    /**
     * The minimal count of timeouts to start waiting for data.
     */
    public static final int MinTimeOutsToWait = 5;

    /**
     * The minimal count of successes to start generating quiz sets from the
     * data sets.
     */
    public static final int MinSuccessesToGather = 10;

    /**
     * The maximal length of a question.
     */
    public static final int MaxQuestionLength = 85;

    /**
     * The maximal length of an answer.
     */
    public static final int MaxAnswerLength = 64;

    /**
     * The currently active language.
     */
    public static final Language ActiveLanguage = Language.English;

    /**
     * Private constructor to avoid public object generation.
     */
    private Configuration() {
    }
}
