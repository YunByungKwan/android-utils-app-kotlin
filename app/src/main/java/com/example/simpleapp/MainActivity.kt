package com.example.simpleapp

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import com.example.simpleapp.util.Utils
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val utils = Utils(this, this) /** 수정 필요 */



        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // utils.showDialog("권한 필요", "앱을 사용하기 위해 해당 권한 승인이 필요합니다.", "확인", "취소")
        // utils.checkPermission(Manifest.permission.CAMERA)

        btn_network.setOnClickListener {
            utils.showNetworkState()
        }

        btn_camera.setOnClickListener {
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                takePictureIntent.resolveActivity(packageManager)?.also {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }

        btn_gallery.setOnClickListener {

        }
    }


    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 1000
    }
}