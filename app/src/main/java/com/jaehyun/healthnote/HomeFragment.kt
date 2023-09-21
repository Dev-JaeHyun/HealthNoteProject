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
import android.widget.Toast
import com.jaehyun.healthnote.databinding.FragmentHomeBinding
import com.jaehyun.healthnote.databinding.FragmentLibraryBinding
import com.jaehyun.healthnote.dataclass.ExerciseWeekInfoResponse
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
        api.getExerciseWeekInfo(ID).enqueue(object: Callback<ExerciseWeekInfoResponse>{
            override fun onResponse(
                call: Call<ExerciseWeekInfoResponse>,
                response: Response<ExerciseWeekInfoResponse>
            ) {
                when(response.body()!!.code){
                    200 -> {

                        val dec = DecimalFormat("#,###")
                        var weight : Int = response.body()!!.totalWeekWeight
                        binding.checkWeight.setText( dec.format(weight) + " 분")

                        var days = response.body()!!.weekExerciseCheck
                        var dayViews : Array<View> = arrayOf(
                            binding.day1, binding.day2, binding.day3,
                            binding.day4, binding.day5, binding.day6, binding.day7
                            )

                        var dayCount = 0
                        for(i in 1..days.size){
                            if(days[i-1]){
                                dayViews[i-1].visibility = View.VISIBLE

                                dayCount++
                            }else{ dayViews[i-1].visibility = View.INVISIBLE }
                        }

                        binding.checkDay.setText( dayCount.toString() + "/7 일" )

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