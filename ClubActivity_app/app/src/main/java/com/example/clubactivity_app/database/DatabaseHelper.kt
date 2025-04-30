package com.example.clubactivity_app.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.databinding.ObservableField
import com.example.clubactivity_app.database.models.ClubActivity
import com.example.clubactivity_app.database.models.User
import java.security.MessageDigest

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "club_activity.db"
        const val DATABASE_VERSION = 1

        // User Table
        const val TABLE_USERS = "users"
        const val COLUMN_USER_ID = "user_id"
        const val COLUMN_USERNAME = "username"
        const val COLUMN_PASSWORD = "password"

        // Activity Table
        const val TABLE_ACTIVITIES = "activities"
        const val COLUMN_ACTIVITY_ID = "activity_id"
        const val COLUMN_CREATOR_ID = "creator_id"
        const val COLUMN_TITLE = "title"
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_DATE = "date"

        // Enrollment Table
        const val TABLE_ENROLLMENTS = "enrollments"
        const val COLUMN_ENROLL_ID = "enroll_id"
    }

    override fun onCreate(db: SQLiteDatabase) {
        createUserTable(db)
        createActivityTable(db)
        createEnrollmentTable(db)
    }

    private fun createUserTable(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_USERS (
                $COLUMN_USER_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_USERNAME TEXT UNIQUE NOT NULL,
                $COLUMN_PASSWORD TEXT NOT NULL
            )
        """.trimIndent()
        db.execSQL(createTable)
    }

    private fun createActivityTable(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_ACTIVITIES (
                $COLUMN_ACTIVITY_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_CREATOR_ID INTEGER NOT NULL,
                $COLUMN_TITLE TEXT NOT NULL,
                $COLUMN_DESCRIPTION TEXT,
                $COLUMN_DATE TEXT NOT NULL,
                FOREIGN KEY ($COLUMN_CREATOR_ID) REFERENCES $TABLE_USERS ($COLUMN_USER_ID)
            )
        """.trimIndent()
        db.execSQL(createTable)
    }

    private fun createEnrollmentTable(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_ENROLLMENTS (
                $COLUMN_ENROLL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_USER_ID INTEGER NOT NULL,
                $COLUMN_ACTIVITY_ID INTEGER NOT NULL,
                UNIQUE ($COLUMN_USER_ID, $COLUMN_ACTIVITY_ID),
                FOREIGN KEY ($COLUMN_USER_ID) REFERENCES $TABLE_USERS ($COLUMN_USER_ID),
                FOREIGN KEY ($COLUMN_ACTIVITY_ID) REFERENCES $TABLE_ACTIVITIES ($COLUMN_ACTIVITY_ID)
            )
        """.trimIndent()
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ENROLLMENTS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ACTIVITIES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }

    // User operations
    fun addUser(username: String, password: String): Long {
        val values = ContentValues().apply {
            put(COLUMN_USERNAME, username)
            put(COLUMN_PASSWORD, sha256(password))
        }
        return writableDatabase.insert(TABLE_USERS, null, values)
    }

    fun getUser(username: String): User? {
        val cursor = readableDatabase.query(
            TABLE_USERS,
            arrayOf(COLUMN_USER_ID, COLUMN_USERNAME, COLUMN_PASSWORD),
            "$COLUMN_USERNAME = ?",
            arrayOf(username),
            null, null, null
        )

        return if (cursor.moveToFirst()) {
            User(
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD))
            )
        } else {
            null
        }.also { cursor.close() }
    }

    // Activity operations
    fun addActivity(activity: ClubActivity): Long {
        val values = ContentValues().apply {
            put(COLUMN_CREATOR_ID, activity.creatorId)
            put(COLUMN_TITLE, activity.title.get() ?: "")  // 处理ObservableField
            put(COLUMN_DESCRIPTION, activity.description.get() ?: "")
            put(COLUMN_DATE, activity.date.get() ?: "")
        }
        return writableDatabase.insert(TABLE_ACTIVITIES, null, values)
    }

    fun getActivities(): List<ClubActivity> {
        val activities = mutableListOf<ClubActivity>()
        val cursor = readableDatabase.rawQuery(
            "SELECT * FROM $TABLE_ACTIVITIES ORDER BY $COLUMN_DATE DESC", null
        )

        while (cursor.moveToNext()) {
            activities.add(
                ClubActivity(
                    activityId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ACTIVITY_ID)),
                    creatorId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CREATOR_ID)),
                    title = ObservableField(cursor.getString(cursor.getColumnIndexOrThrow(
                        COLUMN_TITLE
                    ))),  // 转换为ObservableField
                    description = ObservableField(cursor.getString(cursor.getColumnIndexOrThrow(
                        COLUMN_DESCRIPTION
                    ))),
                    date = ObservableField(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE))),
                    currentUserId = -1  // 需后续设置
                )
            )
        }
        cursor.close()
        return activities
    }

    fun deleteActivity(activityId: Int, creatorId: Int): Int {
        return writableDatabase.delete(
            TABLE_ACTIVITIES,
            "$COLUMN_ACTIVITY_ID = ? AND $COLUMN_CREATOR_ID = ?",
            arrayOf(activityId.toString(), creatorId.toString())
        )
    }

    // Enrollment operations
    fun enrollActivity(userId: Int, activityId: Int): Long {
        val values = ContentValues().apply {
            put(COLUMN_USER_ID, userId)
            put(COLUMN_ACTIVITY_ID, activityId)
        }
        return writableDatabase.insert(TABLE_ENROLLMENTS, null, values)
    }

    fun getEnrollmentsCount(activityId: Int): Int {
        val cursor = readableDatabase.rawQuery(
            "SELECT COUNT(*) FROM $TABLE_ENROLLMENTS WHERE $COLUMN_ACTIVITY_ID = ?",
            arrayOf(activityId.toString())
        )
        val count = if (cursor.moveToFirst()) cursor.getInt(0) else 0
        cursor.close()
        return count
    }

    // Activity查询扩展
    fun getActivityById(activityId: Int): ClubActivity? {
        val cursor = readableDatabase.query(
            TABLE_ACTIVITIES,
            null,
            "$COLUMN_ACTIVITY_ID = ?",
            arrayOf(activityId.toString()),
            null, null, null
        )

        return if (cursor.moveToFirst()) {
            ClubActivity(
                activityId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ACTIVITY_ID)),
                creatorId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CREATOR_ID)),
                title = ObservableField(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))),
                description = ObservableField(cursor.getString(cursor.getColumnIndexOrThrow(
                    COLUMN_DESCRIPTION
                ))),
                date = ObservableField(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE))),
                currentUserId = -1  // 需后续设置
            )
        } else {
            null
        }.also { cursor.close() }
    }

    fun updateActivity(activity: ClubActivity): Int {
        val values = ContentValues().apply {
            put(COLUMN_TITLE, activity.title.get() ?: "")  // 处理ObservableField
            put(COLUMN_DESCRIPTION, activity.description.get() ?: "")
            put(COLUMN_DATE, activity.date.get() ?: "")
        }

        return writableDatabase.update(
            TABLE_ACTIVITIES,
            values,
            "$COLUMN_ACTIVITY_ID = ?",
            arrayOf(activity.activityId.toString())
        )
    }

    // 工具方法
    private fun sha256(input: String): String {
        return MessageDigest
            .getInstance("SHA-256")
            .digest(input.toByteArray())
            .fold("") { str, it -> str + "%02x".format(it) }
    }

    fun validatePassword(user: User?, plainPassword: String): Boolean {
        return user?.password == sha256(plainPassword)
    }
}