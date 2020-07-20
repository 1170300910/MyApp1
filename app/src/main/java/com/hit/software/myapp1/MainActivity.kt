package com.hit.software.myapp1

import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity(),HttpResponse,AMapLocationListener {
    var mRealm:Realm?=null
    var mHandler=object: Handler() {
        override fun handleMessage(msg: Message) {
            tips.setText(msg.obj.toString())
        }
    }



    /**
     * 加载布局文件
     */
    override fun onCreate(savedInstanceState: Bundle?) { //打开应用时执行
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        setContentView(R.layout.layout2)
//        R.string.app_name
//        Log.d("hello","This is activity.")

//        val mConfig= RealmConfiguration.Builder()
//            //第一次运行时要生成数据库，
//            // 这个时生成在手机上，
//            // 生成后拷贝到assets文件夹中
////            .name("exam.realm")
////            .directory(File("sdcard/exam"))
//            //自动新建了exam文件夹
//            //若闪退很可能是没有这个文件夹
//
//            //以后直接使用assets文件夹中已有的数据库
//            .assetFile("exam.realm")
//
//            .build()
//        mRealm= Realm.getInstance(mConfig)

//        var mUser=UserInfo(UUID.randomUUID().toString(),"lorna","20172728")
//        //为什么多次运行exam.realm中还是只有abc那个条目
//        mRealm?.beginTransaction() //开启事务
//        mRealm?.copyToRealmOrUpdate(mUser)
//        //一个Realm的实例中可以存储不同RealmObject的子类实例
//        mRealm?.commitTransaction() //提交事务

        /**
         * 给登录按钮设置监听，将信息发送给后台
         * 请求数据格式：{"username":"", "userpassword":""}
         */
        button_login.setOnClickListener{
//            savePreferences("username",)
//            savePreferences("userpassword",input_password.text.toString())

//            startActivity(Intent(this,WebActivity::class.java).putExtra("user_name","abc"))
//            this.finish()
//            // Call this when your activity is done and should be closed.
//            //也就是如果退出上面start的activity，该activity也会退出
//
//            //给指定的app发送广播
//            var mIntent=Intent()
//            mIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//            mIntent.action = "com.tencent.mm"
//            this.sendBroadcast(mIntent)

            val mLoginRequest="{\"user_name\":\""+input_name.text.toString()+"\",\"user_password\":\""+input_password.text.toString()+"\"}"
            Log.d("hello","发送的网络请求是"+mLoginRequest)
            //发送用户登录请求
            Http.post_send(this,"http://add52e5.cpolar.io/mlogin",mLoginRequest)
        }

        /**
         * 读取信息
         */
//        button_show.setOnClickListener{
////            Log.d("hit2",getPreference("username"))
////            Log.d("hit2",getPreference("userpassword"))
//            readData()
//        }

        /**
         * 释放程序中的某个文件
         */
        val fileDirectory= File("/sdcard/myMusic")
        if(!fileDirectory.exists()){
            fileDirectory.mkdir()
        }
        val mFile=File(fileDirectory.path+"/Note.txt")
        if(!mFile.exists()){
            mFile.createNewFile()
            val fos= FileOutputStream(mFile)
            //返回mFile的文件输出流
            val fis=this.assets.open("Note.txt")
            //指定源文件名称
            val buffer=ByteArray(1024)//定义缓冲区
            var length=fis.read(buffer)
            //将fis文件的内容读到buffer中
            while(length>0){
                fos.write(buffer,0,length)
                length=fis.read(buffer)
            }
            fos.flush() //Flushes this stream
            fis.close()
            fos.close()
        }
    }




    /**
     * 读取数据库中的数据
     */
    fun readData(){
        mRealm?.beginTransaction()
        var result=mRealm?.where(UserInfo::class.java)?.findAll()
        //返回mRealm中所有UserInfo类的对象
        result?.forEach{
            Log.d("hello",it.mUserId)
            Log.d("hello",it.mUserName)
            Log.d("hello",it.mUserPassword)
        }
        mRealm?.commitTransaction()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("hello","Activity destroyed.")
    }
    
    /**
     * 块注释
     */

    //行注释

    /**
     * 保存key和value到preferences，mSharedPreferences是局部变量，但相当于有一个全局数据结构名为"com.hit.software.myapp1"
     */
    private fun savePreferences(key:String,value:String){
        //Log.d("hit", input_name.text.toString())
        //保存用户输入的用户名和密码
        val mSharedPreferences:SharedPreferences?=getSharedPreferences("com.hit.software.myapp1",0)
        //var是variable表示mSharedPreference是一个变量，SharedPreferences是一个类似HashMap的数据类型，一般用于保存轻量级的数据
        val mEditor=mSharedPreferences?.edit() //SharedPreferences.edit()返回SharedPreferences对象的编辑器，进入到编辑模式
        //什么情况下需要申请权限
        mEditor?.putString(key,value)
        mEditor?.apply()
    }

    /**
     * 根据key读取Preferences中对应的value
     */
    private fun getPreference(key:String):String{ //若在返回值类型后面加?，表示返回值可以为null
        //即使每次点的时候mSharedPreferences内容发生了变化，userName每次都是一个新声明的变量，只被赋了一次值
        //Toast()
        return getSharedPreferences("com.hit.software.myapp1",0).getString(key,"").toString() //这里为什么需要.toString()
        //若不存在key为username的数据，则返回null
    }

    override fun getResponse(content: String?) {
        Log.d("hello","THIS IS CALLBACK"+content)
        val loginResult=ProcessResponse().userLogin(content!!)
        if(loginResult=="success"){
            //登陆成功
            startActivity(Intent(this,HomeActivity::class.java))
        }
        else{
            val mMessage=Message()
            if(loginResult==null){
                mMessage.what=2
                mMessage.obj="返回消息不是user_login类型"
            }
            else{
                mMessage.what=1
                mMessage.obj=loginResult
            }
            mHandler.sendMessage(mMessage)
        }
    }

    override fun errorMsg() {
        var mMessage=Message()
        mMessage.what=1
        mHandler.sendMessage(mMessage)
    }

    override fun onLocationChanged(amapLocation: AMapLocation?) {
        Log.d("hello", amapLocation?.errorCode.toString())
        if (amapLocation != null) {
            if (amapLocation.errorCode == 0) { //可在其中解析amapLocation获取相应内容。
                Log.d("hello", amapLocation.address.toString())
//                Log.d(amapLocation.locationType)//获取当前定位结果来源，如网络定位结果，详见定位类型表
//                Log.d("hello", amapLocation.latitude.toString())
//                Log.d("hello", amapLocation.longitude.toString())
//                Log.d("hello", amapLocation.accuracy.toString())
//                Log.d("hello", amapLocation.streetNum.toString())
//                Log.d("hello", amapLocation.cityCode.toString())
//                Log.d("hello", amapLocation.adCode.toString())
//                Log.d("hello", amapLocation.aoiName.toString())
//                Log.d("hello", amapLocation.buildingId.toString())
//                Log.d("hello", amapLocation.floor.toString())
//                Log.d("hello", amapLocation.gpsAccuracyStatus.toString())
//                val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
//                val date = Date(amapLocation.time)
//                Log.d("hello", df.format(date))
            }
            else {
                //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                Log.e("AmapError","location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:"
                        + amapLocation.getErrorInfo());
            }
        }
    }

    private fun checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("hello","没有权限")
        }
    }

    private fun locate(){
        //声明AMapLocationClient类对象
        var mLocationClient: AMapLocationClient? = null
        //声明定位回调监听器
        mLocationClient=AMapLocationClient(applicationContext)
        mLocationClient.setLocationListener(this)

        //声明AMapLocationClientOption对象
        var mLocationOption: AMapLocationClientOption? = null
        //初始化AMapLocationClientOption对象
        mLocationOption = AMapLocationClientOption()

        val option = AMapLocationClientOption()
        /**
         * 设置定位场景，目前支持三种场景（签到、出行、运动，默认无场景）
         */
        option.locationPurpose = AMapLocationClientOption.AMapLocationPurpose.SignIn
        if (null != mLocationClient) {
            mLocationClient.setLocationOption(option)
            //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
            mLocationClient.stopLocation()
            mLocationClient.startLocation()
        }

        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);

        //获取一次定位结果：
        //该方法默认为false。
        mLocationOption.setOnceLocation(true);

        //获取最近3s内精度最高的一次定位结果：
        //设置setOnceLocationLatest(boolean b)接口为true，
        // 启动定位时SDK会返回最近3s内精度最高的一次定位结果。
        // 如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，
        // 反之不会，默认为false。
        mLocationOption.setOnceLocationLatest(true);

        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);

        //设置是否允许模拟位置,默认为true，允许模拟位置
        mLocationOption.setMockEnable(true);

        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption.setHttpTimeOut(20000);

        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        Log.d("hello","启动定位")
        mLocationClient.startLocation();
        Log.d("hello","停止定位")
        mLocationClient.stopLocation();
        mLocationClient.onDestroy();//销毁定位客户端，同时销毁本地定位服务。
    }
}