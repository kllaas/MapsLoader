package com.klimchuk.mapsloader.services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.klimchuk.mapsloader.App;
import com.klimchuk.mapsloader.R;
import com.klimchuk.mapsloader.data.Download;
import com.klimchuk.mapsloader.progress.FragmentProgress;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;

import static android.provider.ContactsContract.CommonDataKinds.StructuredPostal.REGION;

public class DownloadService extends IntentService {

    public static final String REMOVE_TASK = "cancel_loading";

    private NotificationCompat.Builder notificationBuilder;

    private NotificationManager notificationManager;

    private Collection<Intent> intents;

    public DownloadService() {
        super("Download Service");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_action_import)
                .setContentTitle("Download")
                .setContentText("Downloading File")
                .setAutoCancel(true);
        notificationManager.notify(0, notificationBuilder.build());

        initDownload(intent.getExtras().getString(REGION));
    }

    private void initDownload(String filename) {
        Call<ResponseBody> request = App.getMapsApi().downloadMap(filename);
        try {

            downloadFile(request.execute().body());

        } catch (IOException e) {

            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }

    private void downloadFile(ResponseBody body) throws IOException {

        int count;
        byte data[] = new byte[1024 * 4];
        long fileSize = body.contentLength();

        InputStream bis = new BufferedInputStream(body.byteStream(), 1024 * 8);
        File outputFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                "file" + (new Date()).getTime() + ".zip");

        OutputStream output = new FileOutputStream(outputFile);

        long total = 0;
        long startTime = System.currentTimeMillis();
        int timeCount = 1;

        while ((count = bis.read(data)) != -1) {

            total += count;
            int totalFileSize = (int) (fileSize / (Math.pow(1024, 2)));
            double current = Math.round(total / (Math.pow(1024, 2)));

            int progress = (int) ((total * 100) / fileSize);

            long currentTime = System.currentTimeMillis() - startTime;

            Download download = new Download();
            download.setTotalFileSize(totalFileSize);

            if (currentTime > 1000 * timeCount) {

                download.setCurrentFileSize((int) current);
                download.setProgress(progress);
                sendNotification(download);
                timeCount++;
            }

            output.write(data, 0, count);
        }

        onDownloadComplete();
        output.flush();
        output.close();
        bis.close();
    }

    private void sendNotification(Download download) {

        sendIntent(download);
        notificationBuilder.setProgress(100, download.getProgress(), false);
        notificationBuilder.setContentText("Downloading file: " + download.getCurrentFileSize() + " of " + download.getTotalFileSize() + " MB");
        notificationManager.notify(0, notificationBuilder.build());
    }

    private void sendIntent(Download download) {

        Intent intent = new Intent(FragmentProgress.MESSAGE_PROGRESS);
        intent.putExtra("download", download);
        LocalBroadcastManager.getInstance(DownloadService.this).sendBroadcast(intent);
    }

    private void onDownloadComplete() {

        Download download = new Download();
        download.setProgress(100);
        sendIntent(download);

        notificationManager.cancel(0);
        notificationBuilder.setProgress(0, 0, false);
        notificationBuilder.setContentText("File Downloaded");
        notificationManager.notify(0, notificationBuilder.build());
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        notificationManager.cancel(0);
    }
}