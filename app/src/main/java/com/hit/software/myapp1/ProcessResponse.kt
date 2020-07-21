package com.hit.software.myapp1

import android.util.Log
import org.json.JSONObject

class ProcessResponse {

    /**
     * 处理用户登录数据
     * 数据格式：{"type":"user_login", "data":"true/false"}
     */
    fun userLogin(res:String):String?{
        //解析JSON
        return try{
            val mLoginData=JSONObject(res)
            when(mLoginData.optString("type","")){
                "user_login"-> {
                    if(mLoginData.optString("data")=="true")
                        //登陆成功
                        "success"
                    else
                        mLoginData.optString("error")
                }
                else -> null
            }
        } catch(e:Exception){
            Log.d("hello","Exception caught in userLogin function.")
            null
        }
    }

    /**
     * 处理返回的考试数据
     * 数据格式：{"type":"exam","exist":"true/false","data":{"queTitle":"","queA":"","queB":"","queC":"","queD":""}}
     */
    fun examRequest(res:String):String?{
        //解析JSON
        return try{
            val mLoginData=JSONObject(res)
            when(mLoginData.optString("type","")){
                "exam"-> {
                    if(mLoginData.optString("exist")=="true") //存在该科目试题
                        mLoginData.optString("data")
                    else //不存在该科目试题
                        ""
                }
                else -> null
            }
        } catch(e:Exception){
            Log.d("hello","Exception caught in examRequest function.")
            null
        }
    }
}