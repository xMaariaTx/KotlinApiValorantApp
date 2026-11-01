package com.example.api_lists.data


import com.example.api_lists.data.api.RetrofitClient
import com.example.apilist.data.models.AgentModel
import com.example.apilist.data.models.ValorantAgents
import com.google.firebase.appdistribution.gradle.ApiService
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class MainRepository{

    //Clase encargada de organizar las operacions de la API y la BD

    //suspend fun getItemsFromApi() = apiService.getItems()

    //suspend fun insertItem(item: ItemEntity) {

    //  itemDao.insertFavorite(item)
    //}

    suspend fun getCharactersFromApi(): List<AgentModel>{
        return try {
            val response = RetrofitClient.instance.getCharacters()

            if (response.isSuccessful) {
                // Si va bé, retornem la llista que hi ha dins de .results
                // Si és null, retornem llista buida
                response.body()?.data ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            android.util.Log.e("API_ERROR", "Error: ${e.message}")
            emptyList()

        }
    }

}
