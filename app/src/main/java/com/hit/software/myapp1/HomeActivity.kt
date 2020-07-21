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
            when(msg.what){
                0->{
                    val data=JSONObject(msg.obj.toString())
                    que_A.setText("A. "+data.optString("queA"))
                    que_B.setText("B. "+data.optString("queB"))
                    que_C.setText("C. "+data.optString("queC"))
                    que_D.setText("D. "+data.optString("queD"))
                }
                else->input_subject.setText(msg.obj?.toString())
            }
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
            Http.post_send(this,"http://192.168.162.1:8080/que/request",mExamRequest)
        }

        /**
         * 给保存按钮设置监听，将信息发送给后台
         * 请求数据格式：{"queTitle":"","answer":"A/B/C/D"}
         */
        button_save.setOnClickListener{
            val mAnswer="{\"queTitle\":\""+input_subject.text.toString()+"\",\"answer\":\""+input_answer.text.toString()+"\"}"
            Log.d("hello","保存答案请求是："+mAnswer)
            //发送保存请求
            Http.post_send(this,"http://192.168.162.1:8080/que/save",mAnswer)
        }
    }

    override fun getResponse(content: String?) {
        Log.d("hello","THIS IS HomeActivities' CALLBACK"+content)
        val requestResult=ProcessResponse().examRequest(content!!)
        val mMessage= Message()
        if(requestResult==null){
            mMessage.what=2
            mMessage.obj="返回消息不是exam类型"
        }
        else{
            if(requestResult!=""){ //存在该科目试题
//            startActivity(Intent(this,HomeActivity::class.java))
                Log.d("hello","试题为："+requestResult)
                //不可以直接在协程的回调方法中setText，原因是什么我也不知道
                mMessage.what=0
                mMessage.obj=requestResult //肯定不是空字符串
            }
            else{ //不存在该科目试题
                mMessage.what=1
                mMessage.obj="输入科目不存在！"
            }
        }
        mHandler.sendMessage(mMessage)
    }

    override fun errorMsg() {
        var mMessage=Message()
        mMessage.what=1
        mHandler.sendMessage(mMessage)
    }
}