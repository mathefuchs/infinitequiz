package com.fuchs.infinitequiz;

import com.fuchs.infinitequiz.logging.LoggingManager;

/**
 * This class starts the QuizManager.
 *
 * @author Tobias Fuchs
 * @version 07/18/17
 */
public final class Main {

    private static final LoggingManager log = LoggingManager.getLog(Main.class);

    /**
     * Main entry-point for the application.
     *
     * @param args command line args
     */
    public static void main(final String[] args) {
        QuizManager.getInstance().startSearching();

        try {
            Thread.sleep(300000);
        } catch (InterruptedException e) {
            log.log("An exception occurred while waiting until the quiz sets have been retrieved.", e);
        }

        QuizManager.getInstance().saveQuizSets();
    }
}
