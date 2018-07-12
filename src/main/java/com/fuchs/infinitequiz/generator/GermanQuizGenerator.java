package com.fuchs.infinitequiz.generator;

import com.fuchs.infinitequiz.logging.LoggingManager;
import com.fuchs.infinitequiz.model.DataSet;
import com.fuchs.infinitequiz.model.GermanDataSet;
import com.fuchs.infinitequiz.model.QuizSet;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class represents a QuizGenerator.
 *
 * @author Tobias Fuchs
 * @version 09/07/16
 */
public class GermanQuizGenerator extends QuizGenerator {
    private static final String randomArticleUrl = "https://de.wikipedia.org/wiki/Spezial:Zuf%C3%A4llige_Stammseite";
    private static final String articleRegex = "((Der|Die|Das|Ein|Eine) )?(.+?)(,.*?,)?(\\(.*?\\))? (ist|war|sind|waren) (der|die|das|ein|eine) (.+?[^0-9 \\(\\)][^0-9 \\(\\)][^0-9]\\.)(.*)";
    private static final String bracketRegex = "\\(.*?\\)";
    private static final String rectBracketRegex = "\\[.*?\\]";
    private static final String commaRegex = ",.*?,";
    private static final String curlyBracket = "\\{.*?\\}";
    private static final Pattern articlePattern = Pattern.compile(articleRegex);
    private static final Pattern bracketPattern = Pattern.compile(bracketRegex);
    private static final Pattern rectBracketPattern = Pattern.compile(rectBracketRegex);
    private static final Pattern commaPattern = Pattern.compile(commaRegex);
    private static final Pattern curlyBracketPattern = Pattern.compile(curlyBracket);

    private static final LoggingManager log = LoggingManager.getLog(GermanQuizGenerator.class);

    private BlockingQueue<GermanDataSet>[] dataStorage;

    /**
     * Initializes a new QuizGenerator.
     */
    @SuppressWarnings("unchecked")
    public GermanQuizGenerator() {
        super();

        this.dataStorage = new ArrayBlockingQueue[GermanDataSet.Combinations];
        for (int i = 0; i < this.dataStorage.length; i++) {
            this.dataStorage[i] = new ArrayBlockingQueue<>(Configuration.AnswerCount * 2);
        }

        log.log("New GermanQuizGenerator initialised.");
    }

    /**
     * Tries to store a data set to the storage of a random wikipedia article.
     *
     * @return a boolean whether a article was stored successfully
     */
    @Override
    protected boolean tryStoreRandomArticleData() {
        // Retrieving the string.
        String string = this.getRandomArticle(randomArticleUrl);

        // Parsing the string
        if (string != null && !string.isEmpty()) {
            Matcher articleMatcher = GermanQuizGenerator.articlePattern.matcher(string);

            if (articleMatcher.matches()) {
                String objectPrefix = articleMatcher.group(2);
                String object = GermanQuizGenerator.curlyBracketPattern
                        .matcher(
                                GermanQuizGenerator.commaPattern
                                        .matcher(GermanQuizGenerator.rectBracketPattern
                                                .matcher(GermanQuizGenerator.bracketPattern
                                                        .matcher(articleMatcher.group(3)).replaceAll(""))
                                                .replaceAll(""))
                                        .replaceAll(""))
                        .replaceAll("").trim().replaceAll("  ", " ").replaceAll(" ,", ",");
                String verb = articleMatcher.group(6);
                String descriptionPrefix = articleMatcher.group(7);
                String description = GermanQuizGenerator.curlyBracketPattern
                        .matcher(GermanQuizGenerator.rectBracketPattern.matcher(
                                GermanQuizGenerator.bracketPattern.matcher(articleMatcher.group(8)).replaceAll(""))
                                .replaceAll(""))
                        .replaceAll("").trim().replaceAll("  ", " ").replaceAll(" \\.", ".").replaceAll(" ,", ",");
                description = description.substring(0, description.length() - 1);

                if (description.endsWith("bzw")) {
                    return false;
                }

                GermanDataSet dataSet = new GermanDataSet(objectPrefix, object, verb, descriptionPrefix, description);

                if (dataSet.isTooLong()) {
                    return false;
                }

                try {
                    this.dataStorage[dataSet.hashCode()].put(dataSet);
                } catch (InterruptedException e) {
                    log.log("Thread has been interrupted while waiting for free capacity to store the data set.", e);
                }

                return true;
            }
        }

        return false;
    }

    /**
     * Gathers quiz sets from the stored data sets.
     */
    @Override
    protected void gatherQuizSets() {
        for (int i = 0; i < this.dataStorage.length; i++) {
            while (this.dataStorage[i].size() >= Configuration.AnswerCount) {
                try {
                    DataSet[] dataSets = new DataSet[Configuration.AnswerCount];

                    for (int j = 0; j < Configuration.AnswerCount; j++) {
                        dataSets[j] = this.dataStorage[i].take();
                    }

                    this.putQuizSet(new QuizSet(dataSets));
                } catch (InterruptedException e) {
                    log.log("Thread has been interrupted while waiting for capacity to store a quiz set.", e);
                }
            }
        }
    }
}
