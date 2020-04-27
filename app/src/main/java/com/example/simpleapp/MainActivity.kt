package com.example.simpleapp

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.simpleapp.util.Utils
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val utils = Utils()

        toastButton1.setOnClickListener {
            utils.showShortToast(this, "short toast")
        }

        toastButton2.setOnClickListener {
            utils.showLongToast(this, "long toast")
        }

        snackbarButton1.setOnClickListener {
            utils.showShortSnackbar(it, "short snackbar")
        }

        snackbarButton2.setOnClickListener {
            utils.showLongSnackbar(it, "long snackbar")
        }

        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.dialog, null)

        val alertDialog = AlertDialog.Builder(this)
            .setTitle("제목")
            .setPositiveButton("저장") { dialog, which ->
                Toast.makeText(applicationContext, "긍정", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("취소", null)
            .create()

        alertDialog.setView(view)
        alertDialog.show()
        /*
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("제목")
        dialog.setMessage("친구목록에서 지우시겠습니까?")
        val dialog_listener = DialogInterface.OnClickListener { dialog, which ->
            when(which) {
                DialogInterface.BUTTON_POSITIVE ->
                    Toast.makeText(applicationContext, "긍정", Toast.LENGTH_SHORT).show()
                DialogInterface.BUTTON_NEGATIVE ->
                    Toast.makeText(applicationContext, "부정", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.setPositiveButton("YES",dialog_listener)
        dialog.setNegativeButton("NO",dialog_listener)
        dialog.show()
        */

        /**
         * SharedPreferences
         */

    }
}
