package com.example.hamrotv.ui.activity

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hamrotv.databinding.ActivityMovieDetailBinding
import com.example.hamrotv.model.MovieModel
import com.squareup.picasso.Picasso

class MovieDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMovieDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize binding
        binding = ActivityMovieDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get the MovieModel from the Intent
        val movie = intent.getParcelableExtra<MovieModel>("MOVIE_KEY")

        if (movie == null) {
            Toast.makeText(this, "Movie data is missing", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Debugging logs
        Log.d(
            "MovieDetailActivity",
            "Movie Id: ${movie.MovieId}, Name: ${movie.MovieName}, Rating: ${movie.Rating}, " +
                    "Desc: ${movie.description}, Image: ${movie.imageUrl}, Genres: ${movie.genres}"
        )

        // Set data to UI
        setMovieData(movie)

        // Set click listener for the watch button
        binding.watchButton.setOnClickListener {
            openYouTubeLink(movie.trailerUrl ?: "https://www.youtube.com")
        }
    }

    private fun setMovieData(movie: MovieModel) {
        binding.apply {
            // Set movie name
            movieName.text = movie.MovieName.ifEmpty { "Unnamed Movie" }

            // Set movie rating
            movieRating.text = movie.Rating?.let { "Rating: $it/10" } ?: "Rating: N/A"

            // Set release year, duration, and age rating
            releaseYear.text = movie.releaseYear?.toString() ?: "N/A"
            duration.text = movie.duration.ifEmpty { "N/A" }
            ageRating.text = movie.ageRating.ifEmpty { "N/A" }

            // Set genres (now a TextView instead of ChipGroup)
            movieGenres.text = movie.genres.ifEmpty { "N/A" }

            // Set movie description
            movieDescription.text = movie.description.ifEmpty { "No description available" }

            // Load Image with Picasso
            if (!movie.imageUrl.isNullOrEmpty()) {
                Picasso.get()
                    .load(movie.imageUrl)
                    .placeholder(android.R.drawable.ic_menu_gallery) // While loading
                    .error(android.R.drawable.ic_menu_report_image) // If loading fails
                    .into(movieImage)
            } else {
                movieImage.setImageResource(android.R.drawable.ic_menu_gallery) // Fallback
            }
        }
    }

    private fun openYouTubeLink(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // Ensure it opens in a new task
        }
        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "No app found to open this link", Toast.LENGTH_SHORT).show()
        }
    }
}