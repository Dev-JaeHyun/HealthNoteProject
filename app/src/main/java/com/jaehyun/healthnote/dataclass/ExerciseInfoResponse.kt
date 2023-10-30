package com.jaehyun.healthnote.dataclass

data class ExerciseInfoResponse(
    val code: Int,
    val exerciseNumber: Int,
    val exerciseName: String,
    val exerciseExplanation: String,
    val exerciseUrl: String


    //200	성공
    //400	실패(클라이언트 오류)
)
