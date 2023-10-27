package com.jaehyun.healthnote

import android.content.Context
import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.transition.Visibility
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.jaehyun.healthnote.databinding.FragmentHomeBinding
import com.jaehyun.healthnote.databinding.FragmentLibraryBinding
import com.jaehyun.healthnote.dataclass.ExerciseWeekInfoResponse
import com.jaehyun.healthnote.dataclass.UserInfoResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat
import java.time.DayOfWeek
import java.util.Calendar

class HomeFragment : Fragment() {

    private lateinit var binding : FragmentHomeBinding



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        init() //홈 프래그먼트 실행 시 초기화 설정

        binding.settingButton.setOnClickListener{
            SettingDialog(layoutInflater.context).showDialog()
        }

        binding.reload.setOnClickListener{init()}


        return binding.root
    }


    fun init(){


        val pref : SharedPreferences = layoutInflater.context.getSharedPreferences("HealthNote",Context.MODE_PRIVATE)
        var ID = pref.getLong("ID", 0)

        Log.d("ID", ID.toString())

        val api = Api.create()
        //유저이름 받아오기
        api.userInfo(ID).enqueue(object: Callback<UserInfoResponse>{
            override fun onResponse(
                call: Call<UserInfoResponse>,
                response: Response<UserInfoResponse>
            ) {
                when(response.body()!!.code){
                    200 ->{
                        val username = response.body()!!.userName

                        binding.helloUser.text = "안녕하세요 '$username'님"
                    }//성공
                    400 ->{
                        Log.d("userInfo","400")
                    }//회원정보 오류
                }

            }
            override fun onFailure(call: Call<UserInfoResponse>, t: Throwable) {
                Log.d("userInfo", "유저정보 불러오기 실패")
            }

        })

        //주간 운동정보 받아오기
        api.getExerciseWeekInfo(ID).enqueue(object: Callback<ExerciseWeekInfoResponse>{
            override fun onResponse(
                call: Call<ExerciseWeekInfoResponse>,
                response: Response<ExerciseWeekInfoResponse>
            ) {
                when(response.body()!!.code){
                    200 -> {
                        var days = response.body()!!.weekExerciseCheck
                        var dayViews : Array<TextView> = arrayOf(
                            binding.day1, binding.day2, binding.day3,
                            binding.day4, binding.day5, binding.day6, binding.day7
                            )

                        var dayCount = 0; var timeCount = 0
                        for(i in 1..days.size){
                            if(days[i-1] != 0){
                                dayViews[i-1].visibility = View.VISIBLE
                                dayViews[i-1].setText( days[i-1].toString() + "m" )

                                dayCount++
                                timeCount += days[i-1]
                            }else{ dayViews[i-1].visibility = View.INVISIBLE }
                        }

                        binding.checkDay.setText( dayCount.toString() + "/7 일" )
                        binding.checkTime.setText( timeCount.toString() + "/420 분" )
                        binding.timetext.setText( timeCount.toString() + "m")


                        binding.dayProgress.max = 7
                        binding.dayProgress.progress = dayCount

                        binding.timeProgress.max = 420
                        binding.timeProgress.progress = timeCount

                    } //불러오기 성공
                    400 -> {
                        Log.d("init", "400")
                    } //클라이언트 접근 오류
                    500 -> {
                        Log.d("init", "500")
                    } //서버 예외

                }
            }
            override fun onFailure(call: Call<ExerciseWeekInfoResponse>, t: Throwable) {
                Toast.makeText(layoutInflater.context, "초기화 실패", Toast.LENGTH_SHORT).show()
            }

        })

    }

}