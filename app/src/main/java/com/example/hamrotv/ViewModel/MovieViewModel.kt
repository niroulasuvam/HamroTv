package com.example.hamrotv.ViewModel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hamrotv.model.MovieModel
import com.example.hamrotv.repository.MovieRepository

class MovieViewModel (val repo : MovieRepository){
    fun addMovie(
        productModel: MovieModel,
        callback: (Boolean, String) -> Unit
    ) {
        repo.addMovie(productModel, callback)
    }

    fun updateMovie(
        productId: String,
        data: MutableMap<String, Any>,
        callback: (Boolean, String) -> Unit  // Change here to accept both Boolean and String
    ) {
        repo.updateMovie(productId, data) { success, message ->
            callback(success, message)  // Pass both success and message to the callback
        }
    }

    fun deleteMovie(
        productId: String,
        callback: (Boolean, String) -> Unit
    ) {
        repo.deleteMovie(productId, callback)
    }

    var _Movies= MutableLiveData<MovieModel?>()
    var Movies = MutableLiveData<MovieModel?>()
        get() = _Movies

    var _allMovie = MutableLiveData<List<MovieModel>?>()
    var allMovie = MutableLiveData<List<MovieModel>?>()
        get() = _allMovie

    var _loading = MutableLiveData<Boolean>()
    var loading = MutableLiveData<Boolean>()
        get() = _loading



    fun getMovieById(
        productId: String
    ) {
        repo.geMoviebyId(productId) { products, success, message ->
            if (success) {
                _Movies.value = products
            }

        }
    }

    // Inside ProductViewModel
    fun getAllMovie() {
        _loading.value = true
        repo.getAllMovie() { products, success, message ->
            if (success) {
                _allMovie.postValue(products ?: emptyList())
            }
            _loading.value = false
        }
    }

    val filteredMovie = MutableLiveData<List<MovieModel>>()

//    fun getWorkoutByCategory(category: String) {
//        repo.getWorkoutByCategory(category) { workouts, success, _ ->
//            if (success) {
//                filteredWorkout.postValue(workouts ?: emptyList())
//            } else {
//                filteredWorkout.postValue(emptyList()) // Ensures RecyclerView clears on failure
//            }
//        }
//    }


    fun uploadImage(context: Context, imageUri: Uri, callback: (String?) -> Unit) {
        if (imageUri == null) {
            callback("Image URI is null")
            return
        }

        // Handle the image upload logic here
        repo.uploadImage(context, imageUri) { url ->
            if (url != null) {
                callback(url)  // Successfully uploaded image
            } else {
                callback("Failed to upload image")
            }
        }
    }

}