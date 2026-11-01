package com.example.api_lists.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // 1. La URL Base on farem totes les peticions
    // IMPORTANT: Ha d'acabar amb una barra '/' final

    //Es el configurador central de Retrofit, que establece la dirección web principal de la API
    // y crea la conexión lista para usar, traduciendo automáticamente los datos que llegan de Internet a objetos de Kotlin.

    private const val BASE_URL = "https://valorant-api.com/v1/"

    // 2. La instància de Retrofit (La màquina configurada)
    val instance: APIService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) // Això tradueix el JSON automàticament
            .build()
            .create(APIService::class.java) // Creem la implementació de la interfície
    }
}
