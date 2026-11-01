package com.example.apilist.Navigation

import androidx.compose.ui.graphics.vector.ImageVector
import com.example.api_lists.navigation.Destinations

// Data class per definir els ítems de la BottomBar
data class NavigationItem(
    val label: String,
    val icon: ImageVector,
    val route: Destinations,
    val index: Int
)