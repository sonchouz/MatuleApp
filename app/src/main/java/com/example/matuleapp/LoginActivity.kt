package com.example.matuleapp

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.matuleapp.Data.SupabaseProvider
import com.example.matuleapp.Data.repository.AuthRepositoryImpl
import com.example.matuleapp.databinding.ActivityLoginBinding
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
data class UserRow(
    val id: Long? = null,
    val email: String,
    val password: String
)

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val authRepo = AuthRepositoryImpl()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // безопаснее, чем findViewById(R.id.main) (id может не существовать)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        emailFocusListener()
        pswdFocusListener()

        binding.loginbtn.setOnClickListener {
            val emailError = validEmail()
            val pswdError = validPswd()

            binding.email.helperText = emailError
            binding.pswd.helperText = pswdError

            if (emailError != null || pswdError != null) return@setOnClickListener

            val email = binding.emailtxt.text.toString().trim()
            val pswd = binding.pswdtxt.text.toString()

            lifecycleScope.launch {
                try {
                    authRepo.signIn(email, pswd)
                    startActivity(Intent(this@LoginActivity, MainPageActivity::class.java))
                    finish()
                } catch (e: Exception) {
                    Toast.makeText(
                        this@LoginActivity,
                        e.message ?: "Ошибка входа",
                        Toast.LENGTH_LONG
                    ).show()
                }


                startActivity(Intent(this@LoginActivity, MainPageActivity::class.java))
                finish()
            }
        }
    }

    private fun emailFocusListener() {
        binding.emailtxt.setOnFocusChangeListener { _, focused ->
            if (!focused) binding.email.helperText = validEmail()
        }
    }

    private fun pswdFocusListener() {
        binding.pswdtxt.setOnFocusChangeListener { _, focused ->
            if (!focused) binding.pswd.helperText = validPswd()
        }
    }

    private fun validEmail(): String? {
        val emailText = binding.emailtxt.text.toString().trim()
        if (emailText.isEmpty()) return "Email не может быть пустым"
        if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) return "Некорректный email"
        return null
    }

    private fun validPswd(): String? {
        val pswdText = binding.pswdtxt.text.toString() // без trim
        if (pswdText.isEmpty()) return "Пароль не может быть пустым"
        if (pswdText.length < 8) return "Пароль должен быть не менее 8 символов"
        if (!pswdText.any { it.isUpperCase() }) return "Пароль должен содержать хотя бы 1 заглавную букву"
        if (!pswdText.any { it.isLowerCase() }) return "Пароль должен содержать хотя бы 1 строчную букву"
        if (!pswdText.any { !it.isLetterOrDigit() }) return "Пароль должен содержать хотя бы 1 спецсимвол"
        return null
    }

    private suspend fun userExists(email: String, password: String): Boolean {
        return try {
            val e = email.trim().lowercase()

            val rows = SupabaseProvider.supabase
                .postgrest["Users"]
                .select {
                    filter {
                        eq("email", e)
                        eq("password", password)
                    }
                    limit(1)
                }
                .decodeList<UserRow>()

            rows.isNotEmpty()
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
