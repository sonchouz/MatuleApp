package com.example.matuleapp

import android.os.Bundle
import android.util.Patterns
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.matuleapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
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
    private fun validPswd(): String?
    {
        val pswdtext = binding.pswdtxt.text.toString()
        if (pswdtext.length < 8){
            return "Пароль должен быть не менее 8 символов"
        }
        if (!pswdtext.matches(".*[A-Z].*".toRegex())){
            return "Пароль должен содержать хотя бы 1 заглавную букву"
        }
        if (!pswdtext.matches(".*[a-z].*".toRegex())){
            return "Пароль должен содержать хотя бы 1 строчную букву"
        }
        if (!pswdtext.matches(".*[@\$#&^?!+=].*".toRegex())){
            return "Пароль должен содержать хотя бы 1 спецсимвол"
        }
        return null
    }
}