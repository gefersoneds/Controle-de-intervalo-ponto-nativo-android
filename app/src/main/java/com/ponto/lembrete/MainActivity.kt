package com.ponto.lembrete

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    companion object {
        const val ALARM_REQUEST_CODE = 1001
        const val LUNCH_DURATION_MILLIS = 72 * 60 * 1000L // 1h12min = 72 minutos
        const val NOTIFICATION_PERMISSION_CODE = 100
    }

    private lateinit var btnPonto: Button
    private lateinit var tvStatus: TextView
    private lateinit var tvInfo: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnPonto = findViewById(R.id.btnPonto)
        tvStatus = findViewById(R.id.tvStatus)
        tvInfo = findViewById(R.id.tvInfo)

        requestNotificationPermission()

        btnPonto.setOnClickListener {
            iniciarContagemAlmoco()
        }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    NOTIFICATION_PERMISSION_CODE
                )
            }
        }
    }

    private fun iniciarContagemAlmoco() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(this, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            ALARM_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val triggerTime = SystemClock.elapsedRealtime() + LUNCH_DURATION_MILLIS

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.ELAPSED_REALTIME_WAKEUP,
                        triggerTime,
                        pendingIntent
                    )
                } else {
                    alarmManager.setAndAllowWhileIdle(
                        AlarmManager.ELAPSED_REALTIME_WAKEUP,
                        triggerTime,
                        pendingIntent
                    )
                }
            } else {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    triggerTime,
                    pendingIntent
                )
            }
        } catch (e: SecurityException) {
            alarmManager.setAndAllowWhileIdle(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                triggerTime,
                pendingIntent
            )
        }

        tvStatus.text = "🍽️ Bom almoço!"
        tvStatus.visibility = View.VISIBLE
        tvInfo.text = "Você será avisado em 1h12min.\nPode fechar o app tranquilamente."
        tvInfo.visibility = View.VISIBLE
        btnPonto.isEnabled = false
        btnPonto.text = "⏳ Contando..."
        btnPonto.alpha = 0.5f

        Toast.makeText(this, "Bom almoço! Alarme definido para 1h12min.", Toast.LENGTH_LONG).show()

        btnPonto.postDelayed({
            moveTaskToBack(true)
        }, 2500)
    }

    override fun onResume() {
        super.onResume()
        resetUI()
    }

    private fun resetUI() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            ALARM_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )

        if (pendingIntent == null) {
            btnPonto.isEnabled = true
            btnPonto.text = "🕐 BATER PONTO\nInício do Almoço"
            btnPonto.alpha = 1.0f
            tvStatus.visibility = View.GONE
            tvInfo.text = "Pressione o botão ao iniciar seu almoço"
            tvInfo.visibility = View.VISIBLE
        }
    }
}
