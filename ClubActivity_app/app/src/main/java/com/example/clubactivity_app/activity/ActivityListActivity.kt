package com.example.clubactivity_app.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.clubactivity_app.auth.LoginActivity
import com.example.clubactivity_app.database.DatabaseHelper
import com.example.clubactivity_app.database.models.ClubActivity
import com.example.clubactivity_app.databinding.ActivityListBinding
import com.example.clubactivity_app.databinding.ItemActivityBinding
import com.example.clubactivity_app.util.PreferenceHelper

class ActivityListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListBinding
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var adapter: ActivityAdapter

    private val createActivityLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { if (it.resultCode == RESULT_OK) refreshActivityList() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogout.setOnClickListener {
            PreferenceHelper(this).clearSession()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        dbHelper = DatabaseHelper(this)
        setupUI()
        refreshActivityList()
    }

    private fun setupUI() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ActivityAdapter(emptyList()) { activity ->
            startActivity(Intent(this, ActivityDetailActivity::class.java).apply {
                putExtra("activity_id", activity.activityId)
            })
        }
        binding.recyclerView.adapter = adapter

        binding.fabCreate.setOnClickListener {
            createActivityLauncher.launch(Intent(this, CreateActivity::class.java))
        }
    }

    private fun refreshActivityList() {
        adapter.updateData(dbHelper.getActivities().map {
            it.copy(currentUserId = PreferenceHelper(this).getCurrentUser())
        })
    }

    inner class ActivityAdapter(
        private var activities: List<ClubActivity>,
        private val onClick: (ClubActivity) -> Unit
    ) : RecyclerView.Adapter<ActivityAdapter.ViewHolder>() {

        inner class ViewHolder(val binding: ItemActivityBinding) : RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding = ItemActivityBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return ViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.binding.activity = activities[position] // 直接绑定数据对象
            holder.itemView.setOnClickListener { onClick(activities[position]) }
        }

        override fun getItemCount() = activities.size

        @SuppressLint("NotifyDataSetChanged")
        fun updateData(newData: List<ClubActivity>) {
            activities = newData
            notifyDataSetChanged()
        }
    }
}