package com.jaehyun.healthnote.dataclass

data class FindPwResponse(
    val id : Long,
    val code : Int

    //200: 성공 / 400: 실패 (회원 아이디 오류)
)
