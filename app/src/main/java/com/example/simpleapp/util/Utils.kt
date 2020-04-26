package com.example.simpleapp.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.startActivityForResult

class Utils {

    companion object {
        private val PERMISSION_ID = 1000
        private val REQUEST_IMAGE_CAPTURE = 2000
    }

    private fun isNetworkConnected() {

    }

    private fun checkPermissions(context: Context): Boolean {
        if(ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            return true
        }

        return false
    }

    private fun requestPermissions(activity: Activity) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_ID
        )
    }

    private fun dispatchTakePictureIntent(pm: PackageManager, activity: Activity) {

    }



    private fun gps() {

    }

    private fun fileupload() {

    }

}