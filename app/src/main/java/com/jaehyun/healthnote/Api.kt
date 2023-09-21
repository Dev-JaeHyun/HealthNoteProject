package com.jaehyun.healthnote

import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jaehyun.healthnote.dataclass.ChangePw
import com.jaehyun.healthnote.dataclass.ChangePwResponse
import com.jaehyun.healthnote.dataclass.ExerciseResetResponse
import com.jaehyun.healthnote.dataclass.ExerciseWeekInfoResponse
import com.jaehyun.healthnote.dataclass.FindId
import com.jaehyun.healthnote.dataclass.FindIdResponse
import com.jaehyun.healthnote.dataclass.FindPw
import com.jaehyun.healthnote.dataclass.FindPwResponse
import com.jaehyun.healthnote.dataclass.LogIn
import com.jaehyun.healthnote.dataclass.LogInResponse
import com.jaehyun.healthnote.dataclass.Register
import com.jaehyun.healthnote.dataclass.RegisterResponse
import com.jaehyun.healthnote.dataclass.TestResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface Api {
    //@Headers("app/json")

    @GET("/test")
    fun test(): Call<TestResponse>
    @GET("/exercise/week")
    fun getExerciseWeekInfo( @Query("memberId") memberId: Long ): Call<ExerciseWeekInfoResponse>

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