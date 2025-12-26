package com.example.matuleapp
import android.os.Bundle
import android.util.Patterns
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.matuleapp.databinding.ActivityLoginBinding
import com.example.matuleapp.Presentation.login.LoginViewModel
import androidx.lifecycle.lifecycleScope
import android.widget.Toast
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import android.content.Intent
@Serializable
private data class UserEmailRow(
    val email: String
)

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var vm: LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.loginbtn.setOnClickListener {
            val emailError = validEmail()
            val pswdError = validPswd()

            binding.email.helperText = emailError
            binding.pswd.helperText = pswdError

            if (emailError != null || pswdError != null) return@setOnClickListener

            val email = binding.emailtxt.text.toString().trim()

            lifecycleScope.launch {
                val exists = userExistsByEmail(email)
                if (!exists) {
                    Toast.makeText(
                        this@LoginActivity,
                        "Пользователь не найден. Зарегистрируйтесь.",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@launch
                }

                // Тут уже делай логин (через Supabase Auth или свою логику
                val intent = Intent(this@LoginActivity, MainPageActivity::class.java)
                startActivity(intent)
                finish() // чтобы нельзя было вернуться назад кнопкой Back

            }
        }

        emailFocusListener()
        pswdFocusListener()
    }
    private fun emailFocusListener(){
        binding.emailtxt.setOnFocusChangeListener{_, focused ->
            if (!focused)
            {
                binding.email.helperText = validEmail()
            }
        }
    }
    private fun validEmail(): String?
    {
        val emailtext = binding.emailtxt.text.toString().trim()
        if(emailtext.length == 0)
        {
            return "Email не может быть пустым"
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(emailtext).matches())
        {
            return "Некорректный email"
        }
        return null
    }
    private fun pswdFocusListener(){
        binding.pswdtxt.setOnFocusChangeListener{_, focused ->
            if (!focused)
            {
                binding.pswd.helperText = validPswd()
            }
        }
    }
    private fun validPswd(): String? {
        val pswdtext = binding.pswdtxt.text.toString() // без trim

        if (pswdtext.isEmpty()) return "Пароль не может быть пустым"
        if (pswdtext.length < 8) return "Пароль должен быть не менее 8 символов"
        if (!pswdtext.any { it.isUpperCase() }) return "Пароль должен содержать хотя бы 1 заглавную букву"
        if (!pswdtext.any { it.isLowerCase() }) return "Пароль должен содержать хотя бы 1 строчную букву"
        if (!pswdtext.any { !it.isLetterOrDigit() }) return "Пароль должен содержать хотя бы 1 спецсимвол"

        return null
    }

    private suspend fun userExistsByEmail(email: String): Boolean {
        return try {
            val rows = com.example.matuleapp.Data.SupabaseProvider.supabase
                .postgrest["Users"]
                .select {
                    filter { eq("email", email) }
                    limit(1)
                }
                .decodeList<UserEmailRow>()

            rows.isNotEmpty()
        } catch (e: Exception) {
            false
        }
    }



}