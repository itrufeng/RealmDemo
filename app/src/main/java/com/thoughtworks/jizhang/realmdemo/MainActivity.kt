package com.thoughtworks.jizhang.realmdemo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.thoughtworks.jizhang.realmdemo.model.Impl.RealmUser
import io.realm.Realm
import io.realm.RealmConfiguration

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupDB()
    }

    private fun setupDB() {
        val config = RealmConfiguration.Builder(this)
                .name("db.realm")
                .migration { dynamicRealm, old, new ->
                    val schema = dynamicRealm.schema
                    schema.create("RealmUser")
                            .addField("username", String::class.java)
                            .addField("age", Int::class.java)
                }
                .build()
        Realm.deleteRealm(config)
        val realm = Realm.getInstance(config)
        realm.beginTransaction()

        val user = realm.createObject(RealmUser::class.java)
        user.username = "Steve"
        realm.copyFromRealm(user)
        realm.commitTransaction()
    }
}
