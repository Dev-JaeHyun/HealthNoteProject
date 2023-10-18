package com.jaehyun.healthnote.dataclass

data class UserImagesResponse(
    val encodingImageCount : Int,
    val code : Int,
    val encodingImages : ArrayList<EncodingImage>

    //200: 성공
    //400: 클라이언트 성공/실패
    //500: 서버오류
)
