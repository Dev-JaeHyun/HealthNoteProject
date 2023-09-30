package com.jaehyun.healthnote.dataclass

data class PostProfile(

    val communityId : Long,
    val communityPicture : String,
    val title : String,
    val goodCount : Int,
    val userImage : String,
    val userName : String
)
