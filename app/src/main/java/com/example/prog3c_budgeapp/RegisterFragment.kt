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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterFragment : Fragment() {

    private lateinit var database: DatabaseReference
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        val signInLink = view.findViewById<TextView>(R.id.signInLink)
        signInLink.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, SignInFragment())
                .addToBackStack(null)
                .commit()
        }

        usernameEditText = view.findViewById(R.id.usernameTxt)
        passwordEditText = view.findViewById(R.id.passwordTxt)

        val registerButton = view.findViewById<Button>(R.id.btnRegister)
        registerButton.setOnClickListener {
            registerUser()
        }

        // Initialize Firebase Database reference
        database = FirebaseDatabase.getInstance().reference

        return view
    }

    private fun registerUser() {
        val username = usernameEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // First check if username already exists
        database.child("users").orderByChild("username").equalTo(username)
            .get().addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    // Username already exists
                    Toast.makeText(requireContext(), "Username already exists", Toast.LENGTH_SHORT).show()
                } else {
                    // Username is available, proceed with registration
                    createNewUser(username, password)
                }
            }.addOnFailureListener {
                Toast.makeText(requireContext(), "Database error: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun createNewUser(username: String, password: String) {
        val userId = database.child("users").push().key ?: return
        val user = User(userId, username, password)

        database.child("users").child(userId).setValue(user).addOnSuccessListener {
            Toast.makeText(requireContext(), "Registration successful", Toast.LENGTH_SHORT).show()
            navigateToDashboard(username)
        }.addOnFailureListener {
            Toast.makeText(requireContext(), "Registration failed: ${it.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigateToDashboard(username: String) {
        val intent = Intent(requireContext(), Dashboard::class.java)
        intent.putExtra("username", username)
        startActivity(intent)
        requireActivity().finish()
    }
}
