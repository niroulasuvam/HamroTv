package com.example.hamrotv.ui.activity

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.hamrotv.R
import com.example.hamrotv.ViewModel.MovieViewModel
import com.example.hamrotv.databinding.ActivityAddMovieBinding
import com.example.hamrotv.model.MovieModel
import com.example.hamrotv.repository.MovieRepositoryImp
import com.example.hamrotv.utils.ImageUtlis
import com.example.hamrotv.utils.LoadingUtlis
import com.squareup.picasso.Picasso

class AddMovieActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddMovieBinding
    private lateinit var movieViewModel: MovieViewModel
    private lateinit var loadingUtils: LoadingUtlis
    private lateinit var imageUtils: ImageUtlis
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityAddMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        imageUtils = ImageUtlis(this)
        loadingUtils = LoadingUtlis(this)
        val repo = MovieRepositoryImp()
        movieViewModel = MovieViewModel(repo)

        setupImageUpload()
        setupAddProductButton()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupImageUpload() {
        imageUtils.registerActivity { url ->
            url?.let {
                imageUri = it
                Picasso.get().load(it).into(binding.imageBrowse)
            }
        }

        binding.imageBrowse.setOnClickListener {
            imageUtils.launchGallery(this)
        }
    }

    private fun setupAddProductButton() {
        binding.btnAddProduct.setOnClickListener {
            uploadImage()
        }
    }

    private fun uploadImage() {
        loadingUtils.show()
        imageUri?.let { uri ->
            movieViewModel.uploadImage(this, uri) { imageUrl ->
                Log.d("checkpoints", imageUrl.toString())
                if (imageUrl != null) {
                    addProduct(imageUrl)
                } else {
                    Log.e("Upload Error", "Failed to upload image to Cloudinary")
                    loadingUtils.dismiss()
                    Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show()
                }
            }
        } ?: run {
            loadingUtils.dismiss()
            Toast.makeText(this, "Please select an image first", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addProduct(url: String) {
        val productName = binding.editProductName.text.toString()
        val productPrice = binding.editProductPrice.text.toString().toFloatOrNull() ?: 0.0f
        val productDesc = binding.editProductDesc.text.toString()
        val trailerUrl = binding.editTrailerUrl.text.toString()
        val  duration  = binding.editMovieDuration.text.toString()
        val ageRating = binding.editAgeRating.text.toString()
        val releaseyear = binding.editMovieYear.text.toString()

        if (productName.isNotBlank() && productPrice > 0 && productDesc.isNotBlank()) {
            val model = MovieModel( // Fixed spacing typo
                MovieId = "", // Assuming ID is generated elsewhere
                MovieName = productName,
                description = productDesc,
                Rating = productPrice,
                trailerUrl = trailerUrl,
                duration = duration,
                ageRating = ageRating,
                releaseYear = releaseyear,
                imageUrl = url
            )

            movieViewModel.addMovie(model) { success, message ->
                if (success) {
                    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                    finish()
                } else {
                    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                }
                loadingUtils.dismiss()
            }
        } else {
            Toast.makeText(this, "Please fill all fields correctly", Toast.LENGTH_LONG).show()
            loadingUtils.dismiss()
        }
    }
    }
