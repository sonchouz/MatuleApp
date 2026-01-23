package com.example.matuleapp.Domain

import android.content.Context

object FavoritesStore {
    private const val PREFS = "favorites"
    private const val KEY = "ids"
    fun getIds(context: Context): Set<String> {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        return prefs.getStringSet(KEY, emptySet()) ?: emptySet()
    }
    fun isFavorite(context: Context, id: Int): Boolean {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val set = prefs.getStringSet(KEY, emptySet()) ?: emptySet()
        return set.contains(id.toString())
    }

    fun toggle(context: Context, id: Int): Boolean {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val set = (prefs.getStringSet(KEY, emptySet()) ?: emptySet()).toMutableSet()

        val key = id.toString()
        val nowFav = if (set.contains(key)) {
            set.remove(key)
            false
        } else {
            set.add(key)
            true
        }

        prefs.edit().putStringSet(KEY, set).apply()
        return nowFav
    }
}
