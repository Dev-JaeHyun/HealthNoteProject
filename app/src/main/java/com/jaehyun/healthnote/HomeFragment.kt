package com.jaehyun.healthnote

import android.content.Context
import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import android.os.Bundle
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


        val pref : SharedPreferences = layoutInflater.context.getSharedPreferences("HealthNote",Context.MODE_PRIVATE)
        var ID = pref.getLong("ID", 0)

        Log.d("memberID", ID.toString())



        init()



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
                when(response.code()){
                    200 -> {
                        binding.checkDay.setText( response.body()!!.totalWeekTime.toString() + " 분" )

                        val dec = DecimalFormat("#,###")
                        var weight : Int = response.body()!!.totalWeekWeight
                        binding.checkWeight.setText( dec.format(weight) + " kg")

                        var days = response.body()!!.weekExerciseCheck
                        var dayViews : Array<View> = arrayOf(
                            binding.day1, binding.day2, binding.day3,
                            binding.day4, binding.day5, binding.day6, binding.day7
                            )
                        for(i in 1..days.size){
                            if(days[i-1]){
                                dayViews[i-1].foreground = resources.getDrawable(R.drawable.ic_check)
                            }else{ dayViews[i-1].foreground = null }
                        }


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