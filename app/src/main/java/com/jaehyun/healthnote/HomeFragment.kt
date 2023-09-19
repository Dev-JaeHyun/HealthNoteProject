package com.jaehyun.healthnote

import android.content.Context
import android.content.SharedPreferences
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

class HomeFragment : Fragment() {

    private lateinit var binding : FragmentHomeBinding

    val pref : SharedPreferences =
        layoutInflater.context.getSharedPreferences("HealthNote",Context.MODE_PRIVATE)
    val ID = pref.getLong("ID", 0)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        init()




        return binding.root
    }


    fun init(){
        val api = Api.create()

        api.getExerciseWeekInfo(ID).enqueue(object: Callback<ExerciseWeekInfoResponse>{
            override fun onResponse(
                call: Call<ExerciseWeekInfoResponse>,
                response: Response<ExerciseWeekInfoResponse>
            ) {
                when(response.code()){
                    200 -> {
                        binding.checkDay.setText( response.body()!!.totalWeekTime.toString() + "/7 일" )
                        binding.checkTime
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