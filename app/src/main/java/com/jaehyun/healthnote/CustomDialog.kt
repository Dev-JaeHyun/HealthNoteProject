package com.jaehyun.healthnote

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.jaehyun.healthnote.dataclass.ChangePw
import com.jaehyun.healthnote.dataclass.ChangePwResponse
import com.jaehyun.healthnote.dataclass.FindId
import com.jaehyun.healthnote.dataclass.FindIdResponse
import com.jaehyun.healthnote.dataclass.FindPw
import com.jaehyun.healthnote.dataclass.FindPwResponse
import com.jaehyun.healthnote.dataclass.Register
import com.jaehyun.healthnote.dataclass.RegisterResponse
import com.jaehyun.healthnote.dataclass.UserInfoResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern

class CustomDialog(context: Context) {

    private val context = context
    private val dialog = Dialog(context)
    private var ID : Long? = null

    //유효성 검사
    //성공하면 True, 실패하면 False
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

    //받은 View에 따라 맞는 다이얼로그 띄우기
    fun showDialog(v: View?) {
        dialog.setContentView(
            when (v?.id) {
                R.id.findIdBtn -> R.layout.dialog_findid
                R.id.findPwBtn -> R.layout.dialog_findpw
                R.id.registerBtn -> R.layout.dialog_register
                R.id.changePw -> R.layout.dialog_changepw
                R.id.editProfile -> R.layout.dialog_editprofile
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

        if(v?.id == R.id.editProfile){
            Toast.makeText(context,"초기화 완료",Toast.LENGTH_SHORT).show()
            EditProfileInit(v)
        }


        val submitBtn = dialog.findViewById<Button>(R.id.submitBtn) //전송 버튼
        submitBtn.setOnClickListener {
            val api = Api.create()

            when (v?.id) {
                //아이디 찾기 다이얼로그
                R.id.findIdBtn -> {
                    if (!Validation("EMAIL")) {
                        return@setOnClickListener
                    }

                    var userInfo = FindId(
                        email = dialog.findViewById<EditText>(R.id.inputemail).text.toString()
                    )

                    api.userFindId(userInfo).enqueue(object : Callback<FindIdResponse> {
                        override fun onResponse(
                            call: Call<FindIdResponse>,
                            response: Response<FindIdResponse>
                        ) {
                            when (response.body()!!.code) {
                                200 -> {
                                    dialog.dismiss()
                                    val printText = "아이디 출력 : " + response.body()?.userId
                                    Toast.makeText(context, printText, Toast.LENGTH_SHORT).show()
                                } //성공
                                400 -> {
                                    Toast.makeText(context, "해당 이메일로 가입된 계정이 없습니다", Toast.LENGTH_SHORT).show()
                                } //실패 (이메일 오류)
                            }
                        }

                        override fun onFailure(call: Call<FindIdResponse>, t: Throwable) {
                            Log.d("아이디찾기", "Submit 실패")
                        }
                    })
                }

                R.id.findPwBtn -> {
                    var resultValidation = Validation("ID")
                    resultValidation = !(Validation("EMAIL") && resultValidation)
                    if (resultValidation) {
                        return@setOnClickListener
                    }

                    val userInfo = FindPw (
                        userId = dialog.findViewById<EditText>(R.id.inputid).text.toString(),
                        email = dialog.findViewById<EditText>(R.id.inputemail).text.toString()
                    )

                    api.userFindPw(userInfo).enqueue(object : Callback<FindPwResponse> {
                        override fun onResponse(
                            call: Call<FindPwResponse>,
                            response: Response<FindPwResponse>
                        ) {
                            when (response.body()!!.code) {
                                200 -> {
                                    dialog.dismiss()

                                    val pref : SharedPreferences =
                                        context.getSharedPreferences("HealthNote", Context.MODE_PRIVATE)
                                    val ID : Long = response.body()!!.id
                                    pref.edit().putLong("ID", ID).apply()
                                    
                                    val changePw = dialog.findViewById<TextView>(R.id.changePw)
                                    CustomDialog(context).showDialog(changePw) //다이얼로그 변경

                                    Toast.makeText(context, "비밀번호 인증", Toast.LENGTH_SHORT).show()
                                } //성공
                                400 -> {
                                    Toast.makeText(context, "아이디 혹은 이메일 불일치", Toast.LENGTH_SHORT).show()
                                } //실패 (인증 실패)
                            }
                        }

                        override fun onFailure(call: Call<FindPwResponse>, t: Throwable) {
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

                    val pref : SharedPreferences =
                        context.getSharedPreferences("HealthNote", Context.MODE_PRIVATE)

                    val changeInfo = ChangePw(
                        id = pref.getLong("ID", 0),
                        userPass = dialog.findViewById<EditText>(R.id.inputpw).text.toString()
                    )

                    api.userChangePw(changeInfo).enqueue(object : Callback<ChangePwResponse> {
                        override fun onResponse(
                            call: Call<ChangePwResponse>,
                            response: Response<ChangePwResponse>
                        ) {


                            when (response.body()!!.code) {
                                200 -> {
                                    dialog.dismiss()
                                    
                                    Toast.makeText(context, "비밀번호 변경 완료", Toast.LENGTH_SHORT).show()
                                } //성공
                                
                                400 -> {
                                    Toast.makeText(context, "비밀번호 변경 실패", Toast.LENGTH_SHORT).show()
                                } //회원번호 오류
                            }
                        }

                        override fun onFailure(call: Call<ChangePwResponse>, t: Throwable) {
                            Log.d("changePw", "Submit 실패")
                        }
                    })
                }

                R.id.registerBtn -> {
                    var resultValidation : Boolean = Validation("ID")
                    resultValidation = Validation("PW") && resultValidation
                    resultValidation = Validation("NAME") && resultValidation
                    resultValidation = Validation("EMAIL") && resultValidation
                    if (!resultValidation) {
                        return@setOnClickListener
                    }

                    var register = Register(
                        userId = dialog.findViewById<EditText>(R.id.inputid).text.toString(),
                        userPass = dialog.findViewById<EditText>(R.id.inputpw).text.toString(),
                        userName = dialog.findViewById<EditText>(R.id.inputname).text.toString(),
                        email = dialog.findViewById<EditText>(R.id.inputemail).text.toString()
                    )


                    api.userRegister(register).enqueue(object : Callback<RegisterResponse> {
                        override fun onResponse(
                            call: Call<RegisterResponse>,
                            response: Response<RegisterResponse>
                        ) {
                            when (response.body()!!.code) {
                                200 -> {
                                    dialog.dismiss()
                                    Toast.makeText(context, "회원가입 성공", Toast.LENGTH_SHORT).show()
                                } //로그인 성공

                                300 -> {
                                    dialog.findViewById<TextView>(R.id.warnid).setText("아이디가 존재합니다")
                                    Toast.makeText(context, "회원가입 실패", Toast.LENGTH_SHORT).show()
                                } //아이디 중복

                                400 -> {
                                    dialog.findViewById<TextView>(R.id.warnemail).setText("사용중인 이메일입니다")
                                    Toast.makeText(context, "회원가입 실패", Toast.LENGTH_SHORT).show()
                                } //이메일 중복

                                500 -> {
                                    dialog.findViewById<TextView>(R.id.warnid).setText("아이디가 존재합니다")
                                    dialog.findViewById<TextView>(R.id.warnemail).setText("사용중인 이메일입니다")
                                    Toast.makeText(context, "회원가입 실패", Toast.LENGTH_SHORT).show()
                                } //아이디, 이메일 중복
                            }
                        }

                        override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                            Log.d("Register", "Submit 실패")
                        }
                    })


                }
                R.id.editProfile -> {
                    Toast.makeText(context, "테스트 성공", Toast.LENGTH_SHORT).show()
                }

                else -> return@setOnClickListener
            }
        }


    }

    fun EditProfileInit(v: View?){
        //유저 ID가져오기
        val pref : SharedPreferences = context.getSharedPreferences("HealthNote",
            Context.MODE_PRIVATE)
        var ID = pref.getLong("ID", 0)

        //처음 열었을 때, 초기 정보 표시하기
        val api = Api.create()
        api.userInfo(ID).enqueue(object: Callback<UserInfoResponse>{
            override fun onResponse(
                call: Call<UserInfoResponse>,
                response: Response<UserInfoResponse>
            ) {
                when(response.body()!!.code){
                    200 -> {
                        var image = response.body()!!.userImage
                        if(image != null){
                            var cropImage : Bitmap? = decodePicString(image)
                            cropImage = getBitmapCircleCrop(cropImage!!, 70, 70)
                            dialog.findViewById<ImageView>(R.id.userImage).setImageBitmap(cropImage)
                        }

                        var userName = response.body()!!.userName
                        dialog.findViewById<EditText>(R.id.editUsername).setText(userName)

                        var userIntroduction = response.body()!!.introduction
                        dialog.findViewById<EditText>(R.id.editIntroduction).setText(userIntroduction)

                    }//성공
                    400 -> {
                        Log.d("userInfo", "400")
                    }//회원번호 오류
                }

            }

            override fun onFailure(call: Call<UserInfoResponse>, t: Throwable) {
                Log.d("userInfo", "api오류")
            }
        })

        //사진 선택 버튼에 클릭 리스너 넣기
        var selectedImageView = dialog.findViewById<ImageView>(R.id.userImage)

        dialog.findViewById<Button>(R.id.imageSelect).setOnClickListener {
            val builder = AlertDialog.Builder(context)
                .setTitle("사진 선택")
                .setMessage("원하시는 항목을 선택해주세요.")
                .setPositiveButton("불러오기",
                    DialogInterface.OnClickListener { dialog, which ->
                        dialog.dismiss()
                    })
                .setNegativeButton("초기화",
                    DialogInterface.OnClickListener { dialog, which ->
                        //사진 초기화하기
                        selectedImageView.setImageBitmap(null)


                        dialog.dismiss()
                })

            builder.show()
        }

        //완료 버튼 클릭 시 데이터 전송 리스너 넣기

    }
    
    //사진 비트맵 변환
    fun decodePicString (encodedString: String): Bitmap {

        Log.d("decodedPicString", encodedString)

        val imageBytes = Base64.decode(encodedString, Base64.DEFAULT)
        val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

        return decodedImage
    }

    //비트맵 원형으로 자르기
    fun getBitmapCircleCrop(bitmap: Bitmap, Width: Int, Height: Int): Bitmap? {
        val output = Bitmap.createBitmap(
            bitmap.width,
            bitmap.height, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(output)
        val color = -0xbdbdbe
        val paint = Paint()
        val rect = Rect(0, 0, bitmap.width, bitmap.height)
        paint.setAntiAlias(true)
        canvas.drawARGB(0, 0, 0, 0)
        paint.setColor(color)
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(
            bitmap.width / 2f, bitmap.height / 2f,
            bitmap.width / 2f, paint
        )
        paint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.SRC_IN))
        canvas.drawBitmap(bitmap, rect, rect, paint)
        var CroppedBitmap = output
        //width, Height에 0,0을 넣으면 원본 사이즈 그대로 출력
        if (Width != 0 && Height != 0) CroppedBitmap =
            Bitmap.createScaledBitmap(output, Width, Height, false)
        return CroppedBitmap
    }

}