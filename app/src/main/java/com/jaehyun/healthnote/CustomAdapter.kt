package com.jaehyun.healthnote

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jaehyun.healthnote.databinding.ItemCommunityBinding
import com.jaehyun.healthnote.dataclass.LikeResponse
import com.jaehyun.healthnote.dataclass.PostProfile
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CustomAdapter(val postProfile: ArrayList<PostProfile>, var userId: Long): RecyclerView.Adapter<CustomAdapter.Holder>() {
    
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
        holder.communityId = postList[position].communityId
        holder.titleUsername.text = postList[position].userName
        holder.commentUsername.text = postList[position].userName
        holder.commentTitle.text = postList[position].title
        holder.likeState = postList[position].likeState
        
        //좋아요
        var likeCnt = postList[position].goodCount
        holder.likeCount.text = "좋아요 " + likeCnt.toString() + "개"

        //좋아요가 이미 True라면 이미지 변경
        if(holder.likeState)
            holder.likeButton.setBackgroundResource(R.drawable.ic_favorite)

        holder.likeButton.setOnClickListener {
            if(!holder.likeState) {
                //좋아요가 false일 때

                var api = Api.create()
                api.communityLike(holder.communityId, userId).enqueue(object : Callback<LikeResponse> {
                        override fun onResponse(
                            call: Call<LikeResponse>,
                            response: Response<LikeResponse>
                        ) {
                            when (response.body()!!.code) {
                                200 -> {
                                    holder.likeCount.text = "좋아요 " + response.body()!!.resultCount.toString() + "개"
                                    holder.likeButton.setBackgroundResource(R.drawable.ic_favorite)
                                    holder.likeState = true

                                    Log.d("좋아요","성공")
                                } //성공
                                400 -> {
                                    Log.d("communityLike", "400")
                                } //클라이언트 실패/오류
                                500 -> {
                                    Log.d("communityLike", "500")
                                } //서버 오류
                            }
                        }

                        override fun onFailure(call: Call<LikeResponse>, t: Throwable) {
                            Log.d("communityLike", "좋아요 실패")
                        }
                    })
            }
        }


        //게시글 사진
        if(postList[position].userImage != null){
            var circleCropImage : Bitmap? = decodePicString(postList[position].userImage)
            circleCropImage = getBitmapCircleCrop(circleCropImage!!, 0, 0)
            if(circleCropImage != null)
               holder.titleImage.setImageBitmap(circleCropImage)
        }
        if(postList[position].communityPicture != null)
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
        var likeCount = binding.likeCount

        val likeButton = binding.likeButton
        var likeState = false
        var TouchDelay = 0L

        var communityId = 0L
    }

    fun decodePicString (encodedString: String): Bitmap {

        Log.d("decodedPicString", encodedString)

        val imageBytes = Base64.decode(encodedString, Base64.DEFAULT)
        val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

        return decodedImage
    }
    fun getBitmapCircleCrop(bitmap: Bitmap, Width: Int, Height: Int): Bitmap? {
        val output = Bitmap.createBitmap(
            bitmap.width,
            bitmap.height, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(output)
        val color = -0xbdbdbe
        val paint = Paint()
        val rect = Rect(0, 0, bitmap.width, bitmap.height)
        paint.setAntiAlias(true)
        canvas.drawARGB(0, 0, 0, 0)
        paint.setColor(color)
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(
            bitmap.width / 2f, bitmap.height / 2f,
            bitmap.width / 2f, paint
        )
        paint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.SRC_IN))
        canvas.drawBitmap(bitmap, rect, rect, paint)
        var CroppedBitmap = output
        //width, Height에 0,0을 넣으면 원본 사이즈 그대로 출력
        if (Width != 0 && Height != 0) CroppedBitmap =
            Bitmap.createScaledBitmap(output, Width, Height, false)
        return CroppedBitmap
    }

}