package com.aptPhase3.zhiyuan.android;

/**
 * Created by zou on 12/5/15.
 */
import com.google.android.gms.gcm.GcmListenerService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

public class GcmMessageHandler extends GcmListenerService {
    public static final int MESSAGE_NOTIFICATION_ID = 435345;
    Context context = this;

    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        createNotification(from, message);
        System.out.println("I receive my message: " + message);
        Intent chatIntent = new Intent(context, ChatActivity.class);
        chatIntent.putExtra("receivedMessage", message);
        chatIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(chatIntent);
    }

    // Creates notification based on title and body received
    private void createNotification(String title, String body) {
        Context context = getBaseContext();
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setContentTitle(title)
                .setContentText(body);

        NotificationManager mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(MESSAGE_NOTIFICATION_ID, mBuilder.build());
    }

}