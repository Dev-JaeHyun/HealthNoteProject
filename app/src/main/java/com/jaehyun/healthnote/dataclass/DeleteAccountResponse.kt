package com.jaehyun.healthnote.dataclass

data class DeleteAccountResponse(
    val success : Boolean,
    val code : Int,
    val message : String
    
    //200: 성공
)
