package com.hit.software.myapp1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import kotlinx.android.synthetic.main.activity_home.*
import org.json.JSONObject

class HomeActivity : AppCompatActivity(),HttpResponse {

    var mHandler=object: Handler() {
        override fun handleMessage(msg: Message) {
            input_subject.setText(msg.obj?.toString())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        /**
         * 给答题按钮设置监听，将信息发送给后台
         * 请求数据格式：{"subject":""}
         */
        button_request.setOnClickListener{
            val mExamRequest="{\"subject\":\""+input_subject.text.toString()+"\"}"
            Log.d("hello","发送的题目请求是："+mExamRequest)
            //发送答题请求
//            Http.post_send(this,"http://add52e5.cpolar.io/mlogin",mExamRequest)
            Http.post_send(this,"http://add52e5.cpolar.io/que/request",mExamRequest)
        }
    }

    override fun getResponse(content: String?) {
        Log.d("hello","THIS IS HomeActivities' CALLBACK"+content)
        val requestResult=ProcessResponse().examRequest(content!!)
        val mMessage= Message()
        if(requestResult==null){
            mMessage.what=2
            mMessage.obj="返回消息不是exam类型"
            mHandler.sendMessage(mMessage)
        }
        else{
            if(requestResult!=""){ //存在该科目试题
//            startActivity(Intent(this,HomeActivity::class.java))
                Log.d("hello","试题为："+requestResult)
                //展示选项
                val data= JSONObject(requestResult)
                Log.d("hello",data.optString("queA"))
                Log.d("hello",data.optString("queB"))
                Log.d("hello",data.optString("queC"))
                Log.d("hello",data.optString("queD"))
//                que_A.setText("A. "+data.optString("queA")+" B. "+data.optString("queB")+" C. ${data.optString("queC")}"+" D. ${data.optString("queD")}")
                /**
                 * 给保存按钮设置监听，将信息发送给后台
                 * 请求数据格式：{"queTitle":"","answer":"A/B/C/D"}
                 */
                button_save.setOnClickListener{
                    val mAnswer="{\"queTitle\":\""+data.optString("queTitle")+"\",\"answer\":\""+input_subject.text.toString()+"\"}"
                    Log.d("hello","保存答案请求是："+mAnswer)
                    //发送保存请求
                    Http.post_send(this,"http://add52e5.cpolar.io/que/save",mAnswer)
                }
            }
            else{ //不存在该科目试题
                mMessage.what=1
                mMessage.obj="输入科目不存在！"
            }
            mHandler.sendMessage(mMessage)
        }
    }

    override fun errorMsg() {
        var mMessage=Message()
        mMessage.what=1
        mHandler.sendMessage(mMessage)
    }
}