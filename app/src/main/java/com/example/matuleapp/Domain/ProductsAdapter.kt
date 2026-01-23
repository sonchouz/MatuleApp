package com.example.matuleapp.Presentation.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.matuleapp.Data.SupabaseProvider
import com.example.matuleapp.Data.com.example.matuleapp.Data.ProductDto
import com.example.matuleapp.databinding.ProductCardBinding
import io.github.jan.supabase.storage.storage

class ProductsAdapter(
    private val bucketName: String = "product-images",
    private val onItemClick: ((ProductDto) -> Unit)? = null,
    private val onFavoriteChanged: ((product: ProductDto, isFavorite: Boolean) -> Unit)? = null
) : RecyclerView.Adapter<ProductsAdapter.VH>() {

    private val items = mutableListOf<ProductDto>()

    fun submit(list: List<ProductDto>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    class VH(val binding: ProductCardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ProductCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]

        with(holder.binding) {
            tvTitle.text = item.title
            tvPrice.text = "₽ ${item.price}"

            tvBadge.isVisible = !item.badge.isNullOrBlank()
            tvBadge.text = item.badge.orEmpty()

            val imageUrl = SupabaseProvider.supabase.storage
                .from(bucketName)
                .publicUrl(item.imageurl)

            Glide.with(ivProduct.context)
                .load(imageUrl)
                .into(ivProduct)

            // ----------- ИЗБРАННОЕ: отрисовка состояния -----------
            renderFavorite(item.isfavorite)

            // ----------- ИЗБРАННОЕ: клик -----------
            btnsaved.setOnClickListener {
                var newState = !item.isfavorite
                item.isfavorite = newState

                renderFavorite(newState)

                // Если надо сообщить Activity/VM, чтобы сохранить/удалить
                onFavoriteChanged?.invoke(item, newState)
            }

            root.setOnClickListener { onItemClick?.invoke(item) }
        }
    }

    private fun ProductCardBinding.renderFavorite(isFav: Boolean) {
        // Тут меняй на свои drawable
        // Например: заполненное сердце vs пустое
        if (isFav) {
            btnsaved.setImageResource(com.example.matuleapp.R.drawable.icon3) // активное сердце
        } else {
            btnsaved.setImageResource(com.example.matuleapp.R.drawable.icon) // пустое/обычное
        }
    }

    override fun getItemCount(): Int = items.size
}
