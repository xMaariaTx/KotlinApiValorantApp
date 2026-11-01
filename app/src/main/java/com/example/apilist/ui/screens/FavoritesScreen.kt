import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.api_lists.ui.viewmodel.MainViewModel
import com.example.apilist.ui.theme.Rojo

@Composable
fun FavoritesScreen(viewModel: MainViewModel, onAgentClick: (String) -> Unit) {
    // 1. Recolectamos la lista de la DB
    val favorites by viewModel.dao.getFavorites().collectAsState(initial = emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Favorites",
            modifier = Modifier.padding(top = 15.dp, bottom = 20.dp),
            style = TextStyle(
                color = MaterialTheme.colorScheme.primary, // Usando tu variable de color Rojo
                fontSize = 35.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(com.example.apilist.R.font.valorantfont))
            )
        )

        if (favorites.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "You don't have favorites right now", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        } else {
            // 2. Lista de personajes favoritos
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(favorites) { agent ->
                    FavoriteCard(agent = agent, onClick = { onAgentClick(agent.uuid) })
                }
            }
        }
    }
}

@Composable
fun FavoriteCard(agent: com.example.api_lists.data.local.FavoriteAgentEntity, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = agent.imageUrl,
                contentDescription = agent.name,
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = agent.name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                fontFamily = FontFamily(Font(com.example.apilist.R.font.valorantfont))
            )
        }
    }
}