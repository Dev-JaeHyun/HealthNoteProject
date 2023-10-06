package com.jaehyun.healthnote

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jaehyun.healthnote.databinding.FragmentLibraryBinding

class LibraryFragment : Fragment(){

    private lateinit var binding : FragmentLibraryBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLibraryBinding.inflate(layoutInflater, container, false)


        val layoutParams = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ) //CardView width, height

        layoutParams.setMargins(16,16,16,50)

        val menuCardView = CardView(layoutInflater.context)
        menuCardView.radius = 12F
        menuCardView.setContentPadding(25,25,25,25)
        menuCardView.setCardBackgroundColor(Color.LTGRAY)
        menuCardView.maxCardElevation = 12F
        menuCardView.setOnClickListener{
            Toast.makeText(layoutInflater.context, "텟스트", Toast.LENGTH_SHORT).show()
        }

        menuCardView.addView(generateCardView())
        menuCardView.addView(generateCardView())
        binding.rootLayout.addView(menuCardView)



        return binding.root
    }



    private fun generateCardView(): LinearLayout {

        val cardLinearLayout = LinearLayout(layoutInflater.context)
        cardLinearLayout.orientation = LinearLayout.VERTICAL

        val restaurantName = TextView(layoutInflater.context)
        restaurantName.text = "파스쿠찌 잠실역점"
        restaurantName.textSize = 24f
        restaurantName.setTextColor(Color.WHITE)

        val restaurantNumber = TextView(layoutInflater.context)
        restaurantNumber.text = "02-000-0000"
        restaurantNumber.textSize = 17f
        restaurantNumber.setTextColor(Color.WHITE)

        val restaurantAddress = TextView(layoutInflater.context)
        restaurantAddress.text = "잠실역 7번출구 300m"
        restaurantAddress.textSize = 17f
        restaurantAddress.setTextColor(Color.WHITE)

        cardLinearLayout.addView(restaurantName)
        cardLinearLayout.addView(restaurantNumber)
        cardLinearLayout.addView(restaurantAddress)

        return cardLinearLayout
    }

}