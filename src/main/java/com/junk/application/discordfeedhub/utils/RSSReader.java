
package com.junk.application.discordfeedhub.utils;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 *
 * @author elmerhd
 */
public class RSSReader {
    
    private String rssUrl;
    
    public RSSReader(String url) {
        this.rssUrl = url;
    }
    
    public List<SyndEntry> getList() throws MalformedURLException, IllegalArgumentException, FeedException, IOException {
        URL url = new URL(rssUrl);
        SyndFeedInput input = new SyndFeedInput();
        try (XmlReader reader = new XmlReader(url)) {
            SyndFeed feed = input.build(reader);
            return feed.getEntries();
        }
    }
}
