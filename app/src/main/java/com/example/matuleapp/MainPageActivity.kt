package com.example.matuleapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.matuleapp.Data.com.example.matuleapp.Domain.ProductsRepository
import com.example.matuleapp.R.id.main
import com.example.matuleapp.databinding.ActivityMainPageBinding

class MainPageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainPageBinding
    private val repository = ProductsRepository()
    private val adapter = ProductsAdapter(
        bucketName = "product-images"
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupProductsGrid()
        loadProducts()

        val btnoutdoor = findViewById<Button>(R.id.outdoor_btn)
        btnoutdoor.setOnClickListener {
            val intent = Intent(this, Catalog::class.java)
            startActivity(intent)
        }
        val btnsaved = findViewById<ImageButton>(R.id.btnsaved)
        btnsaved.setOnClickListener {
            val intent = Intent(this, SavedActivity::class.java)
            startActivity(intent)
        }
        private fun setupProductsGrid() {
            // 2 колонки
            binding.rvProducts.layoutManager = GridLayoutManager(this, 2)
            binding.rvProducts.adapter = adapter

            // чуть оптимизации, если карточки одинаковой высоты/структуры
            binding.rvProducts.setHasFixedSize(true)

            // чтобы не было "вплотную" и "криво"
            val spacingPx = dpToPx(12f)
            binding.rvProducts.addItemDecoration(GridSpacingItemDecoration(spanCount = 2, spacing = spacingPx))
        }

        private fun loadProducts() {
            android.util.Log.d("PRODUCTS", "loadProducts() called")
            lifecycleScope.launch {
                try {
                    val products = repository.fetchProducts()
                    android.util.Log.d("PRODUCTS", "loaded = ${products.size}")
                    adapter.submit(products)
                } catch (e: Exception) {
                    android.util.Log.e("PRODUCTS", "error", e)
                    Toast.makeText(
                        this@MainPageActivity,
                        e.message ?: "Ошибка загрузки товаров",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        private fun dpToPx(dp: Float): Int {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                resources.displayMetrics
            ).toInt()
        }

        class GridSpacingItemDecoration(
            private val spanCount: Int,
            private val spacing: Int
        ) : RecyclerView.ItemDecoration() {

            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                val position = parent.getChildAdapterPosition(view)
                if (position == RecyclerView.NO_POSITION) return

                val column = position % spanCount

                outRect.left = spacing - column * spacing / spanCount
                outRect.right = (column + 1) * spacing / spanCount

                // сверху тоже делаем отступ, чтобы не липло к заголовку
                outRect.top = spacing
                outRect.bottom = spacing
            }
        }

    }
}