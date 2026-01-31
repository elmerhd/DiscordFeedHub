package com.junk.application.discordfeedhub.utils;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Properties;
import java.util.logging.Level;

/**
 *
 * @author elmerhd
 */
public final class InstanceChecker {

    private static FileLock lock;
    private static FileChannel channel;

    private InstanceChecker() {}

    public static boolean acquireLock(Properties props) {
        try {
            DiscordFeedHubLogger.getLogger(InstanceChecker.class.getName()).log(Level.INFO, "Acquiring app lock");
            Path lockFile = Path.of(
                System.getProperty("user.home"),
                props.getProperty("app.folder"),
                props.getProperty("app.name") + ".lock"
            );

            Files.createDirectories(lockFile.getParent());

            channel = FileChannel.open(
                lockFile,
                StandardOpenOption.CREATE,
                StandardOpenOption.WRITE
            );

            lock = channel.tryLock();
            return lock != null;

        } catch (IOException ex) {
            DiscordFeedHubLogger.getLogger(InstanceChecker.class.getName()).log(Level.SEVERE, (String) null, ex);
            return false;
        }
    }

    public static void releaseLock() {
        try {
            if (lock != null) lock.release();
            if (channel != null) channel.close();
        } catch (IOException ex) {
            DiscordFeedHubLogger.getLogger(InstanceChecker.class.getName()).log(Level.SEVERE, (String) null, ex);
        }
    }
}