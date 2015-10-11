package cz.cvut.fit.urbanp11.main.service;

import android.content.Context;
import android.os.PowerManager;

/**
 * Created by Petr Urban on 06.05.15.
 */
public class WakeLockHelper {

    private static final String LOCK_NAME = "cz.cvut.fit.urbanp11";
    private static PowerManager.WakeLock sWakeLock = null;

    public static synchronized void acquire(Context context) {
        if (sWakeLock == null) {
            PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            sWakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, LOCK_NAME);
        }
        sWakeLock.acquire();
    }

    public static synchronized void release() {
        if (sWakeLock != null) {
            sWakeLock.release();
        }
    }

}