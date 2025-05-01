
package com.example.prog3c_budgeapp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.prog3c_budgeapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signinBtn.setOnClickListener {
            showFragment(SignInFragment())
        }

        binding.registerBtn.setOnClickListener {
            showFragment(RegisterFragment())
        }
    }

    private fun showFragment(fragment: androidx.fragment.app.Fragment) {
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
}
