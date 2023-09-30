package com.jaehyun.healthnote.dataclass

data class PostProfileResponse(
    val communityCount : Int,
    val code : Int,
    val communities : ArrayList<PostProfile>

    // 200: 성공
    // 400: 클라이언트 성공/오류
    // 500: 서버 오류
)
