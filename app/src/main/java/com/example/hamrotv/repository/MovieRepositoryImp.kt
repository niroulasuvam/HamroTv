package com.example.hamrotv.repository

import android.util.Log
import com.example.hamrotv.model.MovieModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MovieRepositoryImp {
    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference
    private val moviesRef: DatabaseReference = database.child("movies")
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    // Add a new movie
    fun addMovie(movie: MovieModel, callback: (Boolean, String) -> Unit) {
        // Generate unique ID for the movie
        val movieId = moviesRef.push().key ?: return callback(false, "Failed to generate movie ID")

        // Set the movie ID
        movie.MovieId = movieId

        Log.d("MovieRepository", "Adding movie: ${movie.MovieName} with ID: $movieId")

        moviesRef.child(movieId).setValue(movie)
            .addOnSuccessListener {
                Log.d("MovieRepository", "Movie added successfully: ${movie.MovieName}")
                callback(true, "Movie added successfully")
            }
            .addOnFailureListener { exception ->
                Log.e("MovieRepository", "Failed to add movie: ${exception.message}")
                callback(false, "Failed to add movie: ${exception.message}")
            }
    }

    // Get all movies
    fun getAllMovie(callback: (List<MovieModel>?, Boolean, String) -> Unit) {
        Log.d("MovieRepository", "Fetching all movies from Firebase...")

        moviesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val movieList = mutableListOf<MovieModel>()

                Log.d("MovieRepository", "Firebase snapshot exists: ${snapshot.exists()}")
                Log.d("MovieRepository", "Number of children: ${snapshot.childrenCount}")

                for (movieSnapshot in snapshot.children) {
                    try {
                        val movie = movieSnapshot.getValue(MovieModel::class.java)
                        if (movie != null) {
                            // Ensure the movie has an ID
                            if (movie.MovieId.isEmpty()) {
                                movie.MovieId = movieSnapshot.key ?: ""
                            }
                            movieList.add(movie)
                            Log.d("MovieRepository", "Loaded movie: ${movie.MovieName}")
                        }
                    } catch (e: Exception) {
                        Log.e("MovieRepository", "Error parsing movie data: ${e.message}")
                    }
                }

                Log.d("MovieRepository", "Total movies loaded: ${movieList.size}")
                callback(movieList, true, "Movies loaded successfully")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("MovieRepository", "Failed to load movies: ${error.message}")
                callback(null, false, "Failed to load movies: ${error.message}")
            }
        })
    }

    // Update a movie
    fun updateMovie(movie: MovieModel, callback: (Boolean, String) -> Unit) {
        if (movie.MovieId.isEmpty()) {
            callback(false, "Movie ID is required for update")
            return
        }

        Log.d("MovieRepository", "Updating movie: ${movie.MovieName}")

        moviesRef.child(movie.MovieId).setValue(movie)
            .addOnSuccessListener {
                Log.d("MovieRepository", "Movie updated successfully")
                callback(true, "Movie updated successfully")
            }
            .addOnFailureListener { exception ->
                Log.e("MovieRepository", "Failed to update movie: ${exception.message}")
                callback(false, "Failed to update movie: ${exception.message}")
            }
    }

    // Delete a movie
    fun deleteMovie(movieId: String, callback: (Boolean, String) -> Unit) {
        if (movieId.isEmpty()) {
            callback(false, "Movie ID is required for deletion")
            return
        }

        Log.d("MovieRepository", "Deleting movie with ID: $movieId")

        moviesRef.child(movieId).removeValue()
            .addOnSuccessListener {
                Log.d("MovieRepository", "Movie deleted successfully")
                callback(true, "Movie deleted successfully")
            }
            .addOnFailureListener { exception ->
                Log.e("MovieRepository", "Failed to delete movie: ${exception.message}")
                callback(false, "Failed to delete movie: ${exception.message}")
            }
    }

    // Get a single movie by ID
    fun getMovieById(movieId: String, callback: (MovieModel?, Boolean, String) -> Unit) {
        Log.d("MovieRepository", "Fetching movie with ID: $movieId")

        moviesRef.child(movieId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    val movie = snapshot.getValue(MovieModel::class.java)
                    if (movie != null) {
                        movie.MovieId = snapshot.key ?: movieId
                        Log.d("MovieRepository", "Movie found: ${movie.MovieName}")
                        callback(movie, true, "Movie found")
                    } else {
                        Log.d("MovieRepository", "Movie not found")
                        callback(null, false, "Movie not found")
                    }
                } catch (e: Exception) {
                    Log.e("MovieRepository", "Error parsing movie: ${e.message}")
                    callback(null, false, "Error parsing movie: ${e.message}")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("MovieRepository", "Failed to fetch movie: ${error.message}")
                callback(null, false, "Failed to fetch movie: ${error.message}")
            }
        })
    }
}