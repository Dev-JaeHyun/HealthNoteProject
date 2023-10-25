package com.jaehyun.healthnote.dataclass

data class EditProfileResponse(
    val code : Int?

    //200 성공
    //400 실패(회원번호 오류)
)
