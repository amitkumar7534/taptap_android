package com.example.myapplication.fcm

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.myapplication.activities.NotificationActivity
import com.example.myapplication.activities.SplashActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.yookoo.discoveraddis.R
import java.util.*

class FirebaseMessageingService : FirebaseMessagingService() {
    var type: String = ""
    override fun onMessageReceived(remoteMessage: RemoteMessage) {



        Log.e("sadadfffd", remoteMessage.notification?.title.toString())
        Log.e("sadadfffd", remoteMessage.notification?.body.toString())
        sendNotification(remoteMessage.notification?.title.toString(),
            remoteMessage.notification?.body.toString())



//        val notification: Notification = NotificationCompat.Builder(this)
//            .setContentTitle(remoteMessage.data?.get("type").toString())
//            .setContentText(remoteMessage.data?.get("type").toString())
//            .setSmallIcon(R.mipmap.app_icon)
//            .build()
//        val manager = NotificationManagerCompat.from(applicationContext)
//        manager.notify(123, notification)

//        if (remoteMessage.data.isNotEmpty()) {
//            try {
//                type= remoteMessage.data?.get("type").toString()
//                if (type.equals("Task_add")){
//                    sendNotification("New Task","New task added!")
//                }else if (type.equals("Nutriation plan")){
//                    sendNotification("remoteMessage.data","New nutriation plan added!")
//                }else if (type.equals("Workout_add")){
//                    sendNotification("Workout","New workout added")
//                }else if (type.equals("Program_add")){
//                    sendNotification("New Program","New program added")
//                }else if (type.equals("Shop_add")){
//                    sendNotification("Shopping List","New item added in shopping list")
//                }else if (type.equals("CHAT_MESSAGE")){
//                    sendNotification("Message","New chat message")
//                }else {
//                    sendNotification("New Notification","New Notification")
//                }
//            } catch (e: Exception) {
//                Log.e("erroorrr", e.toString())
//            }
//        }

    }

    /*Token Refresh*/
    override fun onNewToken(token: String) {
    }

    @SuppressLint("NewApi", "LogNotTimber")
    private fun sendNotification(title: String, messageBody: String) {
        try {
            val intent = Intent(this, NotificationActivity::class.java)
            sendNotification(title, messageBody, intent)
        } catch (t: Throwable) {
            t.localizedMessage
        }
    }

    private fun sendNotification(title: String?, body: String?, intent: Intent) {
        val pendingIntent: PendingIntent
        pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE)
        } else {
            PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
            )
        }
        val channelId = getString(R.string.channel_id)
        var notification: Uri? = null
        try {
            notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val r = RingtoneManager.getRingtone(applicationContext, notification)
            r.play()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val notificationBuilder =
            NotificationCompat.Builder(this, channelId)
                .setSmallIcon(getNotificationIcon())
                .setColor(resources.getColor(R.color.white))
                .setContentTitle(title)
                .setAutoCancel(true)
                .setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText(body))
                .setVibrate(longArrayOf(500, 1000))
                .setContentText(body)
                .setDefaults(Notification.DEFAULT_ALL)
//               .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setWhen(System.currentTimeMillis())
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val mChannel: NotificationChannel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

//            Uri soundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
//                    getApplicationContext().getPackageName() + "/" + R.raw.notification);

            mChannel = NotificationChannel(
                channelId,
                "SideHustle Channel",
                NotificationManager.IMPORTANCE_HIGH)

            mChannel.lightColor = Color.BLUE
            mChannel.enableLights(true)
            mChannel.setShowBadge(true)
            mChannel.vibrationPattern = longArrayOf(500, 1000)
            mChannel.enableVibration(true)

            // Allow lockscreen playback control
            mChannel.lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build()
            //mChannel.setSound(soundUri, audioAttributes);
            notificationManager?.createNotificationChannel(mChannel)

            assert(notificationManager != null)
            val t = Random()
            val notificationId = t.nextInt(10)
            notificationManager.notify(1, notificationBuilder.build())
        }
    }

    private fun getNotificationIcon(): Int {
        val useWhiteIcon = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
        return if (useWhiteIcon) R.mipmap.ic_app else R.mipmap.ic_app
    }


}