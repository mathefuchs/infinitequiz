package com.fuchs.infinitequiz.generator;

import com.fuchs.infinitequiz.logging.LoggingManager;
import com.fuchs.infinitequiz.model.QuizSet;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This abstract class characterizes a quiz generator.
 *
 * @author Tobias Fuchs
 * @version 11/27/16
 */
public abstract class QuizGenerator {
    private static final LoggingManager log = LoggingManager.getLog(QuizGenerator.class);
    private Thread[] retrieverThreads;
    private Thread gatherThread;
    private AtomicInteger successes;
    private final Runnable gatherRunnable = () -> {
        while (true) {
            if (QuizGenerator.this.successes.intValue() > Configuration.MinSuccessesToGather) {
                QuizGenerator.this.successes.set(0);
                QuizGenerator.this.gatherQuizSets();
            }

            try {
                Thread.sleep(Configuration.TimeToWait);
            } catch (InterruptedException e) {
                log.log("Thread has been interrupted while waiting for data sets.", e);
            }
        }
    };
    private AtomicInteger totalSuccesses;
    private AtomicInteger attempts;
    private AtomicInteger connectionTimeOuts;
    private BlockingQueue<QuizSet> quizSets;
    private final Runnable retrieverRunnable = () -> {
        while (true) {
            if (QuizGenerator.this.quizSets.size() != Configuration.StoredQuizSets) {
                QuizGenerator.this.attempts.incrementAndGet();

                if (QuizGenerator.this.tryStoreRandomArticleData()) {
                    QuizGenerator.this.totalSuccesses.incrementAndGet();
                    QuizGenerator.this.successes.incrementAndGet();
                }
            } else {
                try {
                    Thread.sleep(Configuration.TimeToWait);
                } catch (InterruptedException e) {
                    log.log("Thread has been interrupted while waiting for free capacity.", e);
                }
            }
        }
    };

    /**
     * Initializes a new QuizGenerator.
     */
    public QuizGenerator() {
        this.quizSets = new ArrayBlockingQueue<>(Configuration.StoredQuizSets);

        this.totalSuccesses = new AtomicInteger(0);
        this.attempts = new AtomicInteger(0);
        this.successes = new AtomicInteger(0);
        this.connectionTimeOuts = new AtomicInteger(0);

        this.retrieverThreads = new Thread[Configuration.RetrieverThreads];
        for (int i = 0; i < this.retrieverThreads.length; i++) {
            this.retrieverThreads[i] = new Thread(this.retrieverRunnable);
            this.retrieverThreads[i].setDaemon(true);
        }
        this.gatherThread = new Thread(this.gatherRunnable);
        this.gatherThread.setDaemon(true);
    }

    /**
     * Returns the quality ratio of the Internet research.
     *
     * @return the quality ratio of the Internet research
     */
    public double getQualityRatio() {
        return (double) this.totalSuccesses.intValue() / (this.attempts.intValue() == 0 ? 1 : this.attempts.intValue())
                * 100.0;
    }

    /**
     * Returns the quizSets.
     *
     * @return the quizSets
     */
    public QuizSet[] getQuizSets() {
        return this.quizSets.toArray(new QuizSet[this.quizSets.size()]);
    }

    /**
     * Puts the specified quiz set to the storage.
     *
     * @param quizSet the quiz set to put
     * @throws InterruptedException     if interrupted while waiting
     * @throws ClassCastException       if the class of the specified element prevents it from being
     *                                  added to this queue
     * @throws NullPointerException     if the specified element is null
     * @throws IllegalArgumentException if some property of the specified element prevents it from
     *                                  being added to this
     */
    public void putQuizSet(QuizSet quizSet)
            throws InterruptedException, ClassCastException, NullPointerException, IllegalArgumentException {
        this.quizSets.put(quizSet);
    }

    /**
     * Tries to return a quiz set.
     *
     * @return a quiz set or null
     */
    public QuizSet pollQuizSet() {
        QuizSet quizSet = this.quizSets.poll();

        if (quizSet == null) {
            log.log("No quiz set available at the moment.");
        } else {
            log.log("You took one quiz set. " + this.quizSets.size() + " quiz sets remaining.");
        }

        return quizSet;
    }

    /**
     * Returns a quiz set, waits if necessary.
     *
     * @return a quiz set
     */
    public QuizSet takeQuizSet() {
        QuizSet quizSet = null;

        try {
            quizSet = this.quizSets.take();
        } catch (InterruptedException e) {
            log.log("Thread has been interrupted while waiting for a quiz set.", e);
        }

        return quizSet;
    }

    /**
     * Tries to store a quiz set to the queue.
     *
     * @param quizset the quiz set to store
     * @return a boolean representing whether the operation was successfully
     */
    public boolean tryStoreQuizSet(QuizSet quizset) {
        return this.quizSets.offer(quizset);
    }

    /**
     * Starts to read articles asynchronously.
     */
    public void startReading() {
        for (int i = 0; i < this.retrieverThreads.length; i++) {
            this.retrieverThreads[i].start();
        }
        this.gatherThread.start();
        log.log("Start daemon threads.");
    }

    /**
     * Returns the first paragraph of a random wikipedia article.
     *
     * @param articleUrl the Url of the article
     * @return the first paragraph of a random wikipedia article
     */
    protected final String getRandomArticle(String articleUrl) {
        String string = null;

        try {
            Document document = Jsoup.connect(articleUrl).get();

            if (document != null && document.select("p") != null && document.select("p").first() != null) {
                string = document.select("p").first().text();
            } else {
                log.log("Wikipedia page \"" + document.title() + "\" has wrong layout");
            }

            if (this.connectionTimeOuts.get() > 0) {
                this.connectionTimeOuts.decrementAndGet();
            }
        } catch (IOException e) {
            int timeOuts = this.connectionTimeOuts.incrementAndGet();

            if (timeOuts > Configuration.MinTimeOutsToWait) {
                int timeToWait = timeOuts * Configuration.TimeToWait > Configuration.MaxTimeToWait
                        ? Configuration.MaxTimeToWait
                        : timeOuts * Configuration.TimeToWait;
                log.log("Connection failed. Pausing thread for " + timeToWait + " milliseconds.", e);

                try {
                    Thread.sleep(timeToWait);
                } catch (InterruptedException f) {
                    log.log("Paused thread has been interrupted.", f);
                }
            } else {
                log.log("Connection failed.", e);
            }
        }

        return string;
    }

    /**
     * Tries to store a data set to the storage of a random wikipedia article.
     *
     * @return a boolean whether a article was stored successfully
     */
    protected abstract boolean tryStoreRandomArticleData();

    /**
     * Gathers quiz sets from the stored data sets.
     */
    protected abstract void gatherQuizSets();
}
