package com.jaehyun.healthnote.dataclass

data class ExerciseWeekInfoResponse(
    val weekExerciseCheck : IntArray,
    val code : Int

    //200: 불러오기 성공
    //400: 클라이언트 접근 오류
    //500: 서버 예외
)