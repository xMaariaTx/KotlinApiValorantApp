package com.example.api_lists.data.api

import com.example.apilist.data.models.ValorantAgents
import retrofit2.Response
import retrofit2.http.GET

interface APIService {
    @GET("agents")
    suspend fun getCharacters(): Response<ValorantAgents>
}
