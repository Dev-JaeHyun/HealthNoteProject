package com.jaehyun.healthnote

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.jaehyun.healthnote.databinding.FragmentLibraryBinding

class LibraryFragment : Fragment(){

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
        viewAdapter = LibraryAdapter()

        //리사이클러 뷰 적용
        binding.rvLibrary.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }


        


        return binding.root
    }



}