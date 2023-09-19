package com.jaehyun.healthnote.dataclass

data class FindIdResponse(
    val userId : String,
    val code : Int

    //200: 성공 / 400: 실패 (이메일 오류)
)
