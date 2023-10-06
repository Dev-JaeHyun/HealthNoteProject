package com.jaehyun.healthnote.dataclass

data class LikeResponse(

    val resultCount: Int,
    val code: Int

    //200 성공
    //400 클라이언트 오류/실패
    //500 서버 오류

)
