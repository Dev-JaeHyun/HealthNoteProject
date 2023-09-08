package com.jaehyun.healthnote

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
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

    fun showDialog(v : View?){
        //예외상황 함수 강제종료
        val dialog = CustomDialog(this)
        dialog.showDialog(v)
    }
    //다이얼로그 표시하기


    fun Login(v : View?){
        val api = Api.create()

        api.test().enqueue(object : Callback<TestResponse> {
            override fun onResponse(
                call: Call<TestResponse>,
                response: Response<TestResponse>
            ) {
                Log.d("로그인 통신 성공",response.toString())
                Log.d("로그인 통신 성공",response.body().toString())

                when(response.code()){
                    200 -> {
                        val dummyId = "admin"
                        val dummyPw = "root1234!"
                        val dummyUserID = 20010227


                        val inputId = findViewById<EditText>(R.id.inputid).text.toString()
                        val inputPw = findViewById<EditText>(R.id.inputpw).text.toString()

                        if(dummyId.equals(inputId)){ //입력된 ID가 존재하는가
                            if(dummyPw.equals(inputPw)){
                                val intent = Intent(this@MainActivity, HomeActivity::class.java)
                                intent.putExtra("ID", dummyUserID)
                                startActivity(intent)
                            } else{
                                Toast.makeText(this@MainActivity, "비밀번호가 맞지 않습니다.", Toast.LENGTH_LONG).show()
                            }
                        } else{
                            Toast.makeText(this@MainActivity, "존재하지 않는 아이디 입니다.", Toast.LENGTH_LONG).show()
                        }
                    } //200: 성공
                }
            }

            override fun onFailure(call: Call<TestResponse>, t: Throwable) {
                Log.d("테스트 실패",t.message.toString())
                Log.d("테스트 실패", "fail")
            }
        })

    }
    //테스트용 임시 함수 (삭제 예정)



}