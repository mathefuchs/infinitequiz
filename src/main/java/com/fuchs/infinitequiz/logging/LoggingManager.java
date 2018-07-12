package com.fuchs.infinitequiz.logging;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class represents a LoggingManager.
 *
 * @author Tobias Fuchs
 * @version 07/18/17
 */
public final class LoggingManager {
    private final static DateFormat dateFormat = new SimpleDateFormat("[dd.MM.yyyy | HH:mm:ss.SSS]");
    private Class source;

    private LoggingManager(Class source) {
        this.source = source;
    }

    /**
     * @return new log instance
     */
    public static LoggingManager getLog(Class source) {
        return new LoggingManager(source);
    }

    private String getTimeString() {
        return dateFormat.format(new Date());
    }

    /**
     * Logs a string
     *
     * @param content the content of the log
     */
    public void log(String content) {
        String className = source.getSimpleName();

        StringBuilder builder = new StringBuilder();
        builder.append(getTimeString());
        builder.append(" ");
        builder.append(source.getSimpleName());

        for (int i = 0; i < 20 - className.length(); i++) {
            builder.append(" ");
        }

        builder.append(": ");
        builder.append(content);

        System.out.println(builder.toString());
    }

    /**
     * Logs a string
     *
     * @param content the content of the log
     * @param e       the exception of the log
     */
    public void log(String content, Exception e) {
        log(content);
        System.out.println(e.getStackTrace()[0]);
    }
}
