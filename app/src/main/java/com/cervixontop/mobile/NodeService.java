package com.cervixontop.mobile;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class NodeService extends Service {
    private static final String CHANNEL = "cervix_node";
    private static final int ID = 1401;
    private Thread nodeThread;
    private volatile boolean started = false;

    @Override public void onCreate() {
        super.onCreate();
        createChannel();
        startForeground(ID, notification());
    }

    @Override public int onStartCommand(Intent intent, int flags, int startId) {
        if (!started) {
            started = true;
            nodeThread = new Thread(() -> {
                try {
                    String dir = getFilesDir().getAbsolutePath() + "/nodejs-project";
                    File target = new File(dir);
                    if (!target.exists()) copyAssetFolder("nodejs-project", target);
                    String main = new File(target, "main.js").getAbsolutePath();
                    NodeRuntime.startNode(new String[]{"node", main});
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }, "CervixOnTop-Node");
            nodeThread.start();
        }
        return START_STICKY;
    }

    private Notification notification() {
        if (Build.VERSION.SDK_INT >= 26) {
            return new Notification.Builder(this, CHANNEL)
                    .setContentTitle("CervixOnTop activo")
                    .setContentText("Motor de Minecraft ejecutándose")
                    .setSmallIcon(android.R.drawable.stat_sys_download_done)
                    .setOngoing(true).build();
        }
        return new Notification.Builder(this)
                .setContentTitle("CervixOnTop activo")
                .setContentText("Motor de Minecraft ejecutándose")
                .setSmallIcon(android.R.drawable.stat_sys_download_done)
                .setOngoing(true).build();
    }

    private void createChannel() {
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel c = new NotificationChannel(CHANNEL, "CervixOnTop", NotificationManager.IMPORTANCE_LOW);
            ((NotificationManager)getSystemService(NOTIFICATION_SERVICE)).createNotificationChannel(c);
        }
    }

    private void copyAssetFolder(String assetPath, File out) throws IOException {
        String[] children = getAssets().list(assetPath);
        if (children == null || children.length == 0) {
            out.getParentFile().mkdirs();
            try (java.io.InputStream in = getAssets().open(assetPath); FileOutputStream os = new FileOutputStream(out)) {
                byte[] b = new byte[8192]; int n; while ((n = in.read(b)) != -1) os.write(b, 0, n);
            }
            return;
        }
        if (!out.exists()) out.mkdirs();
        for (String child : children) copyAssetFolder(assetPath + "/" + child, new File(out, child));
    }

    @Override public void onDestroy() { started = false; super.onDestroy(); }
    @Override public IBinder onBind(Intent intent) { return null; }
}
