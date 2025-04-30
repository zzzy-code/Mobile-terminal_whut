package com.example.clubactivity_app.activity

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.clubactivity_app.R
import com.example.clubactivity_app.database.DatabaseHelper
import com.example.clubactivity_app.database.models.ClubActivity
import com.example.clubactivity_app.databinding.ActivityDetailBinding
import com.example.clubactivity_app.util.PreferenceHelper

class ActivityDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var prefs: PreferenceHelper
    private lateinit var currentActivity: ClubActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)
        binding.lifecycleOwner = this

        dbHelper = DatabaseHelper(this)
        prefs = PreferenceHelper(this)

        loadActivityData()
        setupUI()
    }

    private fun loadActivityData() {
        val activityId = intent.getIntExtra("activity_id", -1)
        dbHelper.getActivityById(activityId)?.let {
            currentActivity = it.copy(currentUserId = prefs.getCurrentUser())
            binding.activity = currentActivity
            binding.tvEnrollCount.text = "已报名人数： ${dbHelper.getEnrollmentsCount(it.activityId)}人"
        } ?: run {
            showToast("活动不存在")
            finish()
        }
    }

    private fun setupUI() {
        binding.btnEnroll.setOnClickListener { enrollActivity() }

        binding.activity?.isCreator?.get()?.let { isCreator ->
            if (isCreator) {
                binding.btnEdit.visibility = View.VISIBLE
                binding.btnDelete.visibility = View.VISIBLE

                binding.btnEdit.setOnClickListener {
                    CreateActivity.startForEdit(this, currentActivity.activityId)
                }

                binding.btnDelete.setOnClickListener { deleteActivity() }
            }
        }
    }

    private fun enrollActivity() {
        prefs.getCurrentUser().takeIf { it != -1 }?.let { userId ->
            if (dbHelper.enrollActivity(userId, currentActivity.activityId) != -1L) {
                showToast("报名成功")
                loadActivityData()
            } else {
                showToast("报名失败")
            }
        } ?: showToast("请先登录")
    }

    private fun deleteActivity() {
        if (dbHelper.deleteActivity(currentActivity.activityId, currentActivity.creatorId) > 0) {
            showToast("删除成功")
            setResult(Activity.RESULT_OK)
            finish()
        } else {
            showToast("删除失败")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}