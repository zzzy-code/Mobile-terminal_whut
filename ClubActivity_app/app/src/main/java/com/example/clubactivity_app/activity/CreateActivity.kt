package com.example.clubactivity_app.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import com.example.clubactivity_app.R
import com.example.clubactivity_app.database.DatabaseHelper
import com.example.clubactivity_app.database.models.ClubActivity
import com.example.clubactivity_app.databinding.ActivityCreateBinding
import com.example.clubactivity_app.util.PreferenceHelper

class CreateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateBinding
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var prefs: PreferenceHelper
    private var editingActivity: ClubActivity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create)
        binding.lifecycleOwner = this

        dbHelper = DatabaseHelper(this)
        prefs = PreferenceHelper(this)

        checkEditMode()
        setupUI()
    }

    private fun setupUI() {
        binding.btnSave.setOnClickListener {
            if (validateInput()) saveActivity()
        }

        binding.etDate.setOnClickListener {
            showDatePicker()
        }
    }

    private fun showDatePicker() {
        val calendar = java.util.Calendar.getInstance()
        val year = calendar.get(java.util.Calendar.YEAR)
        val month = calendar.get(java.util.Calendar.MONTH)
        val day = calendar.get(java.util.Calendar.DAY_OF_MONTH)

        val datePickerDialog = android.app.DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val dateString = "${selectedYear}-${selectedMonth + 1}-${selectedDay}"
            binding.etDate.setText(dateString)
        }, year, month, day)

        datePickerDialog.show()
    }


    private fun checkEditMode() {
        intent.getIntExtra("activity_id", -1).takeIf { it != -1 }?.let { activityId ->
            dbHelper.getActivityById(activityId)?.let { activity ->
                editingActivity = activity.copy(
                    currentUserId = prefs.getCurrentUser()
                )
                binding.activity = editingActivity
            }
        } ?: run {
            binding.activity = ClubActivity(
                creatorId = prefs.getCurrentUser(),
                currentUserId = prefs.getCurrentUser()
            )
        }
    }

    private fun validateInput(): Boolean {
        return when {
            binding.etTitle.text.isNullOrBlank() -> {
                showToast("请输入活动标题")
                false
            }
            binding.etDate.text.isNullOrBlank() -> {
                showToast("请选择活动日期")
                false
            }
            else -> true
        }
    }

    private fun saveActivity() {
        val currentUserId = prefs.getCurrentUser().takeIf { it != -1 } ?: run {
            showToast("用户未登录")
            finish()
            return
        }

        val activity = editingActivity?.copy(
            title = ObservableField(binding.etTitle.text.toString()),
            description = ObservableField(binding.etDescription.text.toString()),
            date = ObservableField(binding.etDate.text.toString())
        ) ?: ClubActivity(
            creatorId = currentUserId,
            title = ObservableField(binding.etTitle.text.toString()),
            description = ObservableField(binding.etDescription.text.toString()),
            date = ObservableField(binding.etDate.text.toString()),
            currentUserId = currentUserId
        )

        val result = if (editingActivity == null) {
            dbHelper.addActivity(activity)
        } else {
            dbHelper.updateActivity(activity)
        }

        if (result != -1L) {
            setResult(Activity.RESULT_OK)
            finish()
        } else {
            showToast("保存失败")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        fun startForEdit(context: Activity, activityId: Int) {
            context.startActivity(Intent(context, CreateActivity::class.java).apply {
                putExtra("activity_id", activityId)
            })
        }
    }
}