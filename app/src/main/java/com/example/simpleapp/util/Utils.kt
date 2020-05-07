package com.example.simpleapp.util

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar

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

    /** permission */

    /** 해당 권한이 있는지 판별 */
    fun hasPermissionFor(permissionName: String): Boolean = (
            ContextCompat.checkSelfPermission(context, permissionName)
                    == PackageManager.PERMISSION_GRANTED)

    /** 해당 권한에 대한 요청을 거부한 적이 있는지 판별 */
    fun deniedPermission(permissionName: String): Boolean = (
            ActivityCompat.shouldShowRequestPermissionRationale(activity, permissionName))

    /** 해당 권한 리스트를 요청 */
    fun requestPermissions(permissionArray: Array<out String>) {
        ActivityCompat.requestPermissions(activity, permissionArray, REQUEST_PERMISSIONS_CODE)
    }

    /** 해당 권한 판별 후 없을 경우 요청 */
    fun checkPermission(permissionName: String) {
        if(hasPermissionFor(permissionName)) {
            Log.d(TAG_PERMISSION, "Permission granted")
        } else {
            Log.d(TAG_PERMISSION, "Permission denied")

            if(deniedPermission(permissionName)) { // 거절한적있음
                Log.d(TAG_PERMISSION, "shouldShowRequestPermissionRationale true")

                /** 권한 필요하다는 다이얼로그 띄우기 */
                val alertDialog = AlertDialog.Builder(context)
                    .setTitle("권한 필요")
                    .setMessage("앱을 사용하기 위해 해당 권한 승인이 필요합니다.")
                    .setPositiveButton("확인") {dialog, which ->
                        requestPermissions(arrayOf(permissionName))
                    }
                    .setNegativeButton("취소", null)
                    .create()
                    .show()
            } else { // 거절한적 없음
                Log.d(TAG_PERMISSION, "shouldShowRequestPermissionRationale false")

                requestPermissions(arrayOf(permissionName))
            }

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
        return false
    }

    companion object {
        const val REQUEST_PERMISSIONS_CODE = 1000
        const val REQUEST_IMAGE_CAPTURE = 1001

        private const val TAG_PERMISSION = "Permission check"
        private const val TAG_NETWORK = "Network state check"
    }
}
