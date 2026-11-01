package com.example.apilist.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.api_lists.ui.viewmodel.MainViewModel
import com.example.apilist.data.models.AgentModel
import com.example.apilist.ui.theme.Azul
import com.example.apilist.ui.theme.Rojo
import androidx.compose.runtime.collectAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(onNavegarAlDetall: (AgentModel) -> Unit, viewModel: MainViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- CABECERA (Título y Logo) ---
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            Text(text = "Valorant API",
                modifier = Modifier.padding(top= 15.dp),
                style = TextStyle(
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 35.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(com.example.apilist.R.font.valorantfont))
                )
            )
            Image(
                painter = painterResource(com.example.apilist.R.drawable.logovalo),
                contentDescription = "LogoValo",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .border(5.dp, color = Azul, CircleShape)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        //BARRA DE BÚSQUEDA
        DockedSearchBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            query = viewModel.searchText,
            onQueryChange = { viewModel.onSearchTextChange(it) },
            onSearch = { viewModel.onSearch(it) },
            active = viewModel.active,
            onActiveChange = { viewModel.onSearchActiveChange(it) },
            placeholder = { Text("Search an agent...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            trailingIcon = {
                if (viewModel.active) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        modifier = Modifier.clickable {
                            if (viewModel.searchText.isNotEmpty()) {
                                viewModel.onSearchTextChange("")
                            } else {
                                viewModel.onSearchActiveChange(false)
                            }
                        }
                    )
                }
            }
        ) {
            // HISTORIAL
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    "Recent searches",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.secondary
                )

                viewModel.searchHistory.forEach { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                viewModel.onSearchTextChange(item)
                                viewModel.onSearch(item)
                            }
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(android.R.drawable.ic_menu_recent_history),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp).padding(end = 16.dp)
                        )
                        Text(text = item)
                    }
                }

                if (viewModel.searchHistory.isNotEmpty()) {
                    TextButton(
                        onClick = { viewModel.onClearHistory() },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text("Delete historial", color = Color.Red)
                    }
                }
            }

            // CONTENIDO (LISTA O GRID) ---
            if (viewModel.characterList.isEmpty() && viewModel.searchText.isNotEmpty()) {
                Text(
                    "No agents found",
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.secondary
                )
            } else {
                // Aquí decidimos qué mostrar según la opción del ViewModel
                if (viewModel.vistaSeleccionada == "Grid") {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2), // 2 columnas para el Grid
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        contentPadding = PaddingValues(bottom = 16.dp)
                    ) {
                        items(viewModel.characterList) { agente ->
                            AgentGridItem(agent = agente) { onNavegarAlDetall(it) }
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 16.dp)
                    ) {
                        items(viewModel.characterList) { agente ->
                            AgentListItem(agent = agente) { onNavegarAlDetall(it) }
                        }
                    }
                }
            }
        }
    }
}

// --- COMPONENTE PARA MODO LISTA (Horizontal) ---
@Composable
fun AgentListItem(agent: AgentModel, onClick: (AgentModel) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onClick(agent) },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(12.dp)
        ) {
            AsyncImage(
                model = agent.displayIcon,
                contentDescription = agent.displayName,
                modifier = Modifier.size(50.dp),
                contentScale = ContentScale.Crop,
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = agent.displayName,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                fontFamily = FontFamily(Font(com.example.apilist.R.font.valorantfont))
            )
        }
    }
}

// --- COMPONENTE PARA MODO GRID (Vertical) ---
@Composable
fun AgentGridItem(agent: AgentModel, onClick: (AgentModel) -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { onClick(agent) },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            AsyncImage(
                model = agent.displayIcon,
                contentDescription = agent.displayName,
                modifier = Modifier.size(80.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = agent.displayName,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(com.example.apilist.R.font.valorantfont))
            )
        }
    }
}