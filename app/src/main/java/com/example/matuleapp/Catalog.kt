package com.example.matuleapp

import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Catalog : AppCompatActivity() {
    private lateinit var savedIb: ImageButton
    private var isSaved = false
    private val PRODUCT_ID = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.catalog)
        savedIb = findViewById(R.id.ibSaved)
        savedIb.setOnClickListener{
            if(!isSaved){
                savedIb.setImageResource(R.drawable.icon3)
                isSaved = true

        }else{
                savedIb.setImageResource(R.drawable.icon)
                isSaved = false
        }
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

}