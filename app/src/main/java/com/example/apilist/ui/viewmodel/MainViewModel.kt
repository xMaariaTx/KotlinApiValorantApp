package com.example.api_lists.ui.viewmodel

import android.R
import android.util.Log
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.api_lists.data.MainRepository
import com.example.api_lists.data.local.FavoriteAgentDao
import com.example.api_lists.data.local.FavoriteAgentEntity
import com.example.api_lists.data.preferences.SettingsRepository
import com.example.apilist.data.models.AgentModel
import com.example.apilist.data.models.ValorantAgents
import com.example.apilist.ui.screens.SettingsScreen
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(repository: MainRepository, val dao: FavoriteAgentDao, private val settings: SettingsRepository) : ViewModel() {
    /*
  Es el cerebro de la interfaz que guarda los datos a mostrar, gestiona las
  acciones del usuario (como buscar personajes o marcarlos como favoritos) y le pide
  información al Repositorio para mantener la pantalla siempre actualizada.
   */

    // Estats API
    private val MainRepository = MainRepository()

    // Ara la llista és de tipus 'Result'
    var characterList by mutableStateOf<List<AgentModel>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    init {
        fetchCharacters()
    }

    fun fetchCharacters() {
        viewModelScope.launch {
            isLoading = true
            characterList = MainRepository.getCharactersFromApi()
            listFilterCharacter = MainRepository.getCharactersFromApi()
            isLoading = false
        }
    }

    // --- API ---
//    private fun fetchApiData() {
//        viewModelScope.launch {
//            isLoading.value = true
//            try {
//                val response = repository.getItemsFromApi()
//
//                _apiList.value = response.results
//                _apiListFiltered.value = response.results
//
//            } catch (e: Exception) {
//                Log.e("FETCH_ERROR", "Error descarregant dades: ${e.message}")
//                e.printStackTrace()
//            } finally {
//                isLoading.value = false
//            }
//        }
//    }

    // Estats Detall
    var isDarkMode by mutableStateOf(false) // Por defecto en modo claro
        private set

    fun toggleDarkMode(enabled: Boolean) {
        isDarkMode = enabled
    }

    // Estat Grid o List
    var selectedOption by mutableStateOf("List") // "List" o "Grid"
        private set

    fun updateSelectedOption(option: String) {
        selectedOption = option
    }

    // --- ROOM / FAVORITOS ---
    // Este Flow observa la base de datos constantemente y avisa a la UI de cualquier cambio
    val favoritesList: StateFlow<List<FavoriteAgentEntity>> = dao.getFavorites()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // FUNCIÓN ÚNICA: Añade o quita basándose en el UUID real del agente
    fun toggleFavorite(agent: AgentModel) {
        viewModelScope.launch {
            val entity = FavoriteAgentEntity(
                uuid = agent.uuid,
                name = agent.displayName,
                imageUrl = agent.displayIcon ?: ""
            )

            // Consultamos a la base de datos si ESTE agente específico ya existe
            val isAlreadyFavorite = dao.isFavorite(agent.uuid)

            if (isAlreadyFavorite) {
                dao.deleteFavorite(entity)
            } else {
                dao.insertFavorite(entity)
            }
        }
    }
    fun clearAllFavorites() {
        viewModelScope.launch {
            dao.deleteAllFavorites()
        }
    }

    // SearchBar
    var active by mutableStateOf(false)
    var searchText by mutableStateOf("")
        private set
    //var searchActive = mutableStateOf(false)
        //private set

    var searchHistory = mutableStateListOf<String>().apply {
        addAll(settings.leerHistorial())
    }
        private set

    //var filteredValorant = mutableStateListOf<ValorantAgents>()
        //private set

    var listFilterCharacter by mutableStateOf<List<AgentModel>>(emptyList())
        private set

    // --- SEARCH ---
    fun onSearchTextChange(text: String) {
        searchText = text
        characterList =
            if (text.isEmpty()) {
                listFilterCharacter
            }
            else {
                listFilterCharacter.filter { agent ->
                    agent.displayName.contains(text, ignoreCase = true)
                }
            }
    }
    fun onSearchActiveChange(isActive: Boolean) {
        active = isActive
        if (!isActive) {
            searchText = ""
        }
    }

    fun onSearch(text: String) {
        if (text.isNotBlank()) {
            // Si el texto ya estaba en el historial, lo quitamos para ponerlo el primero
            if (searchHistory.contains(text)) {
                searchHistory.remove(text)
            }
            searchHistory.add(0, text)
            settings.guardarHistorial(searchHistory.toList())
           // active = false
        }
    }

    // NUEVO: Para el botón de "Esborrar tot l'historial"
    fun onClearHistory() {
        searchHistory.clear()
        settings.guardarHistorial(emptyList())
    }

    fun getFavorites() {
        TODO("Not yet implemented")
    }

    init {
        //fetchApiData()
        //loadFavorites()
    }

    //SettingsRespository
    // Al iniciar, leemos lo que haya guardado en el "bloc de notas"
    var estaEnModoOscuro by mutableStateOf(settings.leerModoOscuro())
        private set

    var vistaSeleccionada by mutableStateOf(settings.leerTipoVista())
        private set

    // Modificar modo oscuro y guardar
    fun actualizarModoOscuro(enabled: Boolean) {
        estaEnModoOscuro = enabled
        settings.guardarModoOscuro(enabled) // <--- SE GUARDA
    }

    // Modificar vista y guardar
    fun actualizarTipoVista(option: String) {
        vistaSeleccionada = option
        settings.guardarTipoVista(option) // <--- SE GUARDA
    }
}


// Factory
class MainViewModelFactory(
    private val repository: MainRepository,
    private val dao: FavoriteAgentDao,
    private val settings: SettingsRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(repository, dao, settings) as T
    }
}
