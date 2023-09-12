package com.jaehyun.healthnote

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.jaehyun.healthnote.databinding.ActivityHomeBinding

private const val TAG_HOME = "home_fragment"
private const val TAG_LIBRARY = "library_fragment"
private const val TAG_COMMUNITY = "community_fragment"
private const val TAG_MYPAGE = "mypage_fragment"

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //맨 처음에 보여줄 프래그먼트 설정
        setFragment(TAG_HOME, HomeFragment  ())

        //Navi 항목 클릭 시 프래그먼트 변경하는 함수 호출
        binding.navigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment -> setFragment(TAG_HOME, HomeFragment())
                R.id.libraryFragment -> setFragment(TAG_LIBRARY, LibraryFragment())
                R.id.communityFragment -> setFragment(TAG_COMMUNITY, CommunityFragment())
                R.id.mypageFragment -> setFragment(TAG_MYPAGE, MypageFragment())
            }
            true
        }
    }


    //프래그먼트 컨트롤 함수
    fun setFragment(tag: String, fragment: Fragment){
        val manager: FragmentManager = supportFragmentManager
        val ft: FragmentTransaction = manager.beginTransaction()

        //트랜잭션에 tag로 전달된 fragment가 없을 경우 add
        if(manager.findFragmentByTag(tag) == null){
            ft.add(R.id.homeFrameLayout, fragment, tag)
        }

        //작업이 수월하도록 manager에 add되어있는 fragment들을 변수로 할당해둠
        val home = manager.findFragmentByTag(TAG_HOME)
        val library = manager.findFragmentByTag(TAG_LIBRARY)
        val community = manager.findFragmentByTag(TAG_COMMUNITY)
        val mypage = manager.findFragmentByTag(TAG_MYPAGE)

        //모든 프래그먼트 hide
        if(home != null) ft.hide(home)
        if(library != null) ft.hide(library)
        if(community != null) ft.hide(community)
        if(mypage != null) ft.hide(mypage)

        //선택한 항목에 따라 그에 맞는 프래그먼트만 show
        when(tag) {
            TAG_HOME -> if(home != null) ft.show(home)
            TAG_LIBRARY -> if(library != null) ft.show(library)
            TAG_COMMUNITY -> if(community != null) ft.show(community)
            TAG_MYPAGE -> if(mypage != null) ft.show(mypage)
        }

        //마무리
        ft.commitAllowingStateLoss()
        //ft.commit()
    }




}