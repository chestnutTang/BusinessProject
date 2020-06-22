package com.example.business.model

data class FFF(
    val message: String,
    val result: Result,
    val status: String
)

data class Result(
    val headPic: String,
    val nickName: String,
    val phone: String,
    val sessionId: String,
    val sex: Int,
    val userId: Int
)