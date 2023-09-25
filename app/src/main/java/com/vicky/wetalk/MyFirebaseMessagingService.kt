package com.vicky.wetalk

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.messaging.ktx.remoteMessage

const val channelId="notification_channel"
const val channelName="wetalks"

class MyFirebaseMessagingService: FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if(remoteMessage.getNotification()!=null){
            generateNotification(remoteMessage.notification!!.title!!, remoteMessage.notification!!.body!!)
        }
    }

    //generate the notification

    @SuppressLint("RemoteViewLayout")
    fun getRemoteView(title: String, description: String): RemoteViews{
        val remoteview=RemoteViews("com.vicky.wetalk" ,R.layout.notification)

        remoteview.setTextViewText(R.id.title,title)
        remoteview.setTextViewText(R.id.description,description)
        remoteview.setImageViewResource(R.id.app_notlogo,R.mipmap.ic_launcher)

        return remoteview
    }

    fun generateNotification(title:String,description:String){

        val intent= Intent(this,MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent=PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT)

        //channel id , channel name
        var builder: NotificationCompat.Builder = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.mipmap.ic_launcher).setAutoCancel(true).setVibrate(longArrayOf(1000,1000,1000,1000)).setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)

        builder=builder.setContent(getRemoteView(title,description))

        val notificationManager=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            val notificationChannel= NotificationChannel(channelId, channelName,NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        notificationManager.notify(0,builder.build())

    }


}