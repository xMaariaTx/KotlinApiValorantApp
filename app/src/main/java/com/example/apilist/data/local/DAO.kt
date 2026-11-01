package com.example.api_lists.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteAgentDao {
    @Query("SELECT * FROM favorites_table")
    fun getFavorites(): Flow<List<FavoriteAgentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(item: FavoriteAgentEntity)

    @Delete
    suspend fun deleteFavorite(item: FavoriteAgentEntity)

    @Query("DELETE FROM favorites_table")
    suspend fun deleteAllFavorites()

    @Query("SELECT EXISTS(SELECT 1 FROM favorites_table WHERE uuid = :uuid)")
    suspend fun isFavorite(uuid: String): Boolean

}