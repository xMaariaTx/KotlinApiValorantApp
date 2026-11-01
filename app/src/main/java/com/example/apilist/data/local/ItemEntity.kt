package com.example.api_lists.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites_table")
data class FavoriteAgentEntity(
    @PrimaryKey
    val uuid: String,
    val name: String,
    val imageUrl: String
)