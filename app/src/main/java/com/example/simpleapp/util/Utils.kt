package com.example.simpleapp.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.startActivityForResult
import com.google.android.material.snackbar.Snackbar

class Utils {

    companion object {
        private val PERMISSION_ID = 1000
        private val REQUEST_IMAGE_CAPTURE = 2000
    }

    /**
     * Toast
     */
    fun showShortToast(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    fun showLongToast(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }

    /**
     * Snackbar
     */
    fun showShortSnackbar(view: View, msg: String) {
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show()
    }

    fun showLongSnackbar(view: View, msg: String) {
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show()
    }

    /**
     * Dialog
     */
    fun createAlertDialog(context: Context) {
        val alertDialog = AlertDialog.Builder(context)
            .setTitle("제목")
            .setPositiveButton("저장") { dialog, which ->
                Toast.makeText(context, "긍정", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("취소", null)
            .create()
            .show()
    }

    /**
     * SharedPreferences
     */
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