package com.example.matuleapp

import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.graphics.Rect
import com.example.matuleapp.Data.com.example.matuleapp.Domain.ProductsRepository
import com.example.matuleapp.Presentation.ui.adapters.ProductsAdapter
import com.example.matuleapp.databinding.ActivityMainPageBinding
import kotlinx.coroutines.launch

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
        setupButtons()
        loadProducts()
    }

    private fun setupButtons() {
        val btnoutdoor = findViewById<Button>(R.id.outdoor_btn)
        btnoutdoor.setOnClickListener {
            startActivity(Intent(this, Catalog::class.java))
        }

        val btnsaved = findViewById<ImageButton>(R.id.btnsaved)
        btnsaved.setOnClickListener {
            startActivity(Intent(this, SavedActivity::class.java))
        }
    }

    private fun setupProductsGrid() {
        binding.rvProducts.layoutManager = GridLayoutManager(this, 2)
        binding.rvProducts.adapter = adapter
        binding.rvProducts.setHasFixedSize(true)

        val spacingPx = dpToPx(12f)
        binding.rvProducts.addItemDecoration(
            GridSpacingItemDecoration(spanCount = 2, spacing = spacingPx)
        )
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

            // чтобы не липло и выглядело ровно
            outRect.top = spacing
            outRect.bottom = spacing
        }
    }
}
