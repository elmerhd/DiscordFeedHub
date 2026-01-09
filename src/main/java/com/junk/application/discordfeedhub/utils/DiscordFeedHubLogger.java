package com.junk.application.discordfeedhub.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author elmerhd
 */
public class DiscordFeedHubLogger {
    private static final Logger logger = Logger.getLogger(DiscordFeedHubLogger.class.getName());
    private static String session = null;
    private static boolean initialized = false;
    
    static {
        session = LocalDateTime.now()
            .format(java.time.format.DateTimeFormatter.ofPattern(
                    "yyyy-MM-dd"));
    }
    
    
    
    public static Logger getLogger(String name) {
        if (!initialized) {
            init();
        }
        return logger;
    }
    
    public static void init() {
        Properties properties;
        try {
            properties = Utility.getApplicationProperty();
            Path dir = Path.of(
                System.getProperty("user.home"),
                properties.getProperty("app.folder"),
                properties.getProperty("app.log.folder"),
                session
            );

            Files.createDirectories(dir);

            FileHandler handler = new FileHandler(
                    dir.resolve(properties.getProperty("app.log.file")).toString(),
                    true
            );

            handler.setFormatter(new SimpleFormatter());
            logger.addHandler(handler);
            logger.setUseParentHandlers(false);
            logger.setLevel(Level.ALL);
            initialized = true;
        } catch (IOException ex) {
            logger.log(Level.SEVERE, (String) null, ex);
        }
    }
}
