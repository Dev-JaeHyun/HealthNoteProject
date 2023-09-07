package com.jaehyun.healthnote

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.view.WindowManager
import android.widget.ImageButton

class CustomDialog(context: Context) {

    private val dialog = Dialog(context)

    fun showDialog(v : View?){
        dialog.setContentView(when(v?.id){
            R.id.findIdBtn -> R.layout.dialog_findid
            R.id.findPwBtn -> R.layout.dialog_findpw
            R.id.registerBtn -> R.layout.dialog_register
            else -> return
        }) //받은 view에 따라 레이아웃 지정
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) //배경 투명화
        dialog.window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(false) //바깥쪽 배경을 눌러도 캔슬되게 하는가
        dialog.setCancelable(false) //뒤로가기 버튼을 눌러서 캔슬되게 하는가
        dialog.show()

        var dismissBtn = dialog.findViewById<ImageButton>(R.id.dismissBtn)
        dismissBtn.setOnClickListener {
            dialog.dismiss()
        }
    }

}