package com.example.timer2

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.*

class PomodoroTimerService : Service() {
    private var timerJob: Job? = null
    private lateinit var notificationManager: NotificationManager
    private val CHANNEL_ID = "PomodoroTimerChannel"
    private val NOTIFICATION_ID = 1

    override fun onCreate() {
        super.onCreate()
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            "START" -> {
                val duration = intent.getLongExtra("DURATION", 0L)
                startForegroundService(duration)
            }
            "STOP" -> stopSelf()
        }
        return START_NOT_STICKY
    }

    private fun startForegroundService(duration: Long) {
        val notification = createNotification("Pomodoro Timer Running", "Time left: ${formatTime(duration)}")
        startForeground(NOTIFICATION_ID, notification)

        timerJob = CoroutineScope(Dispatchers.Default).launch {
            var timeLeft = duration
            while (timeLeft > 0) {
                delay(1000)
                timeLeft -= 1000
                updateNotification("Time left: ${formatTime(timeLeft)}")
            }
            stopSelf()
        }
    }

    private fun createNotification(title: String, content: String): Notification {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(content)
            .setSmallIcon(R.drawable.ic_notification) // Make sure to add this icon to your drawable resources
            .setContentIntent(pendingIntent)
            .build()
    }

    private fun updateNotification(content: String) {
        val notification = createNotification("Pomodoro Timer Running", content)
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Pomodoro Timer Channel",
                NotificationManager.IMPORTANCE_LOW
            )
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun formatTime(timeInMillis: Long): String {
        val minutes = timeInMillis / 60000
        val seconds = (timeInMillis % 60000) / 1000
        return String.format("%02d:%02d", minutes, seconds)
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        timerJob?.cancel()
    }
}