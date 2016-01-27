package com.thoughtworks.jizhang.realmdemo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import com.jakewharton.rxbinding.view.clicks
import com.jakewharton.rxbinding.view.enabled
import com.jakewharton.rxbinding.widget.RxTextView
import com.jakewharton.rxbinding.widget.dataChanges
import com.jakewharton.rxbinding.widget.textChanges
import com.joanzapata.android.BaseAdapterHelper
import com.joanzapata.android.QuickAdapter
import com.thoughtworks.jizhang.realmdemo.model.User
import com.thoughtworks.jizhang.realmdemo.model.Impl.RealmUser
import io.realm.DynamicRealm
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmMigration
import rx.Observable
import rx.lang.kotlin.observable

class MainActivity : AppCompatActivity() {
    var data = arrayListOf(RealmUser("A", 29),
            RealmUser("B", 34),
            RealmUser("C", 25))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setup()
    }

    private fun setup() {
        setupListView()
        setupSaveButton()
    }

    private fun setupListView() {
        val listView = findViewById(R.id.listView) as? ListView
        listView?.adapter = object : QuickAdapter<RealmUser>(this, R.layout.user_item, this.data) {
            override fun convert(helper: BaseAdapterHelper?, item: RealmUser?) {
                helper?.setText(R.id.username, item?.username)
                        ?.setText(R.id.age, item?.age.toString())
            }
        }
    }

//    private fun setupListView() {
//        val listView = findViewById(R.id.listView) as? ListView
//        val data = listOf(
//                RealmUser("A", 29),
//                RealmUser("B", 34),
//                RealmUser("C", 25)
//        )
//        listView?.adapter = object : QuickAdapter<RealmUser>(this, R.layout.user_item, this.data) {
//            override fun convert(helper: BaseAdapterHelper?, item: RealmUser?) {
//                helper?.setText(R.id.username, item?.username)
//                        ?.setText(R.id.age, item?.age.toString())
//            }
//        }
//    }

//    private fun setupSaveButton() {
//        val button = findViewById(R.id.saveButton) as? Button
//        val listView = findViewById(R.id.listView) as? ListView
//        button?.isEnabled = true
//        button?.clicks()
//                ?.subscribe {
//                    this.data.add(RealmUser("D", 25))
//                    (listView?.adapter as QuickAdapter<*>)?.notifyDataSetChanged()
//
//                }
//    }

    private fun setupSaveButton() {
        fun setupDB() {
            val config = RealmConfiguration.Builder(this)
//                    .name("db.realm")
//                    .migration { dynamicRealm, old, new ->
//                        val schema = dynamicRealm.schema
//                        schema.create("RealmUser")
//                        .addField("username", String::class.java)
//                        .addField("age", Int::class.java)
//                    }
                    .build()
            Realm.deleteRealm(config)
            val realm = Realm.getInstance(this)
            Log.e("Fuck", realm.toString())
            realm.beginTransaction()
            val user = realm.createObject(RealmUser::class.java)
            realm.copyFromRealm(user)
            realm.commitTransaction()
        }

        fun enable() {
            val usernameEditText = findViewById(R.id.usernameEditText) as? EditText
            val ageEditText = findViewById(R.id.ageEditText) as? EditText
            Observable.combineLatest<CharSequence, CharSequence, Boolean>(usernameEditText?.textChanges(), ageEditText?.textChanges(), { username, age ->
                username.length > 0 && age.length > 0
            }).subscribe { result ->
                val saveButton = findViewById(R.id.saveButton) as? Button
                saveButton?.isEnabled = result
            }
        }

        fun onSave() {
            val saveButton = findViewById(R.id.saveButton) as? Button
            saveButton?.clicks()
                    ?.subscribe { it ->
                        val usernameEditText = findViewById(R.id.usernameEditText) as? EditText
                        val ageEditText = findViewById(R.id.ageEditText) as? EditText
                        val username = usernameEditText?.text.toString()
                        val age = ageEditText?.text.toString().toInt()
                    }
        }

        enable()
        onSave()
        setupDB()
    }
}
