package com.example.krupalcollege

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegistrationActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val name = findViewById<EditText>(R.id.name)
        val email = findViewById<EditText>(R.id.email)
        val password = findViewById<EditText>(R.id.password)
        val confirmpassword = findViewById<EditText>(R.id.confirm_password)
        val phone = findViewById<EditText>(R.id.phone)
        val address = findViewById<EditText>(R.id.address)
        val registerButton = findViewById<Button>(R.id.register_button)
        val regprocessbar = findViewById<ProgressBar>(R.id.regprocess)

        registerButton.setOnClickListener {
            val nameText = name.text.toString().trim()
            val emailText = email.text.toString().trim()
            val passwordText = password.text.toString().trim()
            val confirmpassText = confirmpassword.text.toString().trim()
            val phoneText = phone.text.toString().trim()
            val addressText = address.text.toString().trim()

            if (TextUtils.isEmpty(emailText) || TextUtils.isEmpty(passwordText) || TextUtils.isEmpty(confirmpassText) ||
                TextUtils.isEmpty(nameText) || TextUtils.isEmpty(phoneText) || TextUtils.isEmpty(addressText) || passwordText.equals(confirmpassText) ) {
                Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            auth.createUserWithEmailAndPassword(emailText, passwordText)
                .addOnCompleteListener { task ->
                    regprocessbar.visibility = View.VISIBLE
                    if (task.isSuccessful) {
                        val userId = auth.currentUser?.uid ?: return@addOnCompleteListener
                        val user = hashMapOf(
                            "name" to nameText,
                            "email" to emailText,
                            "phone" to phoneText,
                            "address" to addressText
                        )

                        db.collection("users").document(userId).set(user)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Registration Successful!", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this, LoginActivity::class.java))
                                finish()
                                regprocessbar.visibility = View.GONE
                            }
                            .addOnFailureListener {
                                Toast.makeText(this, "Failed to save user details!", Toast.LENGTH_SHORT).show()
                                regprocessbar.visibility = View.GONE
                            }
                    } else {
                        Toast.makeText(this, "Registration Failed!", Toast.LENGTH_SHORT).show()
                        regprocessbar.visibility = View.GONE
                    }
                }
        }
    }
}
