package com.jaehyun.healthnote

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.jaehyun.healthnote.databinding.DialogSettingBinding
import com.jaehyun.healthnote.dataclass.DeleteAccountResponse
import com.jaehyun.healthnote.dataclass.ExerciseResetResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SettingDialog(context: Context){
    private val context = context
    private val dialog = Dialog(context)

    fun showDialog(){

        dialog.setContentView(R.layout.dialog_setting)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) //배경 투명화
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog.setCanceledOnTouchOutside(false) //바깥쪽 배경을 눌러도 캔슬되게 하는가
        dialog.setCancelable(false) //뒤로가기 버튼을 눌러서 캔슬되게 하는가
        dialog.show()

        val dismissBtn = dialog.findViewById<ImageButton>(R.id.dismissBtn)
        dismissBtn.setOnClickListener{
            dialog.dismiss()
        }

        //데이터 초기화 버튼
        val resetBtn = dialog.findViewById<TextView>(R.id.resetData)
        resetBtn.setOnClickListener {
            val builder = AlertDialog.Builder(context)
                .setTitle("데이터 초기화")
                .setMessage("운동 기록이 초기화 됩니다.\n진행하시겠습니까?")
                .setPositiveButton("확인",
                    DialogInterface.OnClickListener { dialog, which ->
                        val api = Api.create()

                        val pref : SharedPreferences = context.getSharedPreferences("HealthNote", Context.MODE_PRIVATE)
                        val ID = pref.getLong("ID", 0)

                        api.resetData(ID).enqueue(object: Callback<ExerciseResetResponse>{
                            override fun onResponse(
                                call: Call<ExerciseResetResponse>,
                                response: Response<ExerciseResetResponse>
                            ) {
                                when(response.body()!!.code){
                                    200 -> {
                                        Toast.makeText(context, "데이터가 성공적으로 초기화 되었습니다.", Toast.LENGTH_SHORT).show()
                                    } //성공
                                    400 -> {
                                        Toast.makeText(context, "클라이언트 접근 오류", Toast.LENGTH_SHORT).show()
                                    } //클라이언트 접근 오류
                                    500 -> {
                                        Toast.makeText(context, "서버 오류", Toast.LENGTH_SHORT).show()
                                    } //서버 오류
                                }
                            }

                            override fun onFailure(
                                call: Call<ExerciseResetResponse>,
                                t: Throwable
                            ) {
                                Log.d("SettingDialog", "설정 창 데이터 초기화 오류")
                            }
                        })

                    })
                .setNegativeButton("취소",
                    DialogInterface.OnClickListener { dialog, which ->
                        dialog.dismiss()
                    })

            builder.show()
        }

        //로그아웃 버튼
        val logoutBtn = dialog.findViewById<TextView>(R.id.logout)
        logoutBtn.setOnClickListener{
            val builder = AlertDialog.Builder(context)
                .setTitle("로그아웃")
                .setMessage("로그인 페이지로 돌아갑니다")
                .setPositiveButton("확인",
                    DialogInterface.OnClickListener { dialog, which ->
                        val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
                        if(intent != null){
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            context.startActivity(intent)
                            Runtime.getRuntime().exit(0)
                        }
                    })
                .setNegativeButton("취소",
                    DialogInterface.OnClickListener { dialog, which ->
                        dialog.dismiss()
                    })

            builder.show()
        }

        //회원탈퇴 버튼
        val deleteAccountBtn = dialog.findViewById<TextView>(R.id.deleteAccount)
        deleteAccountBtn.setOnClickListener{
            val builder = AlertDialog.Builder(context)
                .setTitle("회원 탈퇴")
                .setMessage("계정이 삭제됩니다\n진행하시겠습니까?")
                .setPositiveButton("확인",
                    DialogInterface.OnClickListener { dialog, which ->
                        val api = Api.create()

                        val pref : SharedPreferences = context.getSharedPreferences("HealthNote", Context.MODE_PRIVATE)
                        val ID = pref.getLong("ID", 0)


                        api.deleteAccount(ID).enqueue(object: Callback<DeleteAccountResponse>{
                            override fun onResponse(
                                call: Call<DeleteAccountResponse>,
                                response: Response<DeleteAccountResponse>
                            ) {
                                when(response.body()!!.code){
                                    200 ->{
                                        val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
                                        if(intent != null){
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                            context.startActivity(intent)
                                            Runtime.getRuntime().exit(0)
                                        }

                                        Toast.makeText(context , "정상적으로 회원탈퇴 되었습니다.", Toast.LENGTH_LONG).show()
                                    }//성공
                                }
                            }

                            override fun onFailure(
                                call: Call<DeleteAccountResponse>,
                                t: Throwable
                            ) {
                                Log.d("회원탈퇴", "서버 접속 실패")
                            }
                        })

                    })
                .setNegativeButton("취소",
                    DialogInterface.OnClickListener { dialog, which ->
                        dialog.dismiss()
                    })

            builder.show()
        }
    }
}