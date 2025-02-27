package com.example.hamrotv.repository

import android.content.Context
import android.net.Uri
import com.example.hamrotv.model.MovieModel

interface MovieRepository {
    fun addMovie(
        MovieModel: MovieModel,
        callback: (Boolean, String) -> Unit
    )

    fun updateMovie(
        MovieId: String,
        data: MutableMap<String, Any>,
        callback: (Boolean, String) -> Unit
    )

    fun deleteMovie(
        MovieId: String,
        callback: (Boolean, String) -> Unit
    )

    fun getMovieFromDatabase(
        MovieId: String,
        callback: (List<MovieModel>?, Boolean, String) -> Unit
    )


    fun geMoviebyId(
        MovieId: String,
        callback: (MovieModel?, Boolean, String) -> Unit
    )
    fun getAllMovie(
        callback:
            (List<MovieModel>?, Boolean, String) -> Unit
    )
    fun uploadImage(context: Context, imageUri: Uri, callback: (String?) -> Unit)

    fun getFileNameFromUri(context: Context, uri: Uri): String?
}

