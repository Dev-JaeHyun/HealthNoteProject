package com.jaehyun.healthnote

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import com.jaehyun.healthnote.databinding.FragmentEditProfileDialogBinding
import com.jaehyun.healthnote.dataclass.UserInfoResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditProfileFragmentDialog : DialogFragment() {

    private lateinit var binding: FragmentEditProfileDialogBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditProfileDialogBinding.inflate(inflater, container, false)

        dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) //배경 투명화

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //뒤로가기 버튼
        binding.dismissBtn.setOnClickListener{
            dismiss()
        }

        //초기화
        EditProfileInit()

        //완료 버튼
        binding.submitBtn.setOnClickListener {
            val api = Api.create()
            //전송버튼 api 작성
        }

    }

    fun EditProfileInit(){
        //유저 ID가져오기
        val pref : SharedPreferences = layoutInflater.context.getSharedPreferences("HealthNote",
            Context.MODE_PRIVATE)
        var ID = pref.getLong("ID", 0)

        //처음 열었을 때, 초기 정보 표시하기
        val api = Api.create()
        api.userInfo(ID).enqueue(object: Callback<UserInfoResponse> {
            override fun onResponse(
                call: Call<UserInfoResponse>,
                response: Response<UserInfoResponse>
            ) {
                when(response.body()!!.code){
                    200 -> {
                        var image = response.body()!!.userImage
                        if(image != null){
                            var cropImage : Bitmap? = decodePicString(image)
                            cropImage = getBitmapCircleCrop(cropImage!!, 70, 70)
                            binding.userImage.setImageBitmap(cropImage)
                        }

                        var userName = response.body()!!.userName
                        binding.editUsername.setText(userName)

                        var userIntroduction = response.body()!!.introduction
                        binding.editIntroduction.setText(userIntroduction)

                    }//성공
                    400 -> {
                        Log.d("userInfo", "400")
                    }//회원번호 오류
                }

            }

            override fun onFailure(call: Call<UserInfoResponse>, t: Throwable) {
                Log.d("userInfo", "api오류")
            }
        })


        binding.imageSelect.setOnClickListener {
            val builder = AlertDialog.Builder(context)
                .setTitle("사진 선택")
                .setMessage("원하시는 항목을 선택해주세요.")
                .setPositiveButton("불러오기",
                    DialogInterface.OnClickListener { dialog, which ->
                        //갤러리 열기
                        dialog.dismiss()
                    })
                .setNegativeButton("초기화",
                    DialogInterface.OnClickListener { dialog, which ->
                        //사진 초기화하기
                        binding.userImage.setImageBitmap(null)
                        dialog.dismiss()
                    })

            builder.show()
        }

        //완료 버튼 클릭 시 데이터 전송 리스너 넣기

    }

    //사진 비트맵 변환
    fun decodePicString (encodedString: String): Bitmap {

        Log.d("decodedPicString", encodedString)

        val imageBytes = Base64.decode(encodedString, Base64.DEFAULT)
        val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

        return decodedImage
    }

    //비트맵 원형으로 자르기
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

    //이미지를 클릭할 시에 data에 uri를 bitmap으로 바꿔주는 함수


}