package com.example.hamrotv

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.hamrotv.ui.theme.HamroTVTheme
import com.example.hamrotv.ui.activity.LoginActiivty
import com.example.hamrotv.ui.activity.NavigationActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()

        setContent {
            HamroTVTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AuthCheckScreen()
                }
            }
        }
    }

    @Composable
    fun AuthCheckScreen() {
        var isLoading by remember { mutableStateOf(true) }

        LaunchedEffect(Unit) {
            // Check if user is already logged in
            val currentUser = firebaseAuth.currentUser

            kotlinx.coroutines.delay(2000) // Show splash for 2 seconds

            if (currentUser != null) {
                // User is logged in, go to main app
                val intent = Intent(this@MainActivity, NavigationActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                // User not logged in, go to login
                val intent = Intent(this@MainActivity, LoginActiivty::class.java)
                startActivity(intent)
                finish()
            }
        }

        // Splash Screen
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "HamroTV",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(16.dp))

                CircularProgressIndicator()

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Loading...",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}