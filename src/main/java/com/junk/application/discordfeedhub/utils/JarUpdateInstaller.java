package com.junk.application.discordfeedhub.utils;

import com.junk.application.discordfeedhub.model.UpdateInfo;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.*;

/**
 *
 * @author elmer
 */
public class JarUpdateInstaller {

    private static final OkHttpClient client = new OkHttpClient();

    public static File downloadJar(UpdateInfo info, ProgressCallback callback) throws IOException {
        Request request = new Request.Builder().url(info.downloadUrl()).build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Download failed");
            }

            long total = response.body().contentLength();
            InputStream in = response.body().byteStream();

            File tempJar = new File(Utility.getRunningJarFile().getParentFile(), info.name());
            try (FileOutputStream out = new FileOutputStream(tempJar)) {
                byte[] buffer = new byte[8192];
                long readTotal = 0;
                int read;

                while ((read = in.read(buffer)) != -1) {
                    out.write(buffer, 0, read);
                    readTotal += read;
                    callback.onProgress(readTotal, total);
                }
            }
            return tempJar;
        }
    }

    public interface ProgressCallback {
        void onProgress(long downloaded, long total);
    }
}

