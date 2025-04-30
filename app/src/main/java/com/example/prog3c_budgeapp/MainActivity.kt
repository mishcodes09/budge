package com.example.prog3c_budgeapp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.prog3c_budgeapp.databinding.ActivityMainBinding
import com.google.firebase.FirebaseApp

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase
        FirebaseApp.initializeApp(this)

        // Set up button click listeners for the home screen
        binding.signinBtn.setOnClickListener {
            showFragment(SignInFragment())
        }

        binding.registerBtn.setOnClickListener {
            showFragment(RegisterFragment())
        }
    }

    // This function switches the fragments and handles visibility
    fun showFragment(fragment: Fragment) {
        // Hide the logo and buttons
        binding.imageView2.visibility = View.GONE
        binding.signinBtn.visibility = View.GONE
        binding.registerBtn.visibility = View.GONE

        // Show the fragment container
        binding.fragmentContainer.visibility = View.VISIBLE

        // Load the fragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }

    // This function handles the back button press
    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
            // Check if we're returning to the main screen (no fragments visible)
            if (supportFragmentManager.backStackEntryCount == 1) {
                showHomeScreen()
            }
        } else {
            super.onBackPressed()
        }
    }

    // Function to show the home screen (logo and buttons)
    fun showHomeScreen() {
        binding.imageView2.visibility = View.VISIBLE
        binding.signinBtn.visibility = View.VISIBLE
        binding.registerBtn.visibility = View.VISIBLE
        binding.fragmentContainer.visibility = View.GONE
    }
}