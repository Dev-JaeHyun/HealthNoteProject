package com.jaehyun.healthnote

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jaehyun.healthnote.dataclass.Register
import com.jaehyun.healthnote.dataclass.RegisterResponse
import com.jaehyun.healthnote.ui.theme.HealthNoteTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : ComponentActivity() {


    fun popUpActivity(v : View?){
        //예외상황 함수 강제종료
        val dialog = CustomDialog(this)
        dialog.showDialog(v)
    }
    //다이얼로그 표시하기

    fun TestCall(v : View?){

        val api = Api.create()
        val data = Register("조재현", "killerjoe", "123456","test@naver.com")

        api.userRegister(data).enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                Log.d("로그인 통신 성공",response.toString())
                Log.d("로그인 통신 성공",response.body().toString())

                when(response.code()){
                    200 -> Toast.makeText(this@MainActivity, "테스트 성공 200", Toast.LENGTH_LONG).show()
                    400 -> Toast.makeText(this@MainActivity, "테스트 실패 400", Toast.LENGTH_LONG).show()
                    500 -> Toast.makeText(this@MainActivity, "테스트 실패 500", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    Log.d("테스트 실패",t.message.toString())
                Log.d("테스트 실패", "fail")
            }
        })

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

}