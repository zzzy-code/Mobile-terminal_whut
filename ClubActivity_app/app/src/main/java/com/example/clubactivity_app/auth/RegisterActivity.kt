package com.example.clubactivity_app.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.clubactivity_app.database.DatabaseHelper
import com.example.clubactivity_app.databinding.ActivityRegisterBinding
import com.example.clubactivity_app.activity.ActivityListActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dbHelper = DatabaseHelper(this)

        setupUI()
    }

    private fun setupUI() {
        binding.btnRegister.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val confirmPassword = binding.etConfirmPassword.text.toString().trim()

            if (validateInput(username, password, confirmPassword)) {
                performRegistration(username, password)
            }
        }

        binding.tvLoginLink.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun validateInput(username: String, password: String, confirmPassword: String): Boolean {
        return when {
            username.isEmpty() -> {
                showToast("用户名不能为空")
                false
            }
            password.isEmpty() -> {
                showToast("密码不能为空")
                false
            }
            password != confirmPassword -> {
                showToast("两次输入的密码不一致")
                false
            }
            else -> true
        }
    }

    private fun performRegistration(username: String, password: String) {
        if (dbHelper.getUser(username) != null) {
            showToast("用户名已存在")
            return
        }

        val result = dbHelper.addUser(username, password)
        if (result != -1L) {
            showToast("注册成功")
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        } else {
            showToast("注册失败，请重试")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}