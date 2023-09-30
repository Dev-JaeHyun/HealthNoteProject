package com.jaehyun.healthnote

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jaehyun.healthnote.databinding.FragmentCommunityBinding
import com.jaehyun.healthnote.dataclass.PostProfile
import com.jaehyun.healthnote.dataclass.PostProfileResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CommunityFragment : Fragment() {

    private lateinit var binding : FragmentCommunityBinding
    private lateinit var cAdapter : CustomAdapter
    private var postCnt : Int = 0 //불러왔던 게시글 수

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCommunityBinding.inflate(layoutInflater, container, false)

        //더미데이터로 lateinit 임시초기화
        var postList = ArrayList<PostProfile>()
        cAdapter = CustomAdapter(postList)
        binding.rv.adapter = cAdapter

        binding.rv.layoutManager = LinearLayoutManager(context) //레이아웃 LinearLayout 방식으로 배치 (vertical)
        
        //프래그먼트를 불러올 때마다 갱신하기 위해 지역변수로 선언
        var postIndex = 0
        loadPosts(postIndex) //첫 로딩 0~10가져오기

        //스크롤 맨 아래로 내렸을 때 새로고침(로딩)
        binding.rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                
                if (!binding.rv.canScrollVertically(1)) {
                    //최하단일 때
                    postIndex += postCnt; postCnt = 0
                    loadPosts(postIndex)
                }
            }
        })

        
        return binding.root
    }

    //인덱스부터 최대10개까지 불러오기
    fun loadPosts(index: Int) {
        if(binding == null) return //뷰 바인딩이 실패할 시 리턴

        val api = Api.create()
        api.getCommunity(index).enqueue(object: Callback<PostProfileResponse>{
            override fun onResponse(
                call: Call<PostProfileResponse>,
                response: Response<PostProfileResponse>
            ) {
                when(response.body()!!.code){
                    200 -> {
                        postCnt = response.body()!!.communityCount
                        if(postCnt == 0) return //게시글이 존재하지 않으면 실행하지 않음

                        val posts: ArrayList<PostProfile> = response.body()!!.communities

                        for(post in posts){
                            cAdapter.addItem(post)
                        } //게시글을 하나씩 어댑터에 추가함

                        //어댑터를 게시글 수만큼 추가적으로 갱신
                        binding.rv.adapter!!.notifyItemInserted(postCnt)
                    } //성공
                    400 -> {
                        Log.d("communityFragment api", "클라이언트 오류/실패")
                    } //클라이언트 오류/실패
                    500 -> {
                        Log.d("communityFragment api", "서버 오류")
                    } //서버 오류
                }
            }

            override fun onFailure(call: Call<PostProfileResponse>, t: Throwable) {
                Log.d("communityFragment", "Api 불러오기 오류")
            }
        })
    }




}