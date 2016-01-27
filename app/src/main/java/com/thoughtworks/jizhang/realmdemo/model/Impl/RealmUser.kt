package com.thoughtworks.jizhang.realmdemo.model.Impl

import com.thoughtworks.jizhang.realmdemo.model.User
import io.realm.RealmObject
import io.realm.annotations.RealmClass

/**
 * Created by jizhang on 1/21/16.
 */
public open class RealmUser constructor(username: String, age: Int): RealmObject() {
    public open var username: String? = null
    public open var age: Int? = null

    init {
        this.username = username
        this.age = age
    }
}