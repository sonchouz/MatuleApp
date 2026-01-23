package com.example.matuleapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.matuleapp.Data.com.example.matuleapp.Domain.ProductsRepository
import com.example.matuleapp.Domain.FavoritesStore
import com.example.matuleapp.Presentation.ui.adapters.ProductsAdapter
import com.example.matuleapp.databinding.ActivitySavedBinding
import kotlinx.coroutines.launch

class SavedActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySavedBinding
    private val repository = ProductsRepository()

    private val adapter = ProductsAdapter(
        bucketName = "product-images",
        onFavoriteChanged = { _, isFav ->
            // если удалили из избранного прямо тут, обновим список
            if (!isFav) loadFavorites()
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_saved)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onResume() {
        super.onResume()
        // чтобы когда вернулась с MainPage, список обновился
        loadFavorites()
    }

    private fun loadFavorites() {
        lifecycleScope.launch {
            try {
                val favIds = FavoritesStore.getIds(this@SavedActivity)
                    .mapNotNull { it.toIntOrNull() }
                    .toSet()

                val all = repository.fetchProducts()

                val onlyFav = all
                    .filter { favIds.contains(it.id) }
                    .onEach { it.isfavorite = true }
                adapter.submit(onlyFav)

            } catch (e: Exception) {
                Toast.makeText(
                    this@SavedActivity,
                    e.message ?: "Ошибка загрузки избранного",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}