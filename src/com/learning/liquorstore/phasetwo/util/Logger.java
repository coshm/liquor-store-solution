package com.learning.liquorstore.phasetwo.util;

import java.util.Arrays;

public class Logger {

    private static final String PREFIX = "[DEBUG] - %s.%s:%d - ";

    private static boolean shouldDisplayLogs;

    public static void isDebugging(boolean isDebugging) {
        shouldDisplayLogs = isDebugging;
        if (shouldDisplayLogs) {
            System.out.println(generatePrefix() + "Enabled debug logging.");
        }
    }

    public static void debug(String msg) {
        if (shouldDisplayLogs) {
            System.out.println(generatePrefix() + msg);
        }
    }

    public static void debug(String msg, Object ... args) {
        if (shouldDisplayLogs) {
            String[] stringArgs = Arrays.asList(args).stream()
                    .map(obj -> String.valueOf(obj))
                    .toArray(String[]::new);
            System.out.println(String.format(generatePrefix() + msg, stringArgs));
        }
    }

    private static String generatePrefix() {
        StackTraceElement callingStackTrace = Thread.currentThread().getStackTrace()[3];

        // Derive the simple class name
        String className = callingStackTrace.getClassName();
        String simpleClassName = className.substring(className.lastIndexOf('.') + 1);

        return String.format(PREFIX, simpleClassName, callingStackTrace.getMethodName(),
                callingStackTrace.getLineNumber());
    }

}
