package com.jaehyun.healthnote.dataclass

data class ExerciseResetResponse(
    val success : Boolean,
    val code : Int,
    val message : String

    //200: 성공
    //400: 클라이언트 접근 오류
    //500: 서버 예외
)
