
package com.junk.application.discordfeedhub.utils;

import com.junk.application.discordfeedhub.model.RssSource;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

/**
 *
 * @author elmerhd
 */
public class RSSReaderTask implements Runnable {
    
    private RssSource source;
    
    public RSSReaderTask(RssSource source) {
        this.source = source;
    }
    
    public List<SyndEntry> getList(String rssUrl) throws MalformedURLException, IllegalArgumentException, FeedException, IOException {
        URL url = new URL(rssUrl);
        SyndFeedInput input = new SyndFeedInput();
        try (XmlReader reader = new XmlReader(url)) {
            SyndFeed feed = input.build(reader);
            return feed.getEntries();
        }
    }

    @Override
    public void run() {
        try {
            List<SyndEntry> items = getList(source.rssUrl());
            for (SyndEntry entry : items) {
                DiscordFeedHubLogger.getLogger(RSSReaderTask.class.getName()).log(Level.INFO, () -> ("Item found => " +entry.getTitle()));
                boolean isPosted = DatabaseManager.isPosted(source.id(), entry.getLink());
                DiscordFeedHubLogger.getLogger(RSSReaderTask.class.getName()).log(Level.INFO, () -> ("Checking item in db exist? ("+isPosted+") "));
                if (!isPosted) {
                    String sanitizedDescription = Jsoup.clean(entry.getDescription().getValue(), Safelist.none());
                    JSONObject embed = new JSONObject()
                        .put("title", entry.getTitle())
                        .put("url", entry.getLink())
                        .put("description", sanitizedDescription)
                        .put("color", Utility.randomColor())
                        .put("footer", new JSONObject()
                                .put("text", source.title()));
                    
                    JSONObject payload = new JSONObject()
                    .put("embeds", new org.json.JSONArray().put(embed));
                    DiscordFeedHubLogger.getLogger(RSSReaderTask.class.getName()).log(Level.INFO, () -> ("Queueing item : => " + entry.getTitle()));
                    DiscordPostQueue.enqueue(new DiscordPost(source.discordWebhookUrl(), payload, source, entry));
                }
            }
        } catch (Exception ex) {
            DiscordFeedHubLogger.getLogger(RSSReaderTask.class.getName()).log(Level.SEVERE, (String) null, ex);
        }
    }
}
