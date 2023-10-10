package com.jaehyun.healthnote

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jaehyun.healthnote.databinding.CardLibraryBinding

class LibraryAdapter() : RecyclerView.Adapter<LibraryAdapter.Holder>() {


    class Holder(val binding: CardLibraryBinding) : RecyclerView.ViewHolder(binding.root){
        var exer_name : TextView = binding.exerciseName
        var exer_info : Button = binding.exerciseInfo
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = CardLibraryBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 10;
    }
}