package com.jaehyun.healthnote

import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jaehyun.healthnote.dataclass.ChangePw
import com.jaehyun.healthnote.dataclass.ChangePwResponse
import com.jaehyun.healthnote.dataclass.DeleteAccountResponse
import com.jaehyun.healthnote.dataclass.EditImageResponse
import com.jaehyun.healthnote.dataclass.EditProfile
import com.jaehyun.healthnote.dataclass.EditProfileResponse
import com.jaehyun.healthnote.dataclass.ExerciseInfoResponse
import com.jaehyun.healthnote.dataclass.ExerciseListResponse
import com.jaehyun.healthnote.dataclass.ExerciseResetResponse
import com.jaehyun.healthnote.dataclass.ExerciseWeekInfoResponse
import com.jaehyun.healthnote.dataclass.FindId
import com.jaehyun.healthnote.dataclass.FindIdResponse
import com.jaehyun.healthnote.dataclass.FindPw
import com.jaehyun.healthnote.dataclass.FindPwResponse
import com.jaehyun.healthnote.dataclass.LikeResponse
import com.jaehyun.healthnote.dataclass.LogIn
import com.jaehyun.healthnote.dataclass.LogInResponse
import com.jaehyun.healthnote.dataclass.PostProfileResponse
import com.jaehyun.healthnote.dataclass.Register
import com.jaehyun.healthnote.dataclass.RegisterResponse
import com.jaehyun.healthnote.dataclass.TestResponse
import com.jaehyun.healthnote.dataclass.UserImagesResponse
import com.jaehyun.healthnote.dataclass.UserInfoResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface Api {
    //@Headers("app/json")

    @GET("/test")
    fun test(): Call<TestResponse>
    
    //홈 화면
    @GET("/exercise/week")
    fun getExerciseWeekInfo( @Query("memberId") memberId: Long ): Call<ExerciseWeekInfoResponse>
    
    
    //게시글(커뮤니티 관련)
    @GET("/community")
    fun communityLike( @Query("communityId") communityId: Long, @Query("memberId") memberId: Long): Call<LikeResponse>
    @GET("/community/all")
    fun getCommunity( @Query("front") front: Int, @Query("memberId") memberId: Long): Call<PostProfileResponse>

    
    //유저 관련
    @GET("/api/myPage/main")
    fun userInfo( @Query("id") id: Long): Call<UserInfoResponse>
    @POST("/api/member/edit-profile")
    fun editProfile( @Body jsonParams: EditProfile): Call<EditProfileResponse>
    @Multipart
    @POST("/api/myPage/userImage")
    fun editImage(
        @Part("mpJson") mpJson : RequestBody,
        @Part userImage : MultipartBody.Part?
    ): Call<EditImageResponse>
    @GET("/community/user/image")
    fun userImages( @Query("memberId") memberId: Long): Call<UserImagesResponse>
    @POST("/api/member/sign-in")
    fun userLogin( @Body jsonParams: LogIn ): Call<LogInResponse>
    @POST("/api/member/find-userId")
    fun userFindId( @Body jsonParams: FindId ): Call<FindIdResponse>
    @POST("/api/member/find-userPass")
    fun userFindPw( @Body jsonParams: FindPw ): Call<FindPwResponse>
    @POST("/api/member/update-userPass")
    fun userChangePw( @Body jsonParams: ChangePw ): Call<ChangePwResponse>
    @POST("/api/member/sign-up")
    fun userRegister( @Body jsonParams : Register ): Call<RegisterResponse>

    @DELETE("/exercise")
    fun resetData( @Query("memberId") memberId: Long ): Call<ExerciseResetResponse>

    @DELETE("/api/member/withdrawal")
    fun deleteAccount( @Query("memberId") memberId: Long): Call<DeleteAccountResponse>

    //라이브러리 관련
    @GET("api/library/exercise-info")
    fun getExerciseInfo( @Query("exerciseNumber") exerciseNumber: Int): Call<ExerciseInfoResponse>
    @GET("api/library/exercise-list")
    fun getExerciseList( @Query("exerciseNumber") exerciseNumber: Int): Call<ExerciseListResponse>
    //100(하체), 200(가슴), 300(등), 400(어깨), 500(팔)



    companion object {
        private const val BASE_URL = "http://healthnote.cloud"
        val gson : Gson = GsonBuilder().setLenient().create()

        fun create() : Api{

            Log.d("Api", "Api Create 함수 실행 성공")

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                //.client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(Api::class.java)
        }

    }

}