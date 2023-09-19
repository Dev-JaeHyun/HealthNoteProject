package com.jaehyun.healthnote.dataclass

data class ChangePwResponse(
    val code : Int

    //200: 성공 / 400: 실패(회원 번호 오류)
)
