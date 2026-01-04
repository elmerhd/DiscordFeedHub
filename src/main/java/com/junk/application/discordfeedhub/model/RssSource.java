package com.junk.application.discordfeedhub.model;

/**
 *
 * @author elmerhd
 */
public record RssSource(Integer id, String title, String websiteUrl, String rssUrl, String discordWebhookUrl, Boolean enabled) {}
