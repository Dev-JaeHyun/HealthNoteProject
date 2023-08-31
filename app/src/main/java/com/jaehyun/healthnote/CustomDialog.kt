package com.jaehyun.healthnote

import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.WindowManager

class CustomDialog(context: Context) {
    private val dialog = Dialog(context)

    fun showDialog(v : View?){
        dialog.setContentView(when(v?.id){
            R.id.findIdBtn -> R.layout.dialog_findid
            R.id.findPwBtn -> R.layout.dialog_findpw
            R.id.registerBtn -> R.layout.dialog_register
            else -> return
        })
        dialog.window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(true)
        dialog.show()
    }


}