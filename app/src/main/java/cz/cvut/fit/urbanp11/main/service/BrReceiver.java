package cz.cvut.fit.urbanp11.main.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Petr Urban on 06.05.15.
 */
public class BrReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("BrReceiver", "running");
        WakeLockHelper.acquire(context);
        context.startService(new Intent(context, DownloadService.class));
    }
}
