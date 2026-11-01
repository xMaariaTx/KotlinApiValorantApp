package com.example.api_lists

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.room.Dao
import com.example.api_lists.data.MainRepository
import com.example.api_lists.data.preferences.SettingsRepository
import com.example.api_lists.navigation.Destinations
import com.example.api_lists.ui.viewmodel.MainViewModel
import com.example.apilist.data.local.AppDatabase
import com.example.apilist.ui.theme.ApiListTheme
import com.example.bottombarnavigation.navigation.NavigationWrapper

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        val db = AppDatabase.getDatabase(this)
        val agentDao = db.favoriteagentdao()
        val settingsRepository = SettingsRepository(this)

        setContent {
            //ResponsiveAdaptative
            // 1. Calculamos el tamaño de la pantalla
            val windowSize = calculateWindowSizeClass(this)
            val widthSizeClass = windowSize.widthSizeClass

            // 1. Instanciamos el ViewModel
            val viewModel: MainViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        // 2. Pasa el agentDao que acabas de crear arriba
                        return MainViewModel(MainRepository(), agentDao, settingsRepository) as T
                    }
                }
            )
            // 2. Configuramos el NavController y observamos la ruta actual
            val navController = rememberNavController()
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            // 3. Definimos los items de la BottomBar
            val items = listOf(
                com.example.apilist.Navigation.NavigationItem("Home", Icons.Default.Home, Destinations.ListRoute, 0),
                com.example.apilist.Navigation.NavigationItem("Favorites", Icons.Default.Favorite, Destinations.FavoritesScreen, 1),
                com.example.apilist.Navigation.NavigationItem("Settings", Icons.Default.Settings, Destinations.SettingsScreen, 2)
            )

            // 4. Aplicamos el Tema pasando el estado isDarkMode del ViewModel
            ApiListTheme(darkTheme = viewModel.estaEnModoOscuro) {
                // El Scaffold es el contenedor principal que organiza la barra inferior y el contenido
                Scaffold(
                    bottomBar = {
                        NavigationBar {
                            items.forEach { item ->
                                // Comprobamos si la ruta actual coincide con el item para iluminarlo
                                val isSelected = currentRoute?.contains(item.route::class.qualifiedName ?: "") == true

                                NavigationBarItem(
                                    selected = isSelected,
                                    label = { Text(item.label) },
                                    icon = { Icon(imageVector = item.icon, contentDescription = item.label) },
                                    onClick = {
                                        navController.navigate(item.route) {
                                            // Navegación limpia para evitar acumular pantallas
                                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    // 5. Surface aplica automáticamente el color de fondo del modo oscuro/claro
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding), // Evita que el contenido quede detrás de la BottomBar
                        color = MaterialTheme.colorScheme.background
                    ) {
                        // Llamamos al Wrapper una sola vez pasando el viewModel compartido
                        NavigationWrapper(navController = navController, viewModel = viewModel)
                    }
                }
            }
        }
    }
}