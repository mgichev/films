package com.test_task.main.authorization

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.test_task.R
import com.test_task.databinding.ActivityAuthorizationBinding
import com.test_task.main.films.FilmsActivity

class AuthorizationActivity : AppCompatActivity() {

    private lateinit var authorizationService: AuthorizationService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityAuthorizationBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        authorizationService = AuthorizationService(UserPreferences(this))
        initAuthBtn(binding)
    }

    private fun initAuthBtn(binding: ActivityAuthorizationBinding) {
        binding.authBtn.setOnClickListener {
            auth(binding)
        }
    }

    private fun auth(binding: ActivityAuthorizationBinding) {
        val authResult = authorizationService.tryAuth(binding.loginTv.text.toString(),
            binding.passwordTv.text.toString())

        if (!authResult) {
            Toast.makeText(this,
                resources.getString(R.string.incorrect_password_or_invalid_data),
                Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this,
                resources.getString(R.string.successful_auth),
                Toast.LENGTH_LONG).show()

            val intent = Intent(this, FilmsActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onRestart() {
        super.onRestart()
        authorizationService.setDefaultAccount()
    }
}
