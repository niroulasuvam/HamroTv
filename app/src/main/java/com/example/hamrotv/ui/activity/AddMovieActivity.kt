package com.example.hamrotv.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hamrotv.ui.theme.HamroTVTheme
import com.example.hamrotv.repository.MovieRepositoryImp
import com.example.hamrotv.model.MovieModel

class AddMovieActivity : ComponentActivity() {
    private lateinit var movieRepository: MovieRepositoryImp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        movieRepository = MovieRepositoryImp()

        setContent {
            HamroTVTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AddMovieScreen(
                        onMovieAdded = {
                            finish()
                        },
                        onBackPressed = {
                            finish()
                        },
                        movieRepository = movieRepository
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMovieScreen(
    onMovieAdded: () -> Unit,
    onBackPressed: () -> Unit,
    movieRepository: MovieRepositoryImp
) {
    var movieName by remember { mutableStateOf("") }
    var movieDesc by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf("") }
    var trailerUrl by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("") }
    var ageRating by remember { mutableStateOf("") }
    var releaseYear by remember { mutableStateOf("") }
    var genres by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Add New Movie",
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
                .padding(16.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Movie Name
            OutlinedTextField(
                value = movieName,
                onValueChange = { movieName = it },
                label = { Text("Movie Name *") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Description
            OutlinedTextField(
                value = movieDesc,
                onValueChange = { movieDesc = it },
                label = { Text("Description *") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 5
            )

            // Genres
            OutlinedTextField(
                value = genres,
                onValueChange = { genres = it },
                label = { Text("Genres (e.g., Action, Comedy) *") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Rating
            OutlinedTextField(
                value = rating,
                onValueChange = { rating = it },
                label = { Text("Rating (1-10) *") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Release Year
            OutlinedTextField(
                value = releaseYear,
                onValueChange = { releaseYear = it },
                label = { Text("Release Year *") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Duration
            OutlinedTextField(
                value = duration,
                onValueChange = { duration = it },
                label = { Text("Duration (e.g., 2h 30m)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Age Rating
            OutlinedTextField(
                value = ageRating,
                onValueChange = { ageRating = it },
                label = { Text("Age Rating (e.g., PG-13)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Image URL
            OutlinedTextField(
                value = imageUrl,
                onValueChange = { imageUrl = it },
                label = { Text("Image URL (optional)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Trailer URL
            OutlinedTextField(
                value = trailerUrl,
                onValueChange = { trailerUrl = it },
                label = { Text("Trailer URL (optional)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Add Movie Button
            Button(
                onClick = {
                    if (movieName.isNotBlank() && movieDesc.isNotBlank() &&
                        rating.isNotBlank() && releaseYear.isNotBlank() && genres.isNotBlank()) {

                        val ratingFloat = rating.toFloatOrNull()
                        if (ratingFloat != null && ratingFloat in 1.0..10.0) {
                            isLoading = true

                            // Create movie object
                            val newMovie = MovieModel(
                                MovieName = movieName.trim(),
                                description = movieDesc.trim(),
                                genres = genres.trim(),
                                Rating = ratingFloat,
                                releaseYear = releaseYear.trim(),
                                duration = duration.trim(),
                                ageRating = ageRating.trim(),
                                imageUrl = imageUrl.trim(),
                                trailerUrl = trailerUrl.trim()
                            )

                            // Save to Firebase
                            movieRepository.addMovie(newMovie) { success, message ->
                                isLoading = false
                                if (success) {
                                    Toast.makeText(context, "Movie added successfully!", Toast.LENGTH_LONG).show()
                                    onMovieAdded()
                                } else {
                                    Toast.makeText(context, "Failed to add movie: $message", Toast.LENGTH_LONG).show()
                                }
                            }
                        } else {
                            Toast.makeText(context, "Please enter a valid rating (1-10)", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(context, "Please fill in all required fields (*)", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(
                        text = "Add Movie",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Cancel Button
            OutlinedButton(
                onClick = onBackPressed,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cancel")
            }

            // Required fields note
            Text(
                text = "* Required fields",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}