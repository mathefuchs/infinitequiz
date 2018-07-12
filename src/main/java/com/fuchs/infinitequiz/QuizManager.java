package com.fuchs.infinitequiz;

import com.fuchs.infinitequiz.generator.Configuration;
import com.fuchs.infinitequiz.generator.EnglishQuizGenerator;
import com.fuchs.infinitequiz.generator.GermanQuizGenerator;
import com.fuchs.infinitequiz.generator.Language;
import com.fuchs.infinitequiz.generator.QuizGenerator;
import com.fuchs.infinitequiz.logging.LoggingManager;
import com.fuchs.infinitequiz.model.QuizSet;
import com.fuchs.infinitequiz.model.QuizSets;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;

/**
 * This class represents a QuizManager.
 *
 * @author Tobias Fuchs
 * @version 11/03/16
 */
public final class QuizManager {
    private static final QuizManager instance = new QuizManager();
    private static final LoggingManager log = LoggingManager.getLog(QuizManager.class);
    private static JAXBContext context;

    private final QuizGenerator quizGenerator;

    /**
     * Private Constructor to avoid object generation.
     */
    private QuizManager() {
        if (Configuration.ActiveLanguage == Language.German) {
            this.quizGenerator = new GermanQuizGenerator();
        } else {
            this.quizGenerator = new EnglishQuizGenerator();
        }

        try {
            context = JAXBContext.newInstance(QuizSets.class);
        } catch (final JAXBException e) {
            log.log("An exception occured while saving the quizsets.", e);
        }

        this.loadQuizSets();
    }

    /**
     * Returns the singleton instance of this class.
     *
     * @return the singleton instance of this class
     */
    public static QuizManager getInstance() {
        return instance;
    }

    /**
     * Returns a quiz set if possible else null.
     *
     * @return a quiz set if possible else null
     */
    public QuizSet pollQuizSet() {
        return this.quizGenerator.pollQuizSet();
    }

    /**
     * Returns a quiz set, waits if necessary.
     *
     * @return a quiz set
     */
    public QuizSet takeQuizSet() {
        return this.quizGenerator.takeQuizSet();
    }

    /**
     * Starts to search for quizSets.
     */
    public void startSearching() {
        log.log("Start reading wikipedia...");
        this.quizGenerator.startReading();
    }

    /**
     * Saves the QuizSets.
     */
    public void saveQuizSets() {
        final QuizSets quizSets = new QuizSets(this.quizGenerator.getQuizSets());
        final String filename = Configuration.ActiveLanguage == Language.German ? "quizSets_de.xml" : "quizSets_en.xml";
        final File file = new File(filename);

        try {
            final Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(quizSets, file);
            log.log("Saved quiz sets to \"" + filename + "\".");
        } catch (final JAXBException e) {
            log.log("An exception occured while saving the quizsets.", e);
        }
    }

    /**
     * Loads the saved quiz sets.
     */
    public void loadQuizSets() {
        final String filename = Configuration.ActiveLanguage == Language.German ? "quizSets_de.xml" : "quizSets_en.xml";
        final File file = new File(filename);

        if (file.exists()) {
            try {
                final Unmarshaller unmarshaller = context.createUnmarshaller();
                final QuizSets quizSets = (QuizSets) unmarshaller.unmarshal(file);

                if (quizSets != null) {
                    final QuizSet[] sets = quizSets.getQuizSets();
                    if (sets != null) {
                        for (final QuizSet set : sets) {
                            if (set != null) {
                                if (!this.quizGenerator.tryStoreQuizSet(set)) {
                                    break;
                                }
                            }
                        }
                    }
                }
            } catch (final JAXBException e) {
                log.log("An exception occured while saving the quizsets.", e);
            }
        }
    }

    /**
     * Returns the quality ratio of the Internet research.
     *
     * @return the quality ratio of the Internet research
     */
    public double getQualityRatio() {
        return this.quizGenerator.getQualityRatio();
    }
}
