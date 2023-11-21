package com.jaehyun.healthnote

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.jaehyun.healthnote.databinding.FragmentExerciseBinding

class ExerciseFragment(var vp: ViewPager2) : Fragment() {
    private lateinit var binding: FragmentExerciseBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentExerciseBinding.inflate(layoutInflater, container, false)


        binding.finishBtn.setOnClickListener {
            //게시글버튼
            UploadDialog(layoutInflater.context).showDialog()

        }




        return binding.root
    }
}