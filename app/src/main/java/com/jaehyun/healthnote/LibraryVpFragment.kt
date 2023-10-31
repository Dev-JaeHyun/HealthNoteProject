package com.jaehyun.healthnote

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.viewpager.widget.ViewPager
import com.example.viewpager2.ViewPagerAdapter
import com.jaehyun.healthnote.databinding.FragmentLibraryVpBinding


class LibraryVpFragment(fragmentActivity: FragmentActivity) : Fragment() {

    private lateinit var ViewPagerAdapter : ViewPagerAdapter
    private lateinit var binding : FragmentLibraryVpBinding
    private var activity = fragmentActivity
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLibraryVpBinding.inflate(layoutInflater)


        //ViewPager에 띄워줄 Fragment들 초기화
        val fragmentList = listOf(LibraryFragment(), ExerciseFragment())

        //ViewPager 초기화
        ViewPagerAdapter = ViewPagerAdapter(activity)
        ViewPagerAdapter.fragments.addAll(fragmentList)


        //ViewPager2와 Adapter 연동
        binding.vp.adapter = ViewPagerAdapter
        binding.vp.isUserInputEnabled = false


        return binding.root
    }

}