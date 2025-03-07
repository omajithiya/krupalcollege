package com.example.krupalcollege

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PrivacyPolicyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_privacy_policy)

        val textViewPrivacy = findViewById<TextView>(R.id.textViewPrivacy)

        val privacyPolicyText = """
            PRIVACY POLICY FOR CAKE SHOP APP
            Effective Date: 11/10/2003
            Last Updated: 06/03/2025

            1. Introduction
            Welcome to the Cake Shop App! Your privacy is important to us, and we are committed to protecting your personal information. This Privacy Policy outlines how we collect, use, store, and protect your data when you use our mobile application.
            
            By using our app, you agree to the collection and use of information in accordance with this policy.
            
            2. Information We Collect
            When you use the Cake Shop App, we may collect different types of information, including:
            
            a) Personal Information (Provided by You)
            We collect personal details when you:
            ✅ Register an account (Name, Email, Phone Number, Date of Birth, etc.)
            ✅ Login using email/password
            ✅ Update your profile information
            ✅ Contact customer support
            
            b) Non-Personal Information (Collected Automatically)
            When you use the app, we may collect:
            ✅ Device Information: Model, OS version, unique device ID
            ✅ Usage Data: Features used, pages visited, time spent on the app
            ✅ IP Address & Location Data: Used to improve app services (if location access is enabled)
            
            c) Information from Third-Party Services
            If you log in via Google/Facebook or make online payments, we may receive data from these platforms as per their privacy settings.
            
            3. How We Use Your Information
            We use your information to:
            ✅ Provide and improve app functionality (e.g., personalize your experience)
            ✅ Secure user accounts and prevent unauthorized access
            ✅ Send notifications about updates, events, or alerts
            ✅ Analyze user behavior to enhance app performance
            ✅ Ensure compliance with legal and regulatory requirements
            
            4. Data Sharing & Security
            a) Do We Share Your Data?
            🔹 We do not sell your data to third parties.
            🔹 We may share limited data with:
            
            Service Providers (e.g., payment processors)
            Legal Authorities (if required by law)
            b) How We Protect Your Data
            🔐 Encryption: We use industry-standard encryption to protect your data.
            🔐 Secure Storage: Your personal information is stored in secure databases.
            🔐 Limited Access: Only authorized personnel can access your data.
            
            5. Your Rights & Choices
            ✅ Access & Update: You can edit your profile and personal details anytime.
            ✅ Delete Account: You can request account deletion via settings or customer support.
            ✅ Opt-out of Notifications: You can manage push notifications in app settings.
            
            6. Third-Party Links & Services
            Our app may contain links to external websites (e.g., payment gateways, college websites). We are not responsible for their privacy practices, so we recommend reviewing their policies.
            
            7. Changes to This Policy
            We may update this Privacy Policy periodically. Any changes will be notified within the app or via email.
            
            8. Contact Us
            If you have any questions or concerns about our Privacy Policy, please contact us at:
            
            📧 Email: omajethiya1@gmail.com 
            📞 Phone: +91 94846 85503
            🏢 Address: Indus University, Ahmedabad

            Thank you for using Cake Shop App!
        """.trimIndent()

        textViewPrivacy.text = privacyPolicyText
    }

}