package com.example.api_lists.navigation

import android.R
import kotlinx.serialization.Serializable

sealed class Destinations {
    @Serializable
    object ListRoute : Destinations()
    @Serializable
    data class DetailRoute(val itemId: String) : Destinations()
    @Serializable
    object FavoritesScreen : Destinations()
    @Serializable
    object SettingsScreen : Destinations()
}
