package com.jaehyun.healthnote.dataclass

data class RegisterResponse(
    val id : Long,
    val code : Int,
    //200: 성공, 400: 클라이언트 실패, 500: 서버 실패
    val success : Boolean
)
