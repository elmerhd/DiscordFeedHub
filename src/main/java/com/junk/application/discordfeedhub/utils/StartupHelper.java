package com.junk.application.discordfeedhub.utils;

import com.junk.application.discordfeedhub.DiscordFeedHub;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Properties;

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
            System.getLogger(StartupHelper.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
    

    /**
     * Checks if the OS is Windows
     */
    public static boolean isWindows() {
        String os = System.getProperty("os.name").toLowerCase();
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
     * Gets the currently running JAR file
     */
    private static File getRunningJarFile() {
        try {
            File jarFile = new File(
                    StartupHelper.class
                            .getProtectionDomain()
                            .getCodeSource()
                            .getLocation()
                            .toURI()
            );
            if (jarFile.isFile()) {
                return jarFile; // running from JAR
            } else {
                return null; // running from IDE
            }
        } catch (URISyntaxException ex) {
            System.getLogger(StartupHelper.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            return null;
        }
    }

    /**
     * Adds the currently running JAR to Windows Startup
     *
     * @param appName Name of the app (used for the batch file)
     */
    public static boolean addToStartup() {
        if (!isWindows()) {
            System.out.println("Startup only supported on Windows");
            return false;
        }

        File jarFile = getRunningJarFile();
        if (jarFile == null) {
            System.out.println("Cannot detect running JAR. Startup skipped.");
            return false;
        }

        File batchFile = new File(getStartupFolder(), appName + ".bat");

        try {
            String content = "@echo off\n" +
                    "start javaw -jar \"" + jarFile.getAbsolutePath() + "\" --minimized\n";

            try (FileWriter writer = new FileWriter(batchFile)) {
                writer.write(content);
            }

            System.out.println("Startup batch created at: " + batchFile.getAbsolutePath());
            return true;

        } catch (IOException ex) {
            System.getLogger(StartupHelper.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            return false;
        }
    }

    /**
     * Removes the startup batch file
     *
     * @param appName Name of the app (used for the batch file)
     */
    public static boolean removeFromStartup() {
        if (!isWindows()) {
            System.out.println("Startup removal only supported on Windows");
            return false;
        }

        File batchFile = new File(getStartupFolder(), appName + ".bat");
        if (batchFile.exists()) {
            boolean deleted = batchFile.delete();
            if (deleted) {
                System.out.println("Removed startup batch: " + batchFile.getAbsolutePath());
            } else {
                System.out.println("Failed to remove startup batch: " + batchFile.getAbsolutePath());
            }
            return deleted;
        } else {
            System.out.println("Startup batch does not exist: " + batchFile.getAbsolutePath());
            return false;
        }
    }
}
