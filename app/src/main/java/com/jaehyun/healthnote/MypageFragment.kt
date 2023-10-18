package com.jaehyun.healthnote

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jaehyun.healthnote.databinding.FragmentMypageBinding
import com.jaehyun.healthnote.dataclass.EncodingImage
import com.jaehyun.healthnote.dataclass.UserImagesResponse
import com.jaehyun.healthnote.dataclass.UserInfoResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MypageFragment : Fragment() {

    private lateinit var binding : FragmentMypageBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMypageBinding.inflate(layoutInflater, container, false)
        
        //유저 ID가져오기
        val pref : SharedPreferences = layoutInflater.context.getSharedPreferences("HealthNote",
            Context.MODE_PRIVATE)
        var ID = pref.getLong("ID", 0)

        //프로필 지정
        var api = Api.create()
        api.userInfo(ID).enqueue(object: Callback<UserInfoResponse>{
            override fun onResponse(
                call: Call<UserInfoResponse>,
                response: Response<UserInfoResponse>
            ) {
                when(response.body()!!.code){
                    200 ->{
                        binding.userName.text = response.body()!!.userName + "님"
                        binding.userEmail.text = response.body()!!.email
                        binding.userIntroduce.text = response.body()!!.introduction

                        //유저 이미지가 null이 아니라면 작업 진행 (null일 경우 사진 없음)
                        if(response.body()!!.userImage != null) {
                            var userimage: Bitmap? = decodePicString(response.body()!!.userImage)
                            userimage = getBitmapCircleCrop(userimage!!, 0, 0)
                            if (userimage != null)
                                binding.userImage.setImageBitmap(userimage)
                        }
                    } //성공
                    400 ->{
                        Log.d("userInfo", "400")
                    } //회원정보 오류
                }
            }

            override fun onFailure(call: Call<UserInfoResponse>, t: Throwable) {
                Log.d("userInfo", "api 오류")
            }

        })

        //유저 사진 가져오기
        api.userImages(ID).enqueue(object: Callback<UserImagesResponse>{
            override fun onResponse(
                call: Call<UserImagesResponse>,
                response: Response<UserImagesResponse>
            ) {
                when(response.body()!!.code){
                    200 ->{
                        if(response.body()!!.encodingImageCount != 0) {
                            var photoList = response.body()!!.encodingImages
                            var gridAdapter = GridPhotoAdapter(context, photoList)
                            binding.photoGrid.adapter = gridAdapter
                        }
                    }//성공
                    400 ->{
                        Log.d("userImages", "400")
                    }//클라이언트 실패/오류
                    500 ->{
                        Log.d("userImages", "500")
                    }//서버 오류
                }
            }
            override fun onFailure(call: Call<UserImagesResponse>, t: Throwable) {
                Log.d("userImages", "api오류")
            }

        })



        binding.editProfile.setOnClickListener{
            val dialog = CustomDialog(layoutInflater.context)
            dialog.showDialog(binding.editProfile)
        }

        return binding.root
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