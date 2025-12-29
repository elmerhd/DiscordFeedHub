package com.junk.application.discordfeedhub.utils;

import com.junk.application.discordfeedhub.model.RssSource;
import com.rometools.rome.feed.synd.SyndEntry;
import java.io.IOException;
import java.util.UUID;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONObject;

/**
 *
 * @author elmerhd
 */
public class DiscordWebhookService {
    private static final OkHttpClient client = new OkHttpClient();

    public static void send(String webhookUrl, JSONObject payload, RssSource source, SyndEntry entry) throws IOException {

        RequestBody body = RequestBody.create(
                payload.toString(),
                MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
                .url(webhookUrl)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                System.err.println(payload.toString());
                throw new IOException("Discord webhook failed: " + response.code());
            } else {
                DatabaseManager.markAsPosted(source.getId(), UUID.randomUUID().toString(), entry.getLink());
            }
        }
    }
}
