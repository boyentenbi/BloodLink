
package samples.exoguru.bloodlink;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.util.logging.Level;
import java.util.logging.Logger;

public class GcmIntentService extends IntentService {

    private static MainActivity mainActivity;

    public GcmIntentService() {
        super("GcmIntentService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        Log.i("onHandleIntent", "got gcm instance and message type");

        // has effect of unparcelling Bundle
// Since we're not using two way messaging, this is all we really to check for
        if (extras != null && !extras.isEmpty()) {
            if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                Logger.getLogger("GCM_RECEIVED").log(Level.INFO, extras.toString());

                String json = extras.getString("message");

                showToast("Received a new alert");

                Log.i("onHandleIntent", json);

                // Use rudimentary parser because Gson crashes the app
                String[] fields = json.split("/");

                String callerName = fields[0];
                Long startDateInMillis = Long.parseLong(fields[1]);
                String hospitalName = fields[2];
                String bloodType = fields[3];
                int totalResponsesNeeded = Integer.parseInt(fields[4]);
                String requestMessage = fields[5];
                Long requestEndDateInMillis = Long.parseLong(fields[6]);
                String actualCode = fields[7];

                String thanksMessage = "Thanks for donating to " + hospitalName + " via BloodLink!";

                Incident newAlert = new Incident(callerName, hospitalName, bloodType, totalResponsesNeeded, requestMessage, thanksMessage,
                        requestEndDateInMillis, actualCode, startDateInMillis);

                /*
                Gson gson = new Gson();


                Incident newIncident = gson.fromJson(json, Incident.class);

                Log.i("onHandleIntent", "created the new Incident!");
*/
                try {
                    IncidentsList.insertToFile(newAlert, IncidentsList.alertsFileName);
                } catch (Exception Ex) {
                    Log.e("onHandleIntent", (Ex.getMessage() == null) ? "Null message" : Ex.getMessage());
                }

                Intent updateActivity = new Intent("updateActivity");

                //send broadcast
                getBaseContext().sendBroadcast(updateActivity);

                // make notification if it matches
                if (IncidentsList.doesBloodMatch(bloodType)) {

                    makeNotification("Call for " + bloodType + " blood issued",
                            hospitalName + " called for " + bloodType + " blood. Respond quickly!",
                            R.mipmap.ic_heart);
                }

            }
        }
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    protected void showToast(final String message) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void makeNotification(String title, String text, int iconId) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(alarmSound);
        builder.setVibrate(new long[]{1000, 250, 60, 75, 1000, 250, 60, 75});
        builder.setLights(Color.RED, 200, 1000);
        builder.setAutoCancel(true);
        builder.setSmallIcon(iconId);
        builder.setContentTitle(title);
        builder.setContentText(text);
        Intent resultIntent = new Intent(this, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);

        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(resultPendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // notificationID allows you to update the notification later on.
        notificationManager.notify(0, builder.build());

    }

}