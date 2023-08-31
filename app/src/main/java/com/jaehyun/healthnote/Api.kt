package com.jaehyun.healthnote

import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jaehyun.healthnote.dataclass.Register
import com.jaehyun.healthnote.dataclass.RegisterResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface Api {
    //@Headers("app/json")
    @POST("/test/member/join")
    fun userRegister(
        @Body jsonParams : Register,
    ): Call<RegisterResponse>

    companion object {
        private const val BASE_URL = "http://localhost:8080"
        val gson : Gson = GsonBuilder().setLenient().create()

        fun create() : Api{

            Log.d("Api", "Create 함수 실행 성공2")

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                //.client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(Api::class.java)
        }

    }

}