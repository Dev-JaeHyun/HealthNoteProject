package com.jaehyun.healthnote

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.DialogFragment
import com.jaehyun.healthnote.databinding.FragmentEditProfileDialogBinding
import com.jaehyun.healthnote.dataclass.EditProfile
import com.jaehyun.healthnote.dataclass.EditProfileResponse
import com.jaehyun.healthnote.dataclass.UserInfoResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.IOException


class EditProfileFragmentDialog : DialogFragment() {

    private lateinit var binding: FragmentEditProfileDialogBinding
    lateinit var resultImage : String
    val Gallery = 0

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


        //이미지 선택 dialog 표시
        binding.imageSelect.setOnClickListener {
            val builder = AlertDialog.Builder(context)
                .setTitle("사진 선택")
                .setMessage("원하시는 항목을 선택해주세요.")
                .setPositiveButton("불러오기",
                    DialogInterface.OnClickListener { dialog, which ->
                        //갤러리 열기
                        loadImage()

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
        binding.submitBtn.setOnClickListener{
            var profile = EditProfile(
                ID,
                binding.editUsername.text.toString(),
                binding.editIntroduction.text.toString()
            )

            api.editProfile(profile).enqueue(object: Callback<EditProfileResponse>{

                override fun onResponse(
                    call: Call<EditProfileResponse>,
                    response: Response<EditProfileResponse>
                ) {
                    when(response.body()!!.code){
                        200 ->{

                        }//성공
                        400 ->{
                            Log.d("editProfile", "400")
                        }//실패(회원 코드 오류)
                    }
                }
                override fun onFailure(call: Call<EditProfileResponse>, t: Throwable) {
                    TODO("Not yet implemented")
                }


            })


        }

    }

    //갤러리에서 사진 가져오기
    private fun loadImage(){
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT


        startActivityForResult(Intent.createChooser(intent, "Load Picture"), Gallery)
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

    //이미지 회전 정보 가져오기
    private fun getOrientationOfImage(uri: Uri): Int {
        // uri -> inputStream
        val inputStream = context?.contentResolver!!.openInputStream(uri)
        val exif: ExifInterface? = try {
            ExifInterface(inputStream!!)
        } catch (e: IOException) {
            e.printStackTrace()
            return -1
        }
        inputStream.close()

        // 회전된 각도 알아내기
        val orientation = exif?.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        if (orientation != -1) {
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> return 90
                ExifInterface.ORIENTATION_ROTATE_180 -> return 180
                ExifInterface.ORIENTATION_ROTATE_270 -> return 270
            }
        }
        return 0
    }

    //이미지를 회전시키는 함수
    private fun getRotatedBitmap(bitmap: Bitmap?, degrees: Float): Bitmap? {
        if (bitmap == null) return null
        if (degrees == 0F) return bitmap
        val m = Matrix()
        m.setRotate(degrees, bitmap.width.toFloat() / 2, bitmap.height.toFloat() / 2)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, m, true)
    }

    //이미지를 갤러리에서 가져올 때 비트맵으로 변환해주는 함수
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == Gallery){
            if(resultCode == Activity.RESULT_OK){
                var dataUri = data?.data
                try{
                    var bitmap : Bitmap? = MediaStore.Images.Media.getBitmap(context?.contentResolver, dataUri)
                    val orientation = getOrientationOfImage(dataUri!!).toFloat()
                    bitmap = getRotatedBitmap(bitmap, orientation)!!
                    binding.userImage.setImageBitmap(bitmap)
                    //이미지를 circleCrop 속성으로 가운데 한번 자른 뒤
                    //하단의 코드로 원형으로 재 정제과정
                    var icon  = binding.userImage.getDrawable()
                    bitmap = icon.toBitmap(1080, 1080)
                    binding.userImage.setImageBitmap(getBitmapCircleCrop(bitmap, 1080, 1080))


                    //서버 측에 보낼 bitmap 저장
                    resultImage = BitmapToString(bitmap)!!
                }catch(e:Exception){
                    Toast.makeText(context, "$e", Toast.LENGTH_SHORT).show()
                }
            }else{
                //something error
            }
        }
    }

    //bitmap 을 base64형 String으로 변환
    fun BitmapToString(bitmap: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos)
        val bytes = baos.toByteArray()
        return Base64.encodeToString(bytes, Base64.DEFAULT)
    }


}