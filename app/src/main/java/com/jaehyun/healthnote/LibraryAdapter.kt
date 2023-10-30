package com.jaehyun.healthnote

import android.content.res.Resources
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.colorResource
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.jaehyun.healthnote.databinding.CardLibraryBinding
import com.jaehyun.healthnote.dataclass.Exercise
import com.jaehyun.healthnote.dataclass.ExerciseInfoResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LibraryAdapter(var lists: List<Exercise>) : RecyclerView.Adapter<LibraryAdapter.Holder>() {

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
        val exerID = lists[position].exerciseNumber
        holder.exer_check = false

        holder.binding.libCard.setOnClickListener {
            holder.exer_check = !(holder.exer_check)
            backgroundSetting(holder)
            Toast.makeText(holder.binding.root.context, exerID.toString() + "번 운동 " + holder.exer_check, Toast.LENGTH_SHORT).show()
        }
        backgroundSetting(holder)

        holder.binding.exerciseId.text = exerID.toString()
        holder.binding.exerciseName.text = lists[position].exerciseName
        holder.binding.exerciseInfo.setOnClickListener {
            val api = Api.create()
            api.getExerciseInfo(exerID).enqueue(object : Callback<ExerciseInfoResponse> {
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

}