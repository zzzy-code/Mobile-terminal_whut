package com.example.clubactivity_app.database.models

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField

data class ClubActivity(
    val activityId: Int = 0,
    val creatorId: Int,
    val title: ObservableField<String> = ObservableField(""),
    val description: ObservableField<String> = ObservableField(""),
    val date: ObservableField<String> = ObservableField(""),
    val currentUserId: Int = -1
) {
    val isCreator: ObservableBoolean = ObservableBoolean(creatorId == currentUserId)
}