package com.junk.application.discordfeedhub.utils;

import com.junk.application.discordfeedhub.model.RssSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

/**
 *
 * @author elmerhd
 */
public class RSSScheduler {
    private int retryCount = 0;
    private boolean started = false;

    private ScheduledExecutorService scheduler = null;

    private ExecutorService rssExecutor = null;
    
    private void initExecutors() {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        rssExecutor = Executors.newFixedThreadPool(4);
    }

    public void start(JLabel statusLabel) {
        if (scheduler == null || scheduler.isShutdown()) {
            initExecutors();
        }
        setStarted(true);
        if (statusLabel != null) {
            SwingUtilities.invokeLater(() -> {
                statusLabel.setText("Starting RSS monitoring...");
            });
        }
        
        scheduler.scheduleAtFixedRate(
            () -> {
            try {
                runCycle(statusLabel);
            } catch (SQLException | IOException ex) {
                DiscordFeedHubLogger.getLogger(RSSScheduler.class.getName()).log(Level.SEVERE, (String) null, ex);
            }
        },
            0,
            Utility.getPreference().getSchedulerTimerInterval(),
            TimeUnit.SECONDS
        );
    }

    private void runCycle(JLabel statusLabel) throws SQLException, IOException {
        retryCount++;
        DiscordFeedHubLogger.getLogger(RSSScheduler.class.getName()).log(Level.INFO, () -> ("Running task : retries => " + retryCount));
        if (statusLabel != null) {
            SwingUtilities.invokeLater(() ->
                statusLabel.setText("Checking Enabled RSS feeds... Retries " + retryCount)
            );
        }
        
        List<RssSource> sources = DatabaseManager.loadSources(true);
        
        for (RssSource source : sources) {
            DiscordFeedHubLogger.getLogger(RSSScheduler.class.getName()).log(Level.INFO, () -> ("Checking source : " + source.title() +" => " + retryCount));
            rssExecutor.submit(new RSSReaderTask(source));
        }
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public boolean isStarted() {
        return started;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void shutdown(JLabel statusLabel) {
        retryCount = 0;
        setStarted(false);
        if (scheduler != null) {
            scheduler.shutdownNow();
        }
        if (rssExecutor != null) {
            rssExecutor.shutdownNow();
        }
        if (statusLabel != null) {
            statusLabel.setText("Stopped");
        }
    }
}

