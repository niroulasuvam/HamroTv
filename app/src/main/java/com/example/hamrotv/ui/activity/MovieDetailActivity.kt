package com.example.hamrotv.ui.activity

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.hamrotv.model.MovieModel
import com.example.hamrotv.repository.MovieRepositoryImp
import com.example.hamrotv.ui.theme.HamroTVTheme

class MovieDetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val movie = intent.getParcelableExtra<MovieModel>("MOVIE_KEY")

        if (movie == null) {
            Toast.makeText(this, "Movie data is missing", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        setContent {
            HamroTVTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MovieDetailScreen(
                        movie = movie,
                        onBackPressed = { finish() },
                        onWatchTrailer = { url ->
                            openYouTubeLink(url)
                        },
                        onDelete = { deleteMovie(it) },
                        onEdit = {
                            val intent = Intent(this, UpdateMovieActivity::class.java)
                            intent.putExtra("MOVIE_ID", movie.MovieId)
                            startActivity(intent)
                        }
                    )
                }
            }
        }
    }

    private fun openYouTubeLink(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "No app found to open this link", Toast.LENGTH_SHORT).show()
        }
    }

    fun deleteMovie(movieId: String) {
        val repo = MovieRepositoryImp()
        repo.deleteMovie(movieId) { success, message ->
            runOnUiThread {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                if (success) finish()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailScreen(
    movie: MovieModel,
    onBackPressed: () -> Unit,
    onWatchTrailer: (String) -> Unit,
    onDelete: (String) -> Unit,
    onEdit: () -> Unit
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = movie.MovieName.ifEmpty { "Movie Details" },
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
        ) {
            AsyncImage(
                model = movie.imageUrl.ifEmpty { "https://via.placeholder.com/400x600?text=No+Image" },
                contentDescription = "Movie Poster",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .padding(16.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = movie.MovieName.ifEmpty { "Unnamed Movie" },
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Star, contentDescription = "Rating")
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${movie.Rating ?: "N/A"}/10",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Year", style = MaterialTheme.typography.bodySmall)
                            Text(movie.releaseYear.ifEmpty { "N/A" })
                        }
                        Column {
                            Text("Duration", style = MaterialTheme.typography.bodySmall)
                            Text(movie.duration.ifEmpty { "N/A" })
                        }
                        Column {
                            Text("Age Rating", style = MaterialTheme.typography.bodySmall)
                            Text(movie.ageRating.ifEmpty { "N/A" })
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Genres", style = MaterialTheme.typography.titleSmall)
                    Text(movie.genres.ifEmpty { "N/A" })
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Description", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = movie.description.ifEmpty { "No description available" },
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Button(
                onClick = {
                    val trailerUrl = movie.trailerUrl.ifEmpty { "https://www.youtube.com" }
                    onWatchTrailer(trailerUrl)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(50.dp)
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = "Play")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Watch Trailer")
            }

            // Edit & Delete Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { onEdit() },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Edit")
                }

                Spacer(modifier = Modifier.width(12.dp))

                Button(
                    onClick = { onDelete(movie.MovieId) },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Delete")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
