package com.example.krupalcollege

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.Window
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth

class SettingActivity : AppCompatActivity() {
    //main setting line add on om
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_setting)

        auth = FirebaseAuth.getInstance() // ✅ Initialize Firebase Auth
        sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigationView.selectedItemId = R.id.bottom_settings

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_home -> {
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
                    finish()
                    true
                }
                R.id.bottom_shopping -> {
                    startActivity(Intent(applicationContext, BuyActivity::class.java))
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
                    finish()
                    true
                }
                R.id.bottom_settings -> true
                R.id.bottom_profile -> {
                    startActivity(Intent(applicationContext, AccountActivity::class.java))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    finish()
                    true
                }
                else -> false
            }
        }

        //start the other code

        val switchNotifications = findViewById<Switch>(R.id.switch_notifications)
        val switchDarkMode = findViewById<Switch>(R.id.switch_dark_mode)
        val switchSoundEffects = findViewById<Switch>(R.id.switch_sound_effects)
        val switchLocationAccess = findViewById<Switch>(R.id.switch_location_access)
        val spinnerLanguage = findViewById<Spinner>(R.id.spinner_language)
        val spinnerCurrency = findViewById<Spinner>(R.id.spinner_currency)
        val buttonEditProfile = findViewById<Button>(R.id.button_edit_profile)
        val buttonChangePassword = findViewById<Button>(R.id.button_change_password)
        val buttonManagePayments = findViewById<Button>(R.id.button_manage_payment_methods)
        val buttonPrivacyPolicy = findViewById<Button>(R.id.button_privacy_policy)
        val buttonLogout = findViewById<Button>(R.id.button_logout)

        // Load saved preferences
        switchNotifications.isChecked = sharedPreferences.getBoolean("notifications", true)
        switchDarkMode.isChecked = sharedPreferences.getBoolean("dark_mode", false)
        switchSoundEffects.isChecked = sharedPreferences.getBoolean("sound_effects", false)
        switchLocationAccess.isChecked = sharedPreferences.getBoolean("location_access", false)

        // Handle switch changes
        switchNotifications.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean("notifications", isChecked).apply()
        }

        switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean("dark_mode", isChecked).apply()
        }

        switchSoundEffects.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean("sound_effects", isChecked).apply()
        }

        switchLocationAccess.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean("location_access", isChecked).apply()
        }

        // Spinner setup
        val languages = arrayOf("English", "Spanish", "French", "German")
        val languageAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, languages)
        languageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerLanguage.adapter = languageAdapter

        val currencies = arrayOf("USD", "EUR", "GBP", "INR")
        val currencyAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, currencies)
        currencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCurrency.adapter = currencyAdapter

        spinnerLanguage.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: android.view.View, position: Int, id: Long) {
                sharedPreferences.edit().putString("language", languages[position]).apply()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        spinnerCurrency.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: android.view.View, position: Int, id: Long) {
                sharedPreferences.edit().putString("currency", currencies[position]).apply()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        buttonEditProfile.setOnClickListener {
            Toast.makeText(this, "Edit Profile clicked", Toast.LENGTH_SHORT).show()
        }

        buttonChangePassword.setOnClickListener {
            showChangePasswordDialog()
//            Toast.makeText(this, "Change Password clicked", Toast.LENGTH_SHORT).show()
        }

        buttonManagePayments.setOnClickListener {
            showImageDialog()
//            Toast.makeText(this, "Manage Payment Methods clicked", Toast.LENGTH_SHORT).show()
        }

        buttonPrivacyPolicy.setOnClickListener {
            startActivity(Intent(applicationContext,PrivacyPolicyActivity::class.java))
//            Toast.makeText(this, "Privacy Policy clicked", Toast.LENGTH_SHORT).show()
        }

        buttonPrivacyPolicy.setOnLongClickListener {
            showPasswordDialog()
            true
        }

        buttonLogout.setOnClickListener {
            showLogoutConfirmation()
//            Toast.makeText(this, "Logout ", Toast.LENGTH_SHORT).show()
        }

    }

    private fun showPasswordDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Enter Password")

        // Create an input field
        val input = EditText(this)
        input.hint = "Password"
        builder.setView(input)

        builder.setPositiveButton("Verify") { dialog, _ ->
            val enteredPassword = input.text.toString().trim()
            val correctPassword = "12345"  // 🔑 Change this to your secure password

            if (enteredPassword == correctPassword) {
                // Open new activity if password is correct
                startActivity(Intent(this, Add_cake_Activity::class.java))
            } else {
                Toast.makeText(this, "Incorrect Password!", Toast.LENGTH_SHORT).show()
            }
            dialog.dismiss()
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }

    private fun showLogoutConfirmation() {
        val alertDialog = AlertDialog.Builder(this)
            .setTitle("Logout")
            .setMessage("Are you sure you want to log out?")
            .setPositiveButton("Yes") { _, _ ->
                logoutUser() // ✅ Properly log out the user
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .setCancelable(false)
            .create()

        alertDialog.show()
    }

    private fun logoutUser() {
        auth.signOut() // ✅ Firebase logout

        // ✅ Clear login status in SharedPreferences
        val editor = sharedPreferences.edit()
        editor.putBoolean("isLoggedIn", false)
        editor.apply()

        // ✅ Redirect to LoginActivity & prevent returning to SettingsActivity
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun showChangePasswordDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_change_password, null)
        val currentPassword = dialogView.findViewById<EditText>(R.id.current_password)
        val newPassword = dialogView.findViewById<EditText>(R.id.new_password)
        val confirmNewPassword = dialogView.findViewById<EditText>(R.id.confirm_new_password)

        val dialog = AlertDialog.Builder(this)
            .setTitle("Change Password")
            .setView(dialogView)
            .setPositiveButton("Change", null) // Set later to prevent auto-dismiss
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            .create()

        dialog.setOnShowListener {
            val button = dialog.getButton(DialogInterface.BUTTON_POSITIVE)
            button.setOnClickListener {
                val currentPass = currentPassword.text.toString().trim()
                val newPass = newPassword.text.toString().trim()
                val confirmPass = confirmNewPassword.text.toString().trim()

                if (TextUtils.isEmpty(currentPass) || TextUtils.isEmpty(newPass) || TextUtils.isEmpty(confirmPass)) {
                    Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (newPass != confirmPass) {
                    Toast.makeText(this, "New passwords do not match!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (newPass.length < 6) {
                    Toast.makeText(this, "Password must be at least 6 characters!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                updatePassword(currentPass, newPass, dialog)
            }
        }

        dialog.show()
    }

    private fun updatePassword(currentPass: String, newPass: String, dialog: AlertDialog) {
        val user = auth.currentUser
        if (user != null && user.email != null) {
            val credential = EmailAuthProvider.getCredential(user.email!!, currentPass)

            // 🔹 Show Progress Dialog
            val progressDialog = ProgressDialog(this)
            progressDialog.setMessage("Updating Password...")
            progressDialog.setCancelable(false)
            progressDialog.show()

            // Re-authenticate the user before changing password
            user.reauthenticate(credential).addOnCompleteListener { task ->
                progressDialog.dismiss()  // 🔹 Hide Progress Dialog

                if (task.isSuccessful) {
                    user.updatePassword(newPass).addOnCompleteListener { updateTask ->
                        if (updateTask.isSuccessful) {
                            Toast.makeText(this, "Password changed successfully!", Toast.LENGTH_SHORT).show()
                            dialog.dismiss()
                        } else {
                            Toast.makeText(this, "Failed: ${updateTask.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Re-authentication failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showImageDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_image)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val imageView = dialog.findViewById<ImageView>(R.id.dialog_image)
        imageView.setImageResource(R.drawable.omqr) // Replace with your actual image resource

        dialog.show()
    }

}