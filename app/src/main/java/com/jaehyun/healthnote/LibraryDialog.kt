package com.jaehyun.healthnote

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.WindowManager
import android.widget.TextView
import com.jaehyun.healthnote.dataclass.Exercise
import com.jaehyun.healthnote.dataclass.ExerciseInfoResponse

class LibraryDialog(context: Context, val exer: ExerciseInfoResponse) {
    private val dialog = Dialog(context)

    fun showDialog(){
        dialog.setContentView(R.layout.dialog_exerinfo)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) //배경 투명화
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog.setCanceledOnTouchOutside(true) //바깥쪽 배경을 눌러도 캔슬되게 하는가
        dialog.setCancelable(true) //뒤로가기 버튼을 눌러서 캔슬되게 하는가
        dialog.show()


        dialog.findViewById<TextView>(R.id.exerciseName).text = exer.exerciseName
        dialog.findViewById<TextView>(R.id.exerDescription).text = exer.exerciseExplanation
        dialog.findViewById<TextView>(R.id.exerUrl).text = "참고영상: " + exer.exerciseUrl

    }


}