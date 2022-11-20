package com.udacity

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.udacity.util.sendNotification
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager
    private var checkBox = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        // initialize the notification manager
        notificationManager = this.getSystemService(
            NotificationManager::class.java
        ) as NotificationManager

        // create Notification channel
        createChannel(
            getString(R.string.notification_channel_id), CHANNEL_ID
        )

        // check the radio buttons
        radio_group.setOnCheckedChangeListener { group, checkedID ->
            checkBox = true
            when (checkedID) {
                R.id.glide_btn -> {
                    fileName = getString(R.string.radio_glide)
                    messageBody = "Downloading Glide"
                }
                R.id.load_btn -> {
                    fileName = getString(R.string.radio_loadApp)
                    messageBody = "Downloading current repository"
                }
                R.id.retrofit_btn -> {
                    fileName = getString(R.string.radio_retrofit)
                    messageBody = "Downloading retrofit"
                }
                else -> checkBox = false
            }
        }


        custom_button.setOnClickListener {
            if (checkBox) {
                download()
                registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
            } else {
                Toast.makeText(
                    this, "Please select the file to download", Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)

            if (id == downloadID) {
                if (checkDownloadStatus() == DownloadManager.STATUS_SUCCESSFUL) {
                    Toast.makeText(this@MainActivity, "Download Succeed", Toast.LENGTH_SHORT).show()
                    downloadStatus = "Success"
                    notificationManager.sendNotification(messageBody, applicationContext)
                } else {
                    Toast.makeText(this@MainActivity, "failed", Toast.LENGTH_SHORT).show()
                    downloadStatus = "Failure"
                    notificationManager.sendNotification(messageBody, applicationContext)
                    Log.i("Adham", "failed ${checkDownloadStatus()}")
                }
            }
        }
    }

    private fun download() {
        val uri = Uri.parse(URL)
        val request =
            DownloadManager.Request(uri)
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)
        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.
    }

    companion object {
        var messageBody = ""
        var fileName = ""
        var downloadStatus = ""
        private const val URL =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val CHANNEL_ID = "channelId"
    }

    fun checkDownloadStatus(): Int {
        val query = DownloadManager.Query()
        query.setFilterById(downloadID)
        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        val cursor = downloadManager.query(query)


        if (cursor.moveToNext()) {
            val status = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
            return status
        }
        return cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
    }

    private fun createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId, channelName, NotificationManager.IMPORTANCE_LOW
            ).apply {
                setShowBadge(false)
                enableLights(true)
                lightColor = Color.RED
                enableVibration(true)
                description = "Download"
            }
            notificationManager.createNotificationChannel(notificationChannel)

        }
    }
}
