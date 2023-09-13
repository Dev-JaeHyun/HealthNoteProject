package com.jaehyun.healthnote

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jaehyun.healthnote.ui.theme.HealthNoteTheme

class MainActivity : ComponentActivity() {

    fun popUpActivity(v : View?){
        //예외상황 함수 강제종료
        val dialog = CustomDialog(this)
        dialog.showDialog(v)


    }

    fun TestCall(v : View?){
        Log.d("테스트","키값")
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

}