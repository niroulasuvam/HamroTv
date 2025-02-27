package com.example.hamrotv.repository

import android.content.Context
import android.database.Cursor
import android.graphics.Movie
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.OpenableColumns
import android.util.Log
import com.cloudinary.Cloudinary
import com.cloudinary.utils.ObjectUtils
import com.example.hamrotv.model.MovieModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.io.InputStream
import java.util.concurrent.Executors

class MovieRepositoryImp: MovieRepository {

    val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    val ref: DatabaseReference = database.reference
        .child("exercises")
    override fun addMovie(MovieModel: MovieModel, callback: (Boolean, String) -> Unit) {
        var id = ref.push().key.toString()
        MovieModel.MovieId = id
        Log.d("Firebase", "Adding exercise: $MovieModel")

        ref.child(id).setValue(MovieModel).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("Firebase", "Exercise added successfully")
                callback(true, "Exercise added successfully")
            } else {
                Log.e("Firebase", "Failed to add exercise", task.exception)
                callback(false, task.exception?.message ?: "Unknown error")
            }
        }
    }

    override fun updateMovie(
        MovieId: String,
        data: MutableMap<String, Any>,
        callback: (Boolean, String) -> Unit
    ) {
        ref.child(MovieId).updateChildren(data).addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Product updated successfully")
            } else {
                callback(false, "${it.exception?.message}")

            }
        }
    }
    override fun deleteMovie(MovieId: String, callback: (Boolean, String) -> Unit) {
        ref.child(MovieId).removeValue().addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Product deleted successfully")
            } else {
                callback(false, "${it.exception?.message}")
            }
        }
    }

    override fun getMovieFromDatabase(
        MovieId: String,
        callback: (List<MovieModel>?, Boolean, String) -> Unit
    ) {
        ref.orderByChild("productName").equalTo(MovieId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val exercises = mutableListOf<MovieModel>()
                    for (data in snapshot.children) {
                        val exercise = data.getValue(MovieModel::class.java)
                        if (exercise != null) {
                            Log.d("checkpoint","i am here")
                            Log.d("checkpoint",exercise.MovieName)
                            exercises.add(exercise)
                        }
                    }
                    if (exercises.isNotEmpty()) {
                        callback(exercises, true, "Exercises fetched successfully")
                    } else {
                        callback(emptyList(), true, "No exercises found for the given productId")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    callback(null, false, error.message)
                }
            })
    }

    override fun geMoviebyId(MovieId: String, callback: (MovieModel?, Boolean, String) -> Unit) {
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    var Movie = mutableListOf<MovieModel>()
                    for (eachData in snapshot.children) {
                        var model = eachData.getValue(MovieModel::class.java)
                        if (model != null) {
                            Movie.add(model)
                        }
                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null, false, error.message)
            }
        })
    }
    override fun getAllMovie(callback: (List<MovieModel>?, Boolean, String) -> Unit) {
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    var products = mutableListOf<MovieModel>()
                    for (eachData in snapshot.children) {
                        var model = eachData.getValue(MovieModel::class.java)
                        if (model != null) {
                            products.add(model)
                        }
                    }

                    callback(products, true, "Product fetched")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null, false, error.message)
            }
        })
    }


    private val cloudinary = Cloudinary(
        mapOf(
            "cloud_name" to "dkscpr3wa",
            "api_key" to "776537619471962",
            "api_secret" to "S_YN8k3Ne5Vlnc96hsQ5bAnOPik"
        )
    )
    override fun uploadImage(context: Context, imageUri: Uri, callback: (String?) -> Unit) {
        val executor = Executors.newSingleThreadExecutor()
        executor.execute {
            try {
                val inputStream: InputStream? = context.contentResolver.openInputStream(imageUri)
                var fileName = getFileNameFromUri(context, imageUri)

                fileName = fileName?.substringBeforeLast(".") ?: "uploaded_image"

                val response = cloudinary.uploader().upload(
                    inputStream, ObjectUtils.asMap(
                        "public_id", fileName,
                        "resource_type", "image"
                    )
                )

                var imageUrl = response["url"] as String?

                imageUrl = imageUrl?.replace("http://", "https://")

                Handler(Looper.getMainLooper()).post {
                    callback(imageUrl)
                }

            } catch (e: Exception) {
                e.printStackTrace()
                Handler(Looper.getMainLooper()).post {
                    callback(null)
                }
            }
        }
    }

    override fun getFileNameFromUri(context: Context, uri: Uri): String? {
        var fileName: String? = null
        val cursor: Cursor? = context.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (nameIndex != -1) {
                    fileName = it.getString(nameIndex)
                }
            }
        }
        return fileName
    }
}