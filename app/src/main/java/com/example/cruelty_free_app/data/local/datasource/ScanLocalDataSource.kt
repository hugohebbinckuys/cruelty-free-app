package com.example.cruelty_free_app.data.local.datasource

import android.content.Context
import com.example.cruelty_free_app.domain.model.ScanEntry
import org.json.JSONArray
import org.json.JSONObject

class ScanLocalDataSource(private val context: Context) {

    private val prefs get() = context.getSharedPreferences("scan_history", Context.MODE_PRIVATE)

    fun getAll(): List<ScanEntry> {
        val array = JSONArray(prefs.getString("entries", "[]") ?: "[]")
        return (0 until array.length()).map { i ->
            val obj = array.getJSONObject(i)
            ScanEntry(
                barcode = obj.getString("barcode"),
                scannedAt = obj.getLong("scannedAt"),
                title = obj.optString("title").takeIf { it.isNotEmpty() },
                imageUrl = obj.optString("imageUrl").takeIf { it.isNotEmpty() }
            )
        }
    }

    private fun ScanEntry.toJson() = JSONObject().apply {
        put("barcode", barcode)
        put("scannedAt", scannedAt)
        title?.let { put("title", it) }
        imageUrl?.let { put("imageUrl", it) }
    }

    fun save(entry: ScanEntry) {
        val array = JSONArray()
        array.put(entry.toJson())
        getAll().forEach { array.put(it.toJson()) }
        prefs.edit().putString("entries", array.toString()).apply()
    }

    fun delete(entry: ScanEntry) {
        val array = JSONArray()
        getAll().filter { it.scannedAt != entry.scannedAt }.forEach { array.put(it.toJson()) }
        prefs.edit().putString("entries", array.toString()).apply()
    }
}