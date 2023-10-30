package com.jaehyun.healthnote

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.jaehyun.healthnote.databinding.CardLibraryBinding
import com.jaehyun.healthnote.dataclass.Exercise
import com.jaehyun.healthnote.dataclass.ExerciseInfoResponse
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LibraryAdapter(var lists: List<Exercise>) : RecyclerView.Adapter<LibraryAdapter.Holder>() {

    private var libList : ArrayList<Long> = ArrayList()

    class Holder(val binding: CardLibraryBinding) : RecyclerView.ViewHolder(binding.root){
        var exer_id : Long = binding.exerciseId.text.toString().toLong()
        var exer_card : ConstraintLayout = binding.libCard
        var exer_name : TextView = binding.exerciseName
        var exer_info : Button = binding.exerciseInfo
        var exer_check = false

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = CardLibraryBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val pref : SharedPreferences = holder.binding.root.context.getSharedPreferences("HealthNote",
            Context.MODE_PRIVATE)
        holder.exer_id = lists[position].exerciseNumber.toLong()


        //클릭 리스너
        holder.binding.libCard.setOnClickListener {
            //눌렸을 때 애니메이션 세팅
            holder.exer_check = !(holder.exer_check)
            backgroundSetting(holder)

            //리스트 불러오기 -> 넣거나 빼기 -> 저장
            readArray(pref)
            if(holder.exer_check){
                libList.add(holder.exer_id)
            }else{
                libList.remove(holder.exer_id)
            }
            saveArray(pref)

            //버튼 조작
            var libBtn = holder.binding.root.rootView.findViewById<TextView>(R.id.libBtn)
            if(libList.isEmpty()) {
                libBtn.visibility = View.GONE
            }else {
                libBtn.visibility = View.VISIBLE
            }
            libBtn.text = libList.size.toString() + "개 운동 추가하기"

        }

        //카드 세팅
        holder.binding.exerciseId.text = lists[position].exerciseNumber.toString()
        holder.binding.exerciseName.text = lists[position].exerciseName
        holder.binding.exerciseInfo.setOnClickListener {
            val api = Api.create()
            api.getExerciseInfo(holder.exer_id.toInt()).enqueue(object : Callback<ExerciseInfoResponse> {
                override fun onResponse(
                    call: Call<ExerciseInfoResponse>,
                    response: Response<ExerciseInfoResponse>
                ) {
                    when (response.body()!!.code) {
                        200 -> {
                            var dialog =
                                LibraryDialog(holder.binding.root.context, response.body()!!)
                            dialog.showDialog()
                        }//성공
                        400 -> {
                            Log.d("getExerciseInfo", "400")
                        }//실패 : 클라이언트 오류
                    }
                }

                override fun onFailure(call: Call<ExerciseInfoResponse>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })
        }

        //현재 상태에따른 버튼 조정
        readArray(pref)
        holder.exer_check = false
        if(libList.isNotEmpty()) {
            libList.sort()


            if(libList.binarySearch(holder.exer_id) >= 0) {
                holder.exer_check = true
            }
        }
        backgroundSetting(holder)
    }
    private fun backgroundSetting(holder: Holder){

        holder.exer_card.setBackgroundColor(ContextCompat.getColor(holder.binding.root.context, R.color.white))
        if (holder.exer_check) {
            holder.exer_card.setBackgroundColor(ContextCompat.getColor(holder.binding.root.context, R.color.warm_blue))


        }
    }

    override fun getItemCount(): Int {
        return lists.size;
    }

    private fun saveArray(pref: SharedPreferences){
        var jsonArray = JSONArray()
        for(i in libList){
            jsonArray.put(i)
        }

        with(pref.edit()){
            putString("LibList", jsonArray.toString())
            commit()
        }

        Log.d("saveArray", jsonArray.toString())
    }

    private fun readArray(pref: SharedPreferences){
        var json = pref.getString("LibList", "")
        libList.clear()
        if(!json.equals("")) {
            var arrJson = JSONArray(json)
            for (i in 0 until arrJson.length()) {
                libList.add(arrJson.optLong(i))
            }
        }
    }


}