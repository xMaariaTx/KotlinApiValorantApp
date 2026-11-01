package com.example.api_lists.data.preferences

import android.content.Context

class SettingsRepository(context: Context) {
    private val prefs = context.getSharedPreferences("AppSettings", Context.MODE_PRIVATE)

    fun leerModoOscuro(): Boolean = prefs.getBoolean("dark_mode", false)
    fun guardarModoOscuro(activado: Boolean) = prefs.edit().putBoolean("dark_mode", activado).apply()

    fun leerTipoVista(): String = prefs.getString("view_mode", "List") ?: "List"
    fun guardarTipoVista(tipo: String) = prefs.edit().putString("view_mode", tipo).apply()

    fun leerHistorial(): List<String> {
        val set = prefs.getStringSet("search_history", emptySet()) ?: emptySet()
        return set.toList().reversed() // Invertimos para que la más reciente salga primero
    }

    fun guardarHistorial(historial: List<String>) {
        prefs.edit().putStringSet("search_history", historial.toSet()).apply()
    }
}