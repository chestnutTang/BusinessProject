package com.example.business.bean

class BaseResponse<T> {
    var data: T? = null
    var errorMsg = ""
    var errorCode = 0


}