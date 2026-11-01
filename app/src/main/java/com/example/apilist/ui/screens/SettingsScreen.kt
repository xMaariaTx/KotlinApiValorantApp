package com.example.apilist.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.api_lists.ui.viewmodel.MainViewModel
import com.example.apilist.ui.theme.Azul
import com.example.apilist.ui.theme.AzulDarkMode
import com.example.apilist.ui.theme.Rojo

@Composable
fun SettingsScreen(viewModel: MainViewModel) {
    // Estados locales para la UI de esta pantalla
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Settings",
            modifier = Modifier.padding(top = 15.dp),
            style = TextStyle(
                color = MaterialTheme.colorScheme.primary,
                fontSize = 35.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(com.example.apilist.R.font.valorantfont))
            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Fila Dark Mode
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Dark Mode",
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                fontFamily = FontFamily(Font(com.example.apilist.R.font.valorantfont))
            )
            Switch(
                checked = viewModel.estaEnModoOscuro,
                onCheckedChange = { viewModel.actualizarModoOscuro(it) }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Fila Show Mode (List/Grid)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Show Mode",
                fontSize = 18.sp,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                fontFamily = FontFamily(Font(com.example.apilist.R.font.valorantfont))
            )
            Box {
                OutlinedCard(
                    onClick = { expanded = true },
                    shape = RoundedCornerShape(4.dp),
                    border = BorderStroke(1.dp, Color.LightGray)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Leemos la opción directamente desde el ViewModel
                        Text(text = viewModel.vistaSeleccionada)
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                    }
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("List") },
                        onClick = {
                            viewModel.actualizarTipoVista("List") // Guardamos el cambio
                            expanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Grid") },
                        onClick = {
                            viewModel.actualizarTipoVista("Grid") // Guardamos el cambio
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Botón Borrar
        Button(
            onClick = { viewModel.clearAllFavorites() },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary, // Rojo en Light / Turquesa en Dark
                contentColor = MaterialTheme.colorScheme.onPrimary  // Color del texto dentro del botón
            ),
            border = BorderStroke(2.dp, color = MaterialTheme.colorScheme.primary)
        ) {
            Text(text = "Delete Fav",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                fontFamily = FontFamily(Font(com.example.apilist.R.font.valorantfont))
            )
        }
    }
}