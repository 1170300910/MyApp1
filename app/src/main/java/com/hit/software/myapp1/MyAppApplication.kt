package com.hit.software.myapp1

import android.app.Application
import android.util.Log
import io.realm.Realm

class MyAppApplication: Application() {
    /**
     * 基础工作和初始化
     */
    override fun onCreate() {
        super.onCreate()
        Log.d("hello","This is application class, the first class started.")
        Realm.init(this)//初始化
    }

    override fun onTerminate() {
        super.onTerminate()
        Log.d("hello","Application Terminated.")
    }
}