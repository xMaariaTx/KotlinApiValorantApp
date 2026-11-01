package com.example.apilist.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.api_lists.data.local.FavoriteAgentDao
import com.example.api_lists.data.local.FavoriteAgentEntity
import kotlin.jvm.java

@Database(entities = [FavoriteAgentEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteagentdao(): FavoriteAgentDao
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "valorant_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}