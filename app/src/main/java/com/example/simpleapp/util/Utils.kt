package com.example.simpleapp.util

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.hardware.fingerprint.FingerprintManagerCompat
import com.example.simpleapp.MainActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.Executor
import kotlin.collections.listOf as listOf1

class Utils(_context: Context, _activity: Activity) {

    private val context = _context
    private val activity = _activity

    /** Toast */

    /** 짧은 토스트 메시지 출력 */
    fun showShortToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    /** 긴 토스트 메시지 출력 */
    fun showLongToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    /** Snackbar */

    /** 짧은 스낵바 메시지 출력 */
    fun showShortSnackbar(view: View, msg: String) {
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show()
    }

    /** 긴 스낵바 메시지 출력 */
    fun showLongSnackbar(view: View, msg: String) {
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show()
    }

    /** Dialog */

    fun showDialog(title: String, message: String) {
        val alertDialog = AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("확인", null)
            .setNegativeButton("취소", null)
            .create()
            .show()
    }

    fun showDialog(title: String, message: String, pText: String, nText: String) {
        val alertDialog = AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(pText, null)
            .setNegativeButton(nText, null)
            .create()
            .show()
    }

    fun showDialog(title: String, message: String, pText: String, nText: String,
                   pListener: DialogInterface.OnClickListener,
                   nListener: DialogInterface.OnClickListener) {
        val alertDialog = AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(pText, pListener)
            .setNegativeButton(nText, nListener)
            .create()
            .show()
    }

    /** Permission */

    /** 해당 권한이 있는지 판별 */
    fun hasPermissionFor(permissionName: String): Boolean = (
            ContextCompat.checkSelfPermission(context, permissionName)
                    == PackageManager.PERMISSION_GRANTED)

    /** 해당 권한에 대한 요청을 거부한 적이 있는지 판별 */
    fun rejectPermission(permissionName: String): Boolean = (
            ActivityCompat.shouldShowRequestPermissionRationale(activity, permissionName))

    /** 해당 권한 리스트를 요청 */
    fun requestPermissions(permissionArray: Array<out String>) {
        ActivityCompat.requestPermissions(activity, permissionArray, REQUEST_PERMISSIONS_CODE)
    }

    /** 1개의 권한 체크 */
    fun checkPermission(permissionName: String) {
        if(hasPermissionFor(permissionName)) {
            Log.d(TAG_PERMISSION, "Permission is found.")
        } else {
            Log.d(TAG_PERMISSION, "Permission is not found.")

            if(rejectPermission(permissionName)) {
                Log.d(TAG_PERMISSION, "Permission is denied.")

                /** 권한 필요하다는 다이얼로그 띄우기 */
                val alertDialog = AlertDialog.Builder(context)
                    .setTitle("권한 필요")
                    .setMessage("앱을 사용하기 위해 해당 권한 승인이 필요합니다.")
                    .setPositiveButton("확인") {_, _ ->
                        requestPermissions(arrayOf(permissionName))
                    }
                    .setNegativeButton("취소", null)
                    .create()
                    .show()
            } else {
                Log.d(TAG_PERMISSION, "Permission is not denied.")

                requestPermissions(arrayOf(permissionName))
            }
        }
    }

    /** 2개 이상의 권한 체크 */
    fun checkPermissions(requestedPermissions: Array<String>) {
        var deniedPermissions = ArrayList<String>()

        for(permission in requestedPermissions) {
            if(!hasPermissionFor(permission)) {
                deniedPermissions.add(permission)
            }
        }

        if(deniedPermissions.isNotEmpty()) {
            val array = arrayOfNulls<String>(deniedPermissions.size)
            requestPermissions(deniedPermissions.toArray(array) as Array<out String>)
        }
    }

    /** Network */

    /** 네트워크 상태를 출력 */
    fun showNetworkState() {
        if(isNetworkConnected()) {
            showShortToast("인터넷이 연결되어 있지 않습니다.")
        } else {
            showShortToast("인터넷이 연결되어 있습니다.")
        }
    }

    /** 네트워크 상태를 판별 */
    fun isNetworkConnected(): Boolean {
        val connectivityManger
                = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val nw = connectivityManger.activeNetwork ?: return false
            val actNw = connectivityManger.getNetworkCapabilities(nw) ?: return false
            return when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                else -> false
            }
        } else {
            val nwInfo = connectivityManger.activeNetworkInfo ?: return false
        }

        return true
    }

    /** Storage */

    fun getIntent(type: String, action: String): Intent {
        val intent = Intent()
        intent.type= type
        intent.action = action

        return intent
    }

    /** Location */

    /** 현재 위치를 불러옴 */
    fun getLastLocation(): Location? {
        var lastLocation: Location? = null
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->
                lastLocation = location!!
            }
        return lastLocation
    }

    /** Biometric */

    companion object {
        const val REQUEST_PERMISSIONS_CODE = 1000
        const val REQUEST_IMAGE_CAPTURE = 1001

        private const val TAG_PERMISSION = "Permission check"
        private const val TAG_NETWORK = "Network state check"
    }
}
