package com.hit.software.myapp1

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import java.util.*

/**
 * Realm数据库类，该类实例与普通类实例不同，会保存到硬盘上
 */
@RealmClass
open class UserInfo ( //open表示其他类可以随意访问该类
    @PrimaryKey
    open var mUserId:String= UUID.randomUUID().toString(),
    open var mUserName:String?=null,
    open var mUserPassword:String?=null
): RealmObject() //继承RealmObject类
//UserInfo后面是小括号是因为里面是参数而不是class的实现