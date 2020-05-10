package com.example.simpleapp

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.simpleapp.util.Utils
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.auth.api.phone.SmsRetrieverClient
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.Executor


class MainActivity : AppCompatActivity() {

    var locationManager: LocationManager? = null

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    private lateinit var client: SmsRetrieverClient
    private var smsRetrieverReceiver: BroadcastReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val utils = Utils(this, this)
        /** 수정 필요 */

        // utils.showDialog("권한 필요", "앱을 사용하기 위해 해당 권한 승인이 필요합니다.", "확인", "취소")
        // utils.checkPermission(Manifest.permission.CAMERA)

        btn_notification.setOnClickListener {
            createNotificationChannel(this, NotificationManagerCompat.IMPORTANCE_DEFAULT,
                false, getString(R.string.app_name), "App notification channel") // 1

            val channelId = "$packageName-${getString(R.string.app_name)}" // 2
            val title = "Notification Title"
            val content = "Notification contents"

//            val intent = Intent(baseContext, NewActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//            val pendingIntent = PendingIntent.getActivity(baseContext, 0,
//                intent, PendingIntent.FLAG_UPDATE_CURRENT)    // 3

            val builder = NotificationCompat.Builder(this, channelId)  // 4
            builder.setSmallIcon(R.drawable.ic_launcher_background)    // 5
            builder.setContentTitle(title)    // 6
            builder.setContentText(content)    // 7
            builder.priority = NotificationCompat.PRIORITY_DEFAULT    // 8
            builder.setAutoCancel(true)   // 9
            //builder.setContentIntent(pendingIntent)   // 10

            val notificationManager = NotificationManagerCompat.from(this)
            notificationManager.notify(NOTIFICATION_ID, builder.build())    // 11
        }

        btn_qr_code.setOnClickListener {
            val intentIntegrator = IntentIntegrator(this)
            intentIntegrator.setBeepEnabled(false)
            intentIntegrator.initiateScan()
        }

        btn_sms.setOnClickListener {
            registerSmsRetrieverReceiver()

            client = SmsRetriever.getClient(this).also {
                it.startSmsRetriever() // 준비가 되면 SMS Retriever를 시작시켜준다. // 인증코드 재전송시에 재호출해주어야 한다. }
            }
        }

        btn_biometric.setOnClickListener {
            executor = ContextCompat.getMainExecutor(this)
            biometricPrompt = BiometricPrompt(this, executor,
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationError(
                        errorCode: Int,
                        errString: CharSequence
                    ) {
                        super.onAuthenticationError(errorCode, errString)
                        Toast.makeText(
                            applicationContext,
                            "Authentication error: $errString", Toast.LENGTH_SHORT
                        )
                            .show()
                    }

                    override fun onAuthenticationSucceeded(
                        result: BiometricPrompt.AuthenticationResult
                    ) {
                        super.onAuthenticationSucceeded(result)
                        Toast.makeText(
                            applicationContext,
                            "Authentication succeeded!", Toast.LENGTH_SHORT
                        )
                            .show()
                    }

                    override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()
                        Toast.makeText(
                            applicationContext, "Authentication failed",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                })

            promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric login for my app")
                .setSubtitle("Log in using your biometric credential")
                .setNegativeButtonText("Use account password")
                .build()
            biometricPrompt.authenticate(promptInfo)
        }

        btn_network.setOnClickListener {
            if(utils.isNetworkConnected())
                utils.showShortSnackbar(it, "인터넷이 연결되어 있습니다.")
            else
                utils.showShortSnackbar(it, "인터넷이 연결되어 있지 않습니다.")
        }

        btn_location.setOnClickListener {
            //            utils.checkPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
//                                            Manifest.permission.ACCESS_COARSE_LOCATION,
//                                            Manifest.permission.ACCESS_BACKGROUND_LOCATION))

            utils.requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )

            val loc = utils.getLastLocation()
            if (loc != null) {
                Log.d(TAG, "${loc.latitude} ${loc.longitude}")
            } else {
                Log.d(TAG, "Location is null.")
            }
        }

        btn_camera.setOnClickListener {
            if (utils.hasPermissionFor(Manifest.permission.CAMERA)) {
                Log.d(TAG, "카메라 권한이 있습니다.")

                dispatchTakePictureIntent()
            } else {
                Log.d(TAG, "카메라 권한이 없습니다.")

                utils.checkPermission(Manifest.permission.CAMERA)
            }
        }

        btn_gallery.setOnClickListener {
            if (utils.hasPermissionFor(Manifest.permission.READ_EXTERNAL_STORAGE) &&
                utils.hasPermissionFor(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            ) {
                Log.d(TAG, "READ/WRITE STORAGE PERMISSION OK.")

                startActivityForResult(
                    utils.getIntent("image/*", Intent.ACTION_GET_CONTENT),
                    REQUEST_GALLERY
                )
            } else {
                Log.d(TAG, "READ/WRITE STORAGE PERMISSION NO.")

                utils.checkPermissions(
                    arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                )
            }
        }
    }

    override fun onDestroy() {
        unregisterSmsRetrieverReceiver()
        super.onDestroy()
    }

    /** SMS */

    private fun registerSmsRetrieverReceiver() {
        smsRetrieverReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (SmsRetriever.SMS_RETRIEVED_ACTION != intent.action) {
                    return
                }

                val extras = intent.extras ?: return
                val status = extras.get(SmsRetriever.EXTRA_STATUS) as? Status
                    ?: return

                if (status.statusCode != CommonStatusCodes.SUCCESS) {
                        return
                    }
                val message = extras.get(SmsRetriever.EXTRA_SMS_MESSAGE)
            }
        }

        registerReceiver(smsRetrieverReceiver, IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION))
    }

    private fun unregisterSmsRetrieverReceiver() {
        if (smsRetrieverReceiver != null) {
            unregisterReceiver(smsRetrieverReceiver)
            smsRetrieverReceiver = null
        }
    }

    /** Camera */

    /** Call Camera intent */
    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            imageView.setImageBitmap(imageBitmap)
        }
        if (requestCode == REQUEST_GALLERY && resultCode == RESULT_OK) {
            TODO("이미지 불러오기")
        }
    }

    /** Notification */

    private fun createNotificationChannel(context: Context, importance: Int, showBadge: Boolean,
                                          name: String, description: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "${context.packageName}-$name"
            val channel = NotificationChannel(channelId, name, importance)
            channel.description = description
            channel.setShowBadge(showBadge)

            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 1000
        private const val REQUEST_GALLERY = 1001

        private const val REQUEST_PERMISSION_GALLERY = 2000
        private const val REQUEST_PERMISSION_LOCATION = 2001

        private const val NOTIFICATION_ID = 2002

        private const val TAG = "MainActivity"
    }
}

