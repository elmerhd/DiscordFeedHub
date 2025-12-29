package com.junk.application.discordfeedhub.model;

/**
 *
 * @author elmerhd
 */
public class RssSource {
    
    private int id;
    private String title;
    private String websiteUrl;
    private String rssUrl;
    private String discordWebhookUrl;
    private boolean enabled;

    public RssSource(int id, String title, String websiteUrl, String rssUrl, String discordWebhookUrl, boolean isEnabled) {
        this.id = id;
        this.title = title;
        this.websiteUrl = websiteUrl;
        this.rssUrl = rssUrl;
        this.discordWebhookUrl = discordWebhookUrl;
        this.enabled = isEnabled;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public String getRssUrl() {
        return rssUrl;
    }

    public String getDiscordWebhookUrl() {
        return discordWebhookUrl;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public void setRssUrl(String rssUrl) {
        this.rssUrl = rssUrl;
    }

    public void setDiscordWebhookUrl(String discordWebhookUrl) {
        this.discordWebhookUrl = discordWebhookUrl;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
