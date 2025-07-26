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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hamrotv.ui.theme.HamroTVTheme

class UpdateMovieActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get movie ID from intent (if passed)
        val movieId = intent.getStringExtra("MOVIE_ID") ?: ""

        setContent {
            HamroTVTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    UpdateMovieScreen(
                        movieId = movieId,
                        onMovieUpdated = {
                            finish()
                        },
                        onBackPressed = {
                            finish()
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateMovieScreen(
    movieId: String,
    onMovieUpdated: () -> Unit,
    onBackPressed: () -> Unit
) {
    // Pre-filled with sample data - in real app, you'd load from database
    var movieName by remember { mutableStateOf("Sample Movie") }
    var movieDesc by remember { mutableStateOf("This is a sample movie description") }
    var rating by remember { mutableStateOf("8.5") }
    var trailerUrl by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("2h 30m") }
    var ageRating by remember { mutableStateOf("PG-13") }
    var releaseYear by remember { mutableStateOf("2024") }
    var genres by remember { mutableStateOf("Action, Adventure") }
    var isLoading by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val scrollState = rememberScrollState()

    // TODO: In real app, load movie data when movieId is provided
    LaunchedEffect(movieId) {
        if (movieId.isNotEmpty()) {
            // Load movie data from database using movieId
            // For now, we'll use the sample data above
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Update Movie",
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
            // Movie ID (read-only)
            if (movieId.isNotEmpty()) {
                OutlinedTextField(
                    value = movieId,
                    onValueChange = { },
                    label = { Text("Movie ID") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = false,
                    singleLine = true
                )
            }

            // Movie Name
            OutlinedTextField(
                value = movieName,
                onValueChange = { movieName = it },
                label = { Text("Movie Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Description
            OutlinedTextField(
                value = movieDesc,
                onValueChange = { movieDesc = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 5
            )

            // Genres
            OutlinedTextField(
                value = genres,
                onValueChange = { genres = it },
                label = { Text("Genres (e.g., Action, Comedy)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Rating
            OutlinedTextField(
                value = rating,
                onValueChange = { rating = it },
                label = { Text("Rating (1-10)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Release Year
            OutlinedTextField(
                value = releaseYear,
                onValueChange = { releaseYear = it },
                label = { Text("Release Year") },
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

            // Trailer URL
            OutlinedTextField(
                value = trailerUrl,
                onValueChange = { trailerUrl = it },
                label = { Text("Trailer URL (optional)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Update Movie Button
            Button(
                onClick = {
                    if (movieName.isNotBlank() && movieDesc.isNotBlank() &&
                        rating.isNotBlank() && releaseYear.isNotBlank()) {

                        val ratingFloat = rating.toFloatOrNull()
                        if (ratingFloat != null && ratingFloat in 1.0..10.0) {
                            isLoading = true

                            // TODO: Add actual movie update logic here
                            // For now, just simulate success
                            Toast.makeText(context, "Movie updated successfully!", Toast.LENGTH_LONG).show()
                            onMovieUpdated()
                        } else {
                            Toast.makeText(context, "Please enter a valid rating (1-10)", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(context, "Please fill in all required fields", Toast.LENGTH_SHORT).show()
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
                        text = "Update Movie",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Delete Button (optional)
            OutlinedButton(
                onClick = {
                    // TODO: Add delete confirmation dialog
                    Toast.makeText(context, "Delete functionality - coming soon!", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Delete Movie")
            }

            // Cancel Button
            OutlinedButton(
                onClick = onBackPressed,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cancel")
            }
        }
    }
}