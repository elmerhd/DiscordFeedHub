package com.junk.application.discordfeedhub.utils;


import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author elmerhd
 */
public final class DiscordPostQueue {

    private static final int INTERVAL_SECONDS = 5;

    private static final BlockingQueue<DiscordPost> queue = new LinkedBlockingQueue<>();

    private static ScheduledExecutorService scheduler;
    private static final AtomicBoolean running = new AtomicBoolean(false);

    private DiscordPostQueue() {
        // prevent instantiation
    }

    /* =============================
       PUBLIC API
       ============================= */

    public static void enqueue(DiscordPost post) {
        queue.offer(post);
        autoStart();
    }

    public static int size() {
        return queue.size();
    }

    public static boolean isRunning() {
        return running.get();
    }

    /* =============================
       INTERNAL LOGIC
       ============================= */

    private static synchronized void autoStart() {
        if (running.get()) return;

        scheduler = Executors.newSingleThreadScheduledExecutor();
        running.set(true);

        scheduler.scheduleAtFixedRate(
                DiscordPostQueue::processQueue,
                0,
                INTERVAL_SECONDS,
                TimeUnit.SECONDS
        );
    }

    private static void processQueue() {
        try {
            DiscordPost post = queue.poll();

            if (post == null) {
                shutdown();
                return;
            }

            DiscordWebhookService.send(
                    post.getWebhookUrl(),
                    post.getPayload(),
                    post.getSource(),
                    post.getEntry()
            );

        } catch (Exception ex) {
            System.getLogger(DiscordPostQueue.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    private static synchronized void shutdown() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
        }
        running.set(false);
    }

    // Helper to generate random Discord color
    public static int randomColor() {
        return ThreadLocalRandom.current().nextInt(0xFFFFFF);
    }
    
}

