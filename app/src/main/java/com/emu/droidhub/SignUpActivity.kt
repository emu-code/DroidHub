package com.emu.droidhub

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.emu.droidhub.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_sign_up)
        auth = FirebaseAuth.getInstance()
        binding.signUpButton.setOnClickListener {
            userSignUp()

        }
    }
    private fun userSignUp() {
            val name = binding.name.editText?.text.toString()
            val email = binding.email.editText?.text.toString()
            val password = binding.password.editText?.text.toString()
        if (name.isEmpty()) {
            binding.name.error = "Please enter your full name"
            binding.name.requestFocus()
            return
        }
        if (email.isEmpty() && !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.email.error = "Please enter Valid Email"
            binding.email.requestFocus()
            return
        }
        if (password.isEmpty()) {
            binding.password.error = "Please enter password"
            binding.password.requestFocus()
            return
        }
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser

                    user!!.sendEmailVerification()
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                startActivity(Intent(this,LoginActivity::class.java))
                                finish()
                            }
                        }

//                    Sign up success, update UI with signed in user function

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(ContentValues.TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Sign up failed. Try again",
                        Toast.LENGTH_SHORT).show()

                }

                // ...
            }
    }
}