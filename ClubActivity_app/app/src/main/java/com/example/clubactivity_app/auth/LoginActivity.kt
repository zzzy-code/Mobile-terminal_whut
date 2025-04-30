package com.example.clubactivity_app.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.clubactivity_app.database.DatabaseHelper
import com.example.clubactivity_app.databinding.ActivityLoginBinding
import com.example.clubactivity_app.activity.ActivityListActivity
import com.example.clubactivity_app.util.PreferenceHelper

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var prefs: PreferenceHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dbHelper = DatabaseHelper(this)
        prefs = PreferenceHelper(this)

        checkAutoLogin()
        setupUI()
    }

    private fun checkAutoLogin() {
        if (prefs.isLoggedIn()) {
            startActivity(Intent(this, ActivityListActivity::class.java))
            finish()
        }
    }

    private fun setupUI() {
        binding.btnLogin.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (validateInput(username, password)) {
                performLogin(username, password)
            }
        }

        binding.tvRegisterLink.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }
    }

    private fun validateInput(username: String, password: String): Boolean {
        return when {
            username.isEmpty() -> {
                showToast("用户名不能为空")
                false
            }
            password.isEmpty() -> {
                showToast("密码不能为空")
                false
            }
            else -> true
        }
    }

    private fun performLogin(username: String, password: String) {
        val user = dbHelper.getUser(username)
        if (user != null && dbHelper.validatePassword(user, password)) {
            prefs.saveLoginState(true)
            prefs.saveCurrentUser(user.userId)
            startActivity(Intent(this, ActivityListActivity::class.java))
            finish()
        } else {
            showToast("用户名或密码错误")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}