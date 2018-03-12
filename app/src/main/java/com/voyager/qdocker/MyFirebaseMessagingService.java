package com.voyager.qdocker;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * This class is called when any push notification is received.
 * On receiving push notification, it builds the notification to show up on the device status bar.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    public static String ARTIFACT = "artefact", EVENT = "event", GENERAL = "general";
    private static final String TAG = "MyFirebaseMsgService";
    private String notificationType;
    String church_name;

    Bitmap bitmap;

    public static final String FaithApp_PREFERENCES = "FaithApp_Prefs";




    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.i("PVL", "MESSAGE RECEIVED!!");
        if (remoteMessage.getData() != null) {/*
            Log.i("PVL", "RECEIVED MESSAGE: " + remoteMessage.getNotification().getBody());
            System.out.println("RemoteMessage -- B"+remoteMessage.getNotification().getTitle());
            System.out.println("RemoteMessage -- B"+remoteMessage.getNotification().getBody());
            System.out.println("RemoteMessage -- B"+remoteMessage.getNotification().getClickAction())*/;
            System.out.println("RemoteMessage -- M"+remoteMessage.getData().get("message"));
        } else {
            System.out.println("RemoteMessage -- M"+remoteMessage.getData().get("message"));
            Log.i("PVL", "RECEIVED MESSAGE: " + remoteMessage.getData().get("message"));
        }
       // showNotification(remoteMessage.getData().get("message"));
        if(remoteMessage.getData().size() > 0) {
            System.out.println("RemoteMessage --"+remoteMessage.getData());
            //Log.e("fcm: ", remoteMessage.getData().get("message"));
      /*      try {
              *//*  JSONObject jsonObject = new JSONObject(remoteMessage.getData().get("message"));
                String to = jsonObject.getString("to");
                if (to.equals("user")) {
                    SharedPreferences sharedPreferences = getSharedPreferences(Helper.UserDetails, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("trip_user", jsonObject.toString());
                    editor.apply();
                    Intent intent1 = new Intent(getApplicationContext(), UserRequestStatusActivity.class);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent1);
                } else if (to.equals("driver")) {
                    String reqId = jsonObject.get("id").toString();
                    Intent intent = new Intent(getApplicationContext(), DriverAlertActivity.class);
                    intent.putExtra("id", reqId);
                    intent.putExtra("json", jsonObject.toString());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else if (to.equals("user_notice")) {
                    //String reqId = jsonObject.get("id").toString();
                    Intent intent = new Intent(getApplicationContext(), UserTripEndActivity.class);
                    intent.putExtra("json", jsonObject.toString());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }*//*
            } catch (JSONException e) {
                e.printStackTrace();
            }*/
        }
    }

    private void showNotification(String message) {

        Intent i = new Intent(getApplicationContext(),MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setContentTitle("FCM Test")
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_build_white_36dp)
                .setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        manager.notify(0,builder.build());
    }

    // [END receive_message]

/*
    private void setEventAlarm(String alarmDate, String alarmTime, String eventTitle){
        System.out.println("EventService inside setEventAlarm()");

        int day, month, year;

        String eventDate[] = alarmDate.split("/");
        day = Integer.parseInt(eventDate[0]);
        month = Integer.parseInt(eventDate[1]);
        year = Integer.parseInt(eventDate[2]);

        String eventTime[] = alarmTime.split(":");
        System.out.println("EventService alarmDate alarTime "+ alarmDate + " ***** " + alarmTime);
        System.out.println("EventService Day Month "+ day + " ***** " + month);
        System.out.println("EventService Hour Minutes Seconds "+ eventTime[0] + " ***** " + eventTime[1] + " ***** " + eventTime[2]);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.YEAR, year);

        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(eventTime[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(eventTime[1]));
        calendar.set(Calendar.SECOND, Integer.parseInt(eventTime[2]));

        Calendar currentTime = Calendar.getInstance();
        currentTime.setTimeInMillis(System.currentTimeMillis());

        //long triggerTime = calendar.getTimeInMillis() - System.currentTimeMillis();
        long triggerTime = calendar.getTimeInMillis() - currentTime.getTimeInMillis();

        System.out.println("EventService currentTime "+ currentTime.getTimeInMillis());
        System.out.println("EventService alarmTime "+ calendar.getTimeInMillis());
        System.out.println("EventService triggerTime "+ triggerTime);

        System.out.println("EventService *****************************************************");

        System.out.println("EventService currentTime "+ new Date(currentTime.getTimeInMillis()));
        System.out.println("EventService alarmTime "+ new Date(calendar.getTimeInMillis()));
        System.out.println("EventService triggerTime "+ new Date(triggerTime));

        Intent alarmIntent = new Intent(getApplicationContext(), EventService.class);
        alarmIntent.putExtra("EventTitle", eventTitle);
        PendingIntent alarmPendingIntent = PendingIntent.getService(getBaseContext(), (int)(System.currentTimeMillis()%10000), alarmIntent, 0);

        AlarmManager alarmManager = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmPendingIntent);

    }
*/

/*
    private void sendArtifactNotification(String church_id, String churchTitle, String messageTitle, String messageBody, Bitmap img){
        try {
            Intent intent = new Intent(this, Faith_Landing_Card_View_item_click_v2.class);
            intent.putExtra("card_view_id", church_id);
            intent.putExtra("notification_title", messageTitle);
            intent.putExtra("notification_message", messageBody);
            System.out.println("****** "+messageTitle);
            intent.putExtra("notification_type", ARTIFACT);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setAction(Long.toString(System.currentTimeMillis()));
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 */
/* Request code *//*
, intent, PendingIntent.FLAG_ONE_SHOT);

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(getNotificationIcon())
                    .setLargeIcon(img)
                    .setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary))
                    .setContentTitle(messageTitle)
                    .setSubText(churchTitle)
                    .setContentText(Html.fromHtml(messageBody))
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(Html.fromHtml(messageBody)))
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify((int) System.currentTimeMillis(), notificationBuilder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/

/*
    private void sendArtifactNotificationBanner(String church_id, String churchTitle, String messageTitle, String messageBody, Bitmap img, Bitmap image){
        try {
            Intent intent = new Intent(this, Faith_Landing_Card_View_item_click_v2.class);
            intent.putExtra("card_view_id", church_id);
            intent.putExtra("notification_title", messageTitle);
            intent.putExtra("notification_message", messageBody);
            System.out.println("****** "+messageTitle);
            intent.putExtra("notification_type", ARTIFACT);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setAction(Long.toString(System.currentTimeMillis()));
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 */
/* Request code *//*
, intent, PendingIntent.FLAG_ONE_SHOT);

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(getNotificationIcon())
                    .setLargeIcon(img)
                    .setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary))
                    .setContentTitle(messageTitle)
                    .setSubText(churchTitle)
                    .setContentText(Html.fromHtml(messageBody))
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(image))*/
/*Notification with Image*//*

                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify((int) System.currentTimeMillis(), notificationBuilder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/


    /*
    *To get a Bitmap image from the URL received
    * */
    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;

        }
    }


    public Bitmap getCroppedBitmap(Bitmap bitmap) {
        try {
            Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                    bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);

            final int color = 0xff424242;
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
            canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                    bitmap.getWidth() / 2, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);
            //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
            //return _bmp;
            return output;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isJSONValid(String test) {
        try {
            new JSONObject(test);
        } catch (JSONException ex) {
            // edited, to include @Arthur's comment
            // e.g. in case JSONArray is valid as well...
            try {
                new JSONArray(test);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }

}