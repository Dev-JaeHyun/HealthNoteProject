package com.jaehyun.healthnote

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.jaehyun.healthnote.databinding.FragmentCommunityBinding
import com.jaehyun.healthnote.databinding.ItemCommunityBinding
import com.jaehyun.healthnote.dataclass.PostProfile

class CustomAdapter(val postProfile: ArrayList<PostProfile>): RecyclerView.Adapter<CustomAdapter.Holder>() {
    
    //지속적으로 추가해줄 arrayList를 구현하며, 생성자로 받은 postProfile을 postList에 넣어놓음
    private var postList : ArrayList<PostProfile> = postProfile
    
    //아이템 추가
    fun addItem(post: PostProfile){
        postList.add(post)
    }
    
    override fun getItemCount(): Int {
        return postList.size
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomAdapter.Holder {
        val binding = ItemCommunityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    //뷰 홀더의 내용을 담는 함수
    override fun onBindViewHolder(holder: CustomAdapter.Holder, position: Int) {
        holder.titleUsername.text = postList[position].userName
        holder.commentUsername.text = postList[position].userName
        holder.commentTitle.text = postList[position].title
        
        //좋아요
        var likeCnt = postList[position].goodCount
        holder.likeCount.text = "좋아요 " + likeCnt.toString() + "개"
        holder.likeButton.setOnClickListener {

            Log.d("좋아요",holder.likeState.toString())

            if(!holder.likeState){
                //좋아요가 false일 때
                holder.likeButton.setBackgroundResource(R.drawable.ic_favorite)

                //좋아요 더하기---------작성필요---------------------------------------------------------

                likeCnt += 1; holder.likeCount.text = "좋아요 " + likeCnt.toString() + "개"
                holder.likeState = true
            } else{
                //좋아요가 true일 때
                holder.likeButton.setBackgroundResource(R.drawable.ic_favorite_border)

                //좋아요 빼기------작성필요------------------------------------------------------------

                likeCnt -= 1; holder.likeCount.text = "좋아요 " + likeCnt.toString() + "개"
                holder.likeState = false
            }

        }


        //게시글 사진
        if(postList[position].userImage != null)
            holder.titleImage.setImageBitmap(decodePicString(postList[position].userImage))
        holder.postPhoto.setImageBitmap(decodePicString(postList[position].communityPicture))

        //사진 더블클릭 시 좋아요 버튼 호출
        holder.postPhoto.setOnClickListener{
            if(System.currentTimeMillis() > holder.TouchDelay) {
                //한 번 클릭
                holder.TouchDelay = System.currentTimeMillis() + 200
                return@setOnClickListener
            }
            if(System.currentTimeMillis() < holder.TouchDelay){
                //더블 터치
                holder.likeButton.callOnClick()

            }
        }

    }
    inner class Holder(val binding: ItemCommunityBinding) : RecyclerView.ViewHolder(binding.root){
        val titleUsername = binding.titleUsername
        val titleImage = binding.titleImage
        val postPhoto = binding.postPhoto
        val commentUsername = binding.commentUsername
        val commentTitle = binding.commentTitle
        val likeCount = binding.likeCount

        val likeButton = binding.likeButton
        var likeState = false
        var TouchDelay = 0L
    }

    fun decodePicString (encodedString: String): Bitmap {

        val imageBytes = Base64.decode(encodedString, Base64.DEFAULT)
        val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

        return decodedImage
    }
}