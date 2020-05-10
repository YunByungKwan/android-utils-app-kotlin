package com.example.simpleapp.util

import android.media.MediaRecorder
import android.os.Environment
import android.widget.Toast
import java.io.IOException
import java.util.*

class AudioRecorder {

    private var output: String? = null
    private var mediaRecorder: MediaRecorder? = null
    private var state: Boolean = false

    fun startRecording() {
        val fileName: String = Date().getTime().toString() + ".mp3"
        output = Environment.getExternalStorageDirectory().absolutePath + "/Download/" + fileName //내장메모리 밑에 위치
        mediaRecorder = MediaRecorder()
        mediaRecorder?.setAudioSource((MediaRecorder.AudioSource.MIC))
        mediaRecorder?.setOutputFormat((MediaRecorder.OutputFormat.MPEG_4))
        mediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        mediaRecorder?.setOutputFile(output)

        try {
            mediaRecorder?.prepare()
            mediaRecorder?.start()
            state = true
            //Toast.makeText(this, "레코딩 시작되었습니다.", Toast.LENGTH_SHORT).show()
        } catch (e: IllegalStateException){
            e.printStackTrace()
        } catch (e: IOException){
            e.printStackTrace()
        }
    }

    fun stopRecording() {
        if(state){
            mediaRecorder?.stop()
            mediaRecorder?.reset()
            mediaRecorder?.release()
            state = false
            //Toast.makeText(this, "중지 되었습니다.", Toast.LENGTH_SHORT).show()
        } else {
            //Toast.makeText(this, "레코딩 상태가 아닙니다.", Toast.LENGTH_SHORT).show()
        }
    }
}