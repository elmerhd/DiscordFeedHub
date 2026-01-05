package com.junk.application.discordfeedhub.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;

/**
 * Helper class to add/remove the currently running JAR to Windows Startup.
 */
public class StartupHelper {
    
    private static String appName;
    
    static {
        try {
            Properties prop = Utility.getApplicationProperty();
            appName = prop.getProperty("app.name");
        } catch (IOException ex) {
            DiscordFeedHubLogger.getLogger(StartupHelper.class.getName()).log(Level.INFO, (String) null, ex);
        }
    }
    

    /**
     * Checks if the OS is Windows
     */
    public static boolean isWindows() {
        String os = System.getProperty("os.name").toLowerCase();
        DiscordFeedHubLogger.getLogger(StartupHelper.class.getName()).log(Level.INFO, "Checking operating system : " + os);
        return os.contains("win");
    }

    /**
     * Gets the user's Startup folder
     */
    private static File getStartupFolder() {
        return new File(System.getenv("APPDATA")
                + "\\Microsoft\\Windows\\Start Menu\\Programs\\Startup");
    }

    /**
     * Adds the currently running JAR to Windows Startup
     *
     * @return return if batch file created, false if not
     */
    public static boolean addToStartup(File jarFile) {
        if (!isWindows()) {
            DiscordFeedHubLogger.getLogger(StartupHelper.class.getName()).log(Level.INFO, "Startup only supported on Windows");
            return false;
        }
        if (jarFile == null) {
            DiscordFeedHubLogger.getLogger(StartupHelper.class.getName()).log(Level.INFO, "Cannot detect running JAR. Startup skipped.");
            return false;
        }

        File batchFile = new File(getStartupFolder(), appName + ".bat");

        try {
            String content = "@echo off\n" +
                    "start javaw -jar \"" + jarFile.getAbsolutePath() + "\"" + Constants.STARTUP_ARGS_MINIMIZED;

            try (FileWriter writer = new FileWriter(batchFile)) {
                writer.write(content);
            }
            DiscordFeedHubLogger.getLogger(StartupHelper.class.getName()).log(Level.INFO, () -> "Startup batch created at: " + batchFile.getAbsolutePath());
            return true;

        } catch (IOException ex) {
            DiscordFeedHubLogger.getLogger(StartupHelper.class.getName()).log(Level.INFO, (String) null, ex);
            return false;
        }
    }

    /**
     * Removes the startup batch file
     *
     * @return true if the batch file removed, false if not
     */
    public static boolean removeFromStartup() {
        if (!isWindows()) {
            DiscordFeedHubLogger.getLogger(StartupHelper.class.getName()).log(Level.INFO, () -> "Startup removal only supported on Windows");
            return false;
        }

        File batchFile = new File(getStartupFolder(), appName + ".bat");
        if (batchFile.exists()) {
            boolean deleted = batchFile.delete();
            if (deleted) {
                DiscordFeedHubLogger.getLogger(StartupHelper.class.getName()).log(Level.INFO, () -> "Removed startup batch: " + batchFile.getAbsolutePath());
            } else {
                DiscordFeedHubLogger.getLogger(StartupHelper.class.getName()).log(Level.INFO, () -> "Failed to remove startup batch: " + batchFile.getAbsolutePath());
            }
            return deleted;
        } else {
            DiscordFeedHubLogger.getLogger(StartupHelper.class.getName()).log(Level.INFO, () -> "Startup batch does not exist: " + batchFile.getAbsolutePath());
            return false;
        }
    }
}
