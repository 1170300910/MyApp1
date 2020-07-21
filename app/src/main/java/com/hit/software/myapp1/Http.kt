package com.hit.software.myapp1

import android.util.Log
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * 网络访问
 */
object Http { //object表示静态类
    //定义OKHTTP客户端
    val mOkHttpClient=OkHttpClient()

    /**
     * 发送请求post发送
     *
     * 参数：url，数据，发送数据的格式
     */
    fun post_send(
        res:HttpResponse?,
        url:String,
        postData:String="",
        mContentType:String="application/x-form-urlencoded"
    ){
        //协程
        GlobalScope.launch{
            //回调方法
            val cb:Callback=object :Callback{
                override fun onFailure(call: Call, e: IOException) {
                    Log.d("hello","This is onFailure! 网络请求失败！")
                    res?.errorMsg()
                }

                override fun onResponse(call: Call, response: Response) {
                    Log.d("hello","返回成功")
                    //Log.d("hello",response.body?.string())
                    //Returns the response as a string.
                    val r=response.body?.string()
                    res?.getResponse(r)
                }
            }

            mOkHttpClient.newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(5,TimeUnit.SECONDS)
            mOkHttpClient.newCall(
                Request.Builder() //An HTTP request. Builder是Request的inner class
                    .url(url)
                    .post(postData.toRequestBody(("application/json;charset=utf-8").toMediaType()))
                        //需要将String类型转化为RequestBody
                    .header("Content-Type",mContentType)
                    .build()
            ).enqueue(cb) //enqueue的时候一定要指定回调方法
//                    .post(
//                        RequestBody.create,
//                        postData)
        }
    }
}