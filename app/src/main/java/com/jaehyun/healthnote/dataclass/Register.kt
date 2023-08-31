package com.jaehyun.healthnote.dataclass

data class Register(
    var userName : String ?= null,
    var userId : String ?= null,
    var userPass : String ?= null,
    var userEmail : String ?= null
)
