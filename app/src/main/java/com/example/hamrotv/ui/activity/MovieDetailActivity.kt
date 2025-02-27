package com.example.hamrotv.ui.activity

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.hamrotv.databinding.ActivityMovieDetailBinding
import com.example.hamrotv.model.MovieModel
import com.squareup.picasso.Picasso

class MovieDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMovieDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize binding
        binding = ActivityMovieDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Handle window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

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
                    "Desc: ${movie.description}, Image: ${movie.imageUrl}"
        )

        // Set data to UI with null safety
        binding.movieName.text = movie.MovieName ?: "Unnamed Movie"
        binding.movieRating.text = "Rating: ${movie.Rating ?: 0}"
        binding.movieDescription.text = movie.description ?: "No description available"

        if (!movie.imageUrl.isNullOrEmpty()) {
            Picasso.get().load(movie.imageUrl).into(binding.movieImage)
        } else {
            binding.movieImage.setImageResource(android.R.drawable.ic_menu_gallery) // Fallback image
        }

        // Optional: Uncomment and adjust if you need a button action
        /*
        binding.startButton.setOnClickListener {
            val resultIntent = Intent().apply {
                putExtra("MOVIE_ID", movie.MovieId)
                putExtra("MOVIE_NAME", movie.MovieName)
                putExtra("MOVIE_RATING", movie.Rating)
                putExtra("MOVIE_DESC", movie.description)
                putExtra("MOVIE_IMAGE", movie.imageUrl)
            }
            setResult(RESULT_OK, resultIntent)
            finish()
        }
        */
    }
}