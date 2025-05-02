package com.example.prog3c_budgeapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        // Link to Sign-In Fragment
        val signInLink = view.findViewById<TextView>(R.id.signInLink)
        signInLink.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, SignInFragment())
                .addToBackStack(null)
                .commit()
        }

        // Initialize Firebase Auth and Database
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        usernameEditText = view.findViewById(R.id.usernameTxt)
        passwordEditText = view.findViewById(R.id.passwordTxt)

        val registerButton = view.findViewById<Button>(R.id.btnRegister)
        registerButton.setOnClickListener {
            registerUser()
        }

        return view
    }

    private fun registerUser() {
        val username = usernameEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Register with Firebase Authentication
        auth.createUserWithEmailAndPassword("$username@example.com", password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // After successful registration, store the username in Firebase Database
                    val userId = auth.currentUser?.uid ?: return@addOnCompleteListener
                    val user = User(userId, username, password)

                    database.child("users").child(userId).setValue(user)
                        .addOnSuccessListener {
                            Toast.makeText(requireContext(), "Registration successful", Toast.LENGTH_SHORT).show()
                            navigateToDashboard(username)
                        }
                        .addOnFailureListener {
                            Toast.makeText(requireContext(), "Failed to save user details: ${it.message}", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(requireContext(), "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun navigateToDashboard(username: String) {
        val intent = Intent(requireContext(), Dashboard::class.java)
        intent.putExtra("username", username)
        startActivity(intent)
        requireActivity().finish()
    }
}
