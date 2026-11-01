package com.example.bottombarnavigation.navigation

import FavoritesScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.api_lists.navigation.Destinations
import com.example.api_lists.ui.viewmodel.MainViewModel
import com.example.apilist.data.models.AgentModel
import com.example.apilist.ui.screens.ListScreen
import com.example.apilist.ui.screens.PantallaDetall
import com.example.apilist.ui.screens.SettingsScreen

@Composable
fun NavigationWrapper(navController: NavHostController, viewModel: MainViewModel) {
    var characterSelect by remember { mutableStateOf<AgentModel?>(null) }

    NavHost(navController = navController, startDestination = Destinations.ListRoute) {

        composable<Destinations.ListRoute> {
            characterSelect?.let { agente ->
                PantallaDetall(
                    character = agente,
                    onBack = { characterSelect = null },
                    viewModel = viewModel
                )
            } ?: run {
                ListScreen(
                    viewModel = viewModel,
                    onNavegarAlDetall = { character ->
                        characterSelect = character
                    }
                )
            }
        }

        composable<Destinations.FavoritesScreen> {
            FavoritesScreen(
                viewModel = viewModel,
                onAgentClick = { uuid ->
                    // Buscamos el agente en la lista descargada de la API
                    val agent = viewModel.characterList.find { it.uuid == uuid }
                    if (agent != null) {
                        characterSelect = agent
                        // Navegamos a la ruta de la lista para que se muestre el detalle
                        navController.navigate(Destinations.ListRoute)
                    }
                }
            )
        }
        composable<Destinations.SettingsScreen> {
            SettingsScreen(viewModel = viewModel)
        }
    }
}
