package com.junk.application.discordfeedhub.utils;

import com.junk.application.discordfeedhub.model.UpdateInfo;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class GitHubUpdateChecker {

    private static final OkHttpClient client = new OkHttpClient();
    
    public static UpdateInfo checkForUpdate(String currentVersion) throws IOException {
        Request request = new Request.Builder()
                .url(Constants.LATEST_RELEASE_API)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("GitHub API error: " + response.code());
            }

            JSONObject json = new JSONObject(response.body().string());

            String tag = json.getString("tag_name").replace("v", "");
            boolean newer = isNewer(tag, currentVersion);

            if (!newer) {
                return null;
            }

            JSONArray assets = json.getJSONArray("assets");
            if (assets.isEmpty()) {
                return null;
            }

            JSONObject asset = assets.getJSONObject(0);
            String name = asset.getString("name");
            String downloadUrl = asset.getString("browser_download_url");
            String publishedDate = asset.getString("created_at");
            long fileSize = asset.getLong("size");
            int downloadCount = asset.getInt("download_count");
            
            return new UpdateInfo(name, tag, downloadUrl, publishedDate, fileSize, downloadCount);
        }
    }

    private static boolean isNewer(String remote, String local) {
        String[] r = remote.split("\\.");
        String[] l = local.split("\\.");

        for (int i = 0; i < Math.max(r.length, l.length); i++) {
            int rv = i < r.length ? Integer.parseInt(r[i]) : 0;
            int lv = i < l.length ? Integer.parseInt(l[i]) : 0;

            if (rv > lv) return true;
            if (rv < lv) return false;
        }
        return false;
    }
}
