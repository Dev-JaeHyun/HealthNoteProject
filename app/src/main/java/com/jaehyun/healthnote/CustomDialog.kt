package com.jaehyun.healthnote

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.jaehyun.healthnote.dataclass.TestResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern

class CustomDialog(context: Context) {

    private val context = context
    private val dialog = Dialog(context)

    fun Validation(type: String): Boolean {
        val idPattern = "^[a-z]+[a-z0-9]{3,15}$" //시작은 영문 4~16글자
        val pwPattern =
            "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[\$@\$!%*#?&.])[A-Za-z[0-9]\$@\$!%*#?&.]{8,16}\$"
        val namePattern = "^([a-zA-Z가-힣0-9][a-zA-Z가-힣0-9]*){4,16}\$"
        val emailPattern =
            "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"

        when (type) {
            "ID" -> {
                val inputId = dialog.findViewById<EditText>(R.id.inputid)
                val warnId = dialog.findViewById<TextView>(R.id.warnid)

                warnId.setText(" ")
                if (!Pattern.matches(idPattern, inputId.text)) {
                    warnId.setText("아이디 형식이 맞지 않습니다")
                    return false
                }
            }

            "PW" -> {
                val inputPw = dialog.findViewById<EditText>(R.id.inputpw)
                val warnPw = dialog.findViewById<TextView>(R.id.warnpw)

                warnPw.setText(" ")
                if (!Pattern.matches(pwPattern, inputPw.text)) {
                    warnPw.setText("비밀번호 형식이 맞지 않습니다")
                    return false
                }
            }

            "CPW" -> {
                val inputPw = dialog.findViewById<EditText>(R.id.inputpw).text.toString()
                val confirmPw = dialog.findViewById<EditText>(R.id.confirmPw).text.toString()
                val warnCpw = dialog.findViewById<TextView>(R.id.warnCpw)

                warnCpw.setText(" ")
                if (!(inputPw.equals(confirmPw))) {
                    warnCpw.setText("비밀번호가 일치하지 않습니다")
                    return false
                }
            }

            "NAME" -> {
                val inputName = dialog.findViewById<EditText>(R.id.inputname)
                val warnName = dialog.findViewById<TextView>(R.id.warnname)

                warnName.setText(" ")
                if (!Pattern.matches(namePattern, inputName.text)) {
                    warnName.setText("알맞은 이름이 아닙니다")
                    return false
                }
            }

            "EMAIL" -> {
                val inputEmail = dialog.findViewById<EditText>(R.id.inputemail)
                val warnEmail = dialog.findViewById<TextView>(R.id.warnemail)

                warnEmail.setText(" ")
                if (!Pattern.matches(emailPattern, inputEmail.text)) {
                    warnEmail.setText("이메일 형식이 맞지 않습니다")
                    return false
                }
            }
        }

        return true
    }
    //유효성 검사: 통과 시 True 반환 / 조건 미충족 시 False 반환


    fun showDialog(v: View?) {
        dialog.setContentView(
            when (v?.id) {
                R.id.findIdBtn -> R.layout.dialog_findid
                R.id.findPwBtn -> R.layout.dialog_findpw
                R.id.registerBtn -> R.layout.dialog_register
                R.id.changePw -> R.layout.dialog_changepw
                else -> return
            }
        ) //받은 view에 따라 레이아웃 지정
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) //배경 투명화
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog.setCanceledOnTouchOutside(false) //바깥쪽 배경을 눌러도 캔슬되게 하는가
        dialog.setCancelable(false) //뒤로가기 버튼을 눌러서 캔슬되게 하는가
        dialog.show()

        val dismissBtn = dialog.findViewById<ImageButton>(R.id.dismissBtn) //뒤로가기 버튼
        dismissBtn.setOnClickListener {
            dialog.dismiss()
        }

        val submitBtn = dialog.findViewById<Button>(R.id.submitBtn) //전송 버튼
        submitBtn.setOnClickListener {
            val api = Api.create()

            when (v?.id) {
                R.id.findIdBtn -> {
                    if (!Validation("EMAIL")) {
                        return@setOnClickListener
                    }

                    api.test().enqueue(object : Callback<TestResponse> {
                        override fun onResponse(
                            call: Call<TestResponse>,
                            response: Response<TestResponse>
                        ) {
                            when (response.code()) {
                                200 -> {
                                    dialog.dismiss()
                                    Toast.makeText(context, "아이디 출력: ", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }

                        override fun onFailure(call: Call<TestResponse>, t: Throwable) {
                            Log.d("findidBtn", "Submit 실패")
                        }
                    })
                }

                R.id.findPwBtn -> {
                    var resultValidation = Validation("ID")
                    resultValidation = !(Validation("EMAIL") && resultValidation)
                    if (resultValidation) {
                        return@setOnClickListener
                    }

                    api.test().enqueue(object : Callback<TestResponse> {
                        override fun onResponse(
                            call: Call<TestResponse>,
                            response: Response<TestResponse>
                        ) {
                            when (response.code()) {
                                200 -> {
                                    dialog.dismiss()
                                    val changePw = dialog.findViewById<TextView>(R.id.changePw)
                                    CustomDialog(context).showDialog(changePw)
                                    //다이얼로그 변경
                                    Toast.makeText(context, "비밀번호 인증", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }

                        override fun onFailure(call: Call<TestResponse>, t: Throwable) {
                            Log.d("findpwBtn", "Submit 실패")
                        }
                    })
                }

                R.id.changePw -> {
                    var resultValidation = Validation("PW")
                    resultValidation = !(Validation("CPW") && resultValidation)
                    if (resultValidation) {
                        return@setOnClickListener
                    }

                    api.test().enqueue(object : Callback<TestResponse> {
                        override fun onResponse(
                            call: Call<TestResponse>,
                            response: Response<TestResponse>
                        ) {
                            when (response.code()) {
                                200 -> {
                                    dialog.dismiss()
                                    Toast.makeText(context, "비밀번호 변경", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }

                        override fun onFailure(call: Call<TestResponse>, t: Throwable) {
                            Log.d("changePw", "Submit 실패")
                        }
                    })
                }

                R.id.registerBtn -> {
                    var resultValidation = Validation("ID")
                    resultValidation = !(Validation("PW") && resultValidation)
                    resultValidation = !(Validation("NAME") && resultValidation)
                    resultValidation = !(Validation("EMAIL") && resultValidation)
                    if (resultValidation) {
                        return@setOnClickListener
                    }

                    api.test().enqueue(object : Callback<TestResponse> {
                        override fun onResponse(
                            call: Call<TestResponse>,
                            response: Response<TestResponse>
                        ) {
                            when (response.code()) {
                                200 -> {
                                    dialog.dismiss()
                                    Toast.makeText(context, "회원가입 성공", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }

                        override fun onFailure(call: Call<TestResponse>, t: Throwable) {
                            Log.d("Register", "Submit 실패")
                        }
                    })


                }

                else -> return@setOnClickListener
            }
        }


    }
}