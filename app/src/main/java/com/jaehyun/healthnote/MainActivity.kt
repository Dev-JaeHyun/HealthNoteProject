package com.jaehyun.healthnote

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.jaehyun.healthnote.dataclass.LogIn
import com.jaehyun.healthnote.dataclass.LogInResponse
import com.jaehyun.healthnote.dataclass.Register
import com.jaehyun.healthnote.dataclass.RegisterResponse
import com.jaehyun.healthnote.dataclass.TestResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Dictionary

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    //다이얼로그 표시하기
    fun showDialog(v : View?){
        //예외상황 함수 강제종료
        val dialog = CustomDialog(this)
        dialog.showDialog(v)
    }



    //로그인 함수
    fun Login(v : View?){
        val api = Api.create()

        var userInfo = LogIn(
            userId = findViewById<EditText>(R.id.inputid).text.toString(),
            userPass = findViewById<EditText>(R.id.inputpw).text.toString()
        )


        api.userLogin(userInfo).enqueue(object : Callback<LogInResponse> {
            override fun onResponse(
                call: Call<LogInResponse>,
                response: Response<LogInResponse>
            ) {
                Log.d("로그인 통신 성공",response.toString())
                Log.d("로그인 통신 성공",response.body().toString())

                when(response.code()){
                    200 -> {
                        val pref : SharedPreferences = getPreferences(Context.MODE_PRIVATE)
                        pref.edit().putLong("ID", response.body()!!.id).apply()  //로그인 성공 시 고유 넘버(ID) 저장
                        
                        val intent = Intent(this@MainActivity, HomeActivity::class.java)
                        startActivity(intent) //액티비티 실행
                    } //200: 성공
                    400 -> {
                        Toast.makeText(this@MainActivity, "아이디 또는 비밀번호가 맞지 않습니다.", Toast.LENGTH_LONG).show()
                    } //400: 실패 (인증 실패)
                }
            }

            override fun onFailure(call: Call<LogInResponse>, t: Throwable) {
                Log.d("테스트 실패",t.message.toString())
                Log.d("테스트 실패", "fail")
            }
        })

    }



}