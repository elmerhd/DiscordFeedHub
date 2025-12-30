package com.junk.application.discordfeedhub.utils;

import com.junk.application.discordfeedhub.model.RssSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

/**
 *
 * @author elmerhd
 */
public class RSSScheduler {
    private int retryCount = 0;
    private final int INTERVAL_SECONDS = 120;
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
                System.getLogger(RSSScheduler.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        },
            0,
            INTERVAL_SECONDS,
            TimeUnit.SECONDS
        );
    }

    private void runCycle(JLabel statusLabel) throws SQLException, IOException {
        retryCount++;
        System.out.println("running retries : " + retryCount);
        if (statusLabel != null) {
            SwingUtilities.invokeLater(() ->
                statusLabel.setText("Checking Enabled RSS feeds... Retries " + retryCount)
            );
        }
        
        List<RssSource> sources = DatabaseManager.loadSources(true);
        
        for (RssSource source : sources) {
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

