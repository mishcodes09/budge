package com.example.prog3c_budgeapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignInFragment : Fragment() {

    private lateinit var database: DatabaseReference
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sign_in, container, false)

        usernameEditText = view.findViewById(R.id.emailTxt)
        passwordEditText = view.findViewById(R.id.passwordTxt)

        val signInButton = view.findViewById<Button>(R.id.btnSignIn)
        signInButton.setOnClickListener {
            signIn()
        }

        // Initialize Firebase Database reference
        database = FirebaseDatabase.getInstance().reference

        return view
    }

    private fun signIn() {
        val username = usernameEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Query the database for the user by username
        database.child("users").orderByChild("username").equalTo(username)
            .get().addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    // User exists, now check password
                    var loginSuccessful = false
                    for (userSnapshot in snapshot.children) {
                        val user = userSnapshot.getValue(User::class.java)
                        if (user != null && user.password == password) {
                            loginSuccessful = true
                            navigateToHome()
                            break
                        }
                    }

                    if (!loginSuccessful) {
                        Toast.makeText(requireContext(), "Incorrect password", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "User not found", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
                Toast.makeText(requireContext(), "Sign in failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun navigateToHome() {
        Toast.makeText(requireContext(), "Sign in successful", Toast.LENGTH_SHORT).show()
        val homeFragment = HomeFragment()
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, homeFragment)
            .addToBackStack(null)
            .commit()
    }
}