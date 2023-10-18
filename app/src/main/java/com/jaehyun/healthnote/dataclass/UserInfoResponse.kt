package com.jaehyun.healthnote.dataclass

data class UserInfoResponse(
    val code : Int,
    val userName : String,
    val introduction : String,
    val email : String,
    val userImage : String

    //200: 성공
    //400: 회원번호 오류
)
