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

class SignInFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sign_in, container, false)

        // Link to Register Fragment
        val registerLink = view.findViewById<TextView>(R.id.registerLink)
        registerLink.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, RegisterFragment())
                .addToBackStack(null)
                .commit()
        }

        // Initialize Firebase Auth and Database
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        usernameEditText = view.findViewById(R.id.emailTxt)
        passwordEditText = view.findViewById(R.id.passwordTxt)

        val signInButton = view.findViewById<Button>(R.id.btnSignIn)
        signInButton.setOnClickListener {
            signIn()
        }

        return view
    }

    private fun signIn() {
        val username = usernameEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Sign in with Firebase Authentication using email and password
        auth.signInWithEmailAndPassword("$username@example.com", password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val currentUser = auth.currentUser
                    if (currentUser != null) {
                        // Retrieve the user's information from the database
                        database.child("users").child(currentUser.uid).get()
                            .addOnSuccessListener { snapshot ->
                                val user = snapshot.getValue(User::class.java)
                                if (user != null) {
                                    Toast.makeText(requireContext(), "Sign in successful", Toast.LENGTH_SHORT).show()
                                    navigateToDashboard(user.username)
                                }
                            }
                            .addOnFailureListener {
                                Toast.makeText(requireContext(), "Failed to retrieve user data: ${it.message}", Toast.LENGTH_SHORT).show()
                            }
                    }
                } else {
                    Toast.makeText(requireContext(), "Sign in failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun navigateToDashboard(username: String) {
        val intent = Intent(requireContext(), Dashboard::class.java)
        intent.putExtra("username", username)
        startActivity(intent)
        requireActivity().finish() // Prevents going back to Sign In screen
    }
}
