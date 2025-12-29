package com.junk.application.discordfeedhub.utils;

import com.junk.application.discordfeedhub.model.RSSSource1;
import com.rometools.rome.feed.synd.SyndEntry;
import org.json.JSONObject;

/**
 *
 * @author elmerhd
 */
public class DiscordPost {
    private final String webhookUrl;
    private final JSONObject payload;
    private final RSSSource1 source;
    private final SyndEntry entry;

    public DiscordPost(String webhookUrl, JSONObject payload, RSSSource1 source, SyndEntry entry) {
        this.webhookUrl = webhookUrl;
        this.payload = payload;
        this.source = source;
        this.entry = entry;
    }

    public String getWebhookUrl() {
        return webhookUrl;
    }

    public JSONObject getPayload() {
        return payload;
    }

    public SyndEntry getEntry() {
        return entry;
    }

    public RSSSource1 getSource() {
        return source;
    }
}
