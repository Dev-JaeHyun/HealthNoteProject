package com.jaehyun.healthnote.dataclass

data class RegisterResponse(
    val code : Int
    //200: 성공, 300: 아이디 중복, 400: 이메일 중복, 500: 아이디&이메일 중복
)
