package cz.cvut.fit.urbanp11.main.service;

import android.app.IntentService;
import android.app.Notification;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;

/**
 * Created by Petr Urban on 06.05.15.
 */
public class DownloadService extends IntentService {

    public DownloadService() {
        super("DownloadService");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final Notification.Builder builder = new Notification.Builder(this);
        builder.setContentText("Downloading feeds").setContentTitle("Feeder reader").setTicker("Downloading feeds").setSmallIcon(android.R.drawable.stat_notify_sync_noanim);
        startForeground(10, builder.build());

            DownloadFeedsClient.download(this, new DownloadFeedsClient.Downloadable() {
                @Override
                public void onStart() {
                    builder.setTicker("start download");
                    Log.d("DownloadService", "Downloading file - onStart");
                }

                @Override
                public void onFinish() {
                    WakeLockHelper.release();
                    builder.setTicker("end download");
                    stopForeground(true);
                    Log.d("DownloadService", "Downloading file - onFinish");
                }
            });


    }

}
