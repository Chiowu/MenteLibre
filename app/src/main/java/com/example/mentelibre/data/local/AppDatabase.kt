package com.example.mentelibre.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mentelibre.data.local.dao.DiaryDao
import com.example.mentelibre.data.local.dao.MoodDao
import com.example.mentelibre.data.local.dao.UserDao
import com.example.mentelibre.data.local.entity.DiaryEntryEntity
import com.example.mentelibre.data.local.entity.MoodEntryEntity
import com.example.mentelibre.local.entity.UserEntity

@Database(
    entities = [
        UserEntity::class,
        MoodEntryEntity::class,
        DiaryEntryEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun moodDao(): MoodDao
    abstract fun diaryDao(): DiaryDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "mente_libre_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
