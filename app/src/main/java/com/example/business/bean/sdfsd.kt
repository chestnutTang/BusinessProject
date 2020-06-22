package com.example.business.bean

class sdfsd {
    /**
     * result : {"headPic":"http://mobile.bwstudent.com/images/small/head_pic/2019-02-26/20190226233015.jpeg","nickName":"Jr_09b24","phone":"18612991023","sessionId":"1592794968897251","sex":1,"userId":251}
     * message : 登录成功
     * status : 0000
     */
    var result: ResultBean? = null
    var message: String? = null
    var status: String? = null

    class ResultBean {
        /**
         * headPic : http://mobile.bwstudent.com/images/small/head_pic/2019-02-26/20190226233015.jpeg
         * nickName : Jr_09b24
         * phone : 18612991023
         * sessionId : 1592794968897251
         * sex : 1
         * userId : 251
         */
        var headPic: String? = null
        var nickName: String? = null
        var phone: String? = null
        var sessionId: String? = null
        var sex = 0
        var userId = 0

    }
}