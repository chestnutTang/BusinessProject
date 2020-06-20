package com.example.business.net

class HttpCodeException(code: Int, s: String) : Exception(s) {

    var code: Int = 0
        internal set

    init {
        this.code = code
    }
}
