package com.jaehyun.healthnote

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.navigation.NavigationBarView
import com.jaehyun.healthnote.databinding.FragmentLibraryBinding
import com.jaehyun.healthnote.dataclass.Exercise
import com.jaehyun.healthnote.dataclass.ExerciseListResponse
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LibraryFragment(var vp: ViewPager2) : Fragment(){

    private lateinit var binding : FragmentLibraryBinding
    private lateinit var viewAdapter : RecyclerView.Adapter<*>
    private lateinit var viewManager : RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLibraryBinding.inflate(layoutInflater, container, false)

        viewManager = LinearLayoutManager(context, VERTICAL, true)

        //첫 화면 (가슴)의 운동을 띄우기
        loadExerList(100)

        binding.libraryNav.setOnItemSelectedListener(NavigationBarView.OnItemSelectedListener { item ->
            var requestCode : Int?
            when(item.itemId){
                R.id.lower ->{ requestCode = 100 }
                R.id.chest ->{ requestCode = 200 }
                R.id.back ->{ requestCode = 300 }
                R.id.sholder -> { requestCode = 400 }
                R.id.arm -> { requestCode = 500 }
                else -> { requestCode = null }
            }
            if(requestCode == null) false

            //운동 불러오기
            loadExerList(requestCode!!)

            true
        })

        //운동 리스트 초기화
        val pref : SharedPreferences = inflater.context.getSharedPreferences("HealthNote",
            Context.MODE_PRIVATE)
        var jsonArray = JSONArray()

        with(pref.edit()){
            putString("LibList", jsonArray.toString())
            commit()
        }

        //운동 하러가기 버튼 활성화
        binding.libBtn.setOnClickListener{
            vp.currentItem = 1
        }


        return binding.root
    }

    fun applyRecylerView(){
        binding.rvLibrary.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }

    fun loadExerList(code: Int){
        val api = Api.create()
        api.getExerciseList(code).enqueue(object: Callback<ExerciseListResponse>{
            override fun onResponse(
                call: Call<ExerciseListResponse>,
                response: Response<ExerciseListResponse>
            ) {
                val exercises = response.body()!!.libraryExerciseListDtos
                viewAdapter = LibraryAdapter(exercises)
                applyRecylerView() //리사이클러 뷰 적용
            }
            override fun onFailure(call: Call<ExerciseListResponse>, t: Throwable) {
                Log.d("getExerciseList", "Failed")
            }
        })
    }

}