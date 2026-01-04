
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
            List<SyndEntry> items = getList(source.getRssUrl());
            for (SyndEntry entry : items) {
                if (!DatabaseManager.isPosted(source.getId(), entry.getLink())) {
                    String sanitizedDescription = Jsoup.clean(entry.getDescription().getValue(), Safelist.none());
                    JSONObject embed = new JSONObject()
                        .put("title", entry.getTitle())
                        .put("url", entry.getLink())
                        .put("description", sanitizedDescription)
                        .put("color", Utility.randomColor())
                        .put("footer", new JSONObject()
                                .put("text", source.getTitle()));
                    
                    JSONObject payload = new JSONObject()
                    .put("embeds", new org.json.JSONArray().put(embed));
                    DiscordPostQueue.enqueue(new DiscordPost(source.getDiscordWebhookUrl(), payload, source, entry));
                }
            }
            


        } catch (Exception ex) {
            System.getLogger(DiscordWebhookService.class.getName()).log(System.Logger.Level.ERROR, "RSS error [" + source.getTitle() + "]: " + ex.getMessage(), ex);
        }
    }
}
