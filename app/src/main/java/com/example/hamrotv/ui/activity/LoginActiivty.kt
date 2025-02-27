package com.example.hamrotv.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.hamrotv.R
import com.example.hamrotv.ViewModel.UserViewModel
import com.example.hamrotv.databinding.ActivityLoginBinding
import com.example.hamrotv.repository.UserRepositoryImp
import com.example.hamrotv.utils.LoadingUtlis

class LoginActiivty : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var loadingUtils: LoadingUtlis

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repo = UserRepositoryImp()
        userViewModel = UserViewModel(repo)

        // Initializing loading
        loadingUtils = LoadingUtlis(this)

        binding.btnLogin.setOnClickListener {
            loadingUtils.show()
            val email: String = binding.etEmail.text.toString()
            val password: String = binding.etPassword.text.toString()

            userViewModel.login(email, password) { success, message ->
                if (success) {
                    Toast.makeText(this@LoginActiivty, message, Toast.LENGTH_LONG).show()
                    loadingUtils.dismiss()
                    val intent = Intent(this@LoginActiivty, NavigationActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
                    loadingUtils.dismiss()
                }
            }
        }

        binding.tvSignUpLink.setOnClickListener {
            val intent = Intent(this@LoginActiivty, RegisterActivity::class.java)
            startActivity(intent)
        }

//        binding.btnForget.setOnClickListener {
//            val intent = Intent(this@LoginActiivty, ForgetPasswordActivity::class.java)
//            startActivity(intent)
//        }

        // Use binding.root instead of findViewById
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}