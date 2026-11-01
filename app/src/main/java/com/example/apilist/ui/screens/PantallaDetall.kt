package com.example.apilist.ui.screens

import android.R.attr.fontWeight
import android.R.attr.onClick
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.api_lists.ui.viewmodel.MainViewModel
import com.example.apilist.R
import com.example.apilist.data.models.AgentModel
import com.example.apilist.data.models.Role
import com.example.apilist.ui.theme.Azul
import com.example.apilist.ui.theme.Rojo
import okhttp3.internal.userAgent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState

@Composable
fun PantallaDetall(character: AgentModel, onBack: () -> Unit, viewModel: MainViewModel) {
    // 1. Escuchamos la lista de favoritos de la base de datos
    val favorites by viewModel.favoritesList.collectAsState()

    // 2. Comprobamos si el agente que estamos viendo está en esa lista
    // Esta línea es la que hace que cada personaje sea independiente
    val isFavorite = favorites.any { it.uuid == character.uuid }
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "About ${character.displayName}",
                Modifier.padding(start = 55.dp, top = 15.dp, end = 55.dp, bottom = 0.dp),
                style = TextStyle(
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 27.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.valorantfont))
                )
            )
            Spacer(modifier = Modifier.height(10.dp))

            AsyncImage(
                model = character.displayIcon,
                contentDescription = character.displayName,
                modifier = Modifier.size(100.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = character.role.displayName,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    fontFamily = FontFamily(Font(com.example.apilist.R.font.valorantfont))
                )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = character.description,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = onBack,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary, // Rojo en Light / Turquesa en Dark
                    contentColor = MaterialTheme.colorScheme.onPrimary  // Color del texto dentro del botón
                ),
                border = BorderStroke(5.dp, color = MaterialTheme.colorScheme.primary)
            ) {
                Text(text = "Return to Menu",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    fontFamily = FontFamily(Font(com.example.apilist.R.font.valorantfont))
                )
            }

            // --- BOTÓN DEL CORAZÓN ---
            IconToggleButton(
                checked = isFavorite,
                onCheckedChange = { viewModel.toggleFavorite(character) }
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite",
                    modifier = Modifier.size(50.dp),
                    tint = if (isFavorite) Color.Red else MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
