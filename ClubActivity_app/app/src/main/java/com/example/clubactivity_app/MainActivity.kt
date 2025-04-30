package com.example.clubactivity_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.clubactivity_app.activity.ActivityListActivity
import com.example.clubactivity_app.auth.LoginActivity
import com.example.clubactivity_app.util.PreferenceHelper

class MainActivity : AppCompatActivity() {

    private lateinit var prefs: PreferenceHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        prefs = PreferenceHelper(this)
        checkLoginStatus()
    }

    private fun checkLoginStatus() {
        // 检查用户是否已登录
        if (prefs.isLoggedIn()) {
            // 已登录，跳转到活动列表
            startActivity(Intent(this, ActivityListActivity::class.java))
        } else {
            // 未登录，跳转到登录界面
            startActivity(Intent(this, LoginActivity::class.java))
        }
        finish() // 关闭当前Activity
    }
}