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
            ScanEntry(obj.getString("barcode"), obj.getLong("scannedAt"))
        }
    }

    fun save(entry: ScanEntry) {
        val array = JSONArray()
        array.put(JSONObject().apply {
            put("barcode", entry.barcode)
            put("scannedAt", entry.scannedAt)
        })
        getAll().forEach { e ->
            array.put(JSONObject().apply {
                put("barcode", e.barcode)
                put("scannedAt", e.scannedAt)
            })
        }
        prefs.edit().putString("entries", array.toString()).apply()
    }

    fun delete(entry: ScanEntry) {
        val filtered = getAll().filter { it.scannedAt != entry.scannedAt }
        val array = JSONArray()
        filtered.forEach { e ->
            array.put(JSONObject().apply {
                put("barcode", e.barcode)
                put("scannedAt", e.scannedAt)
            })
        }
        prefs.edit().putString("entries", array.toString()).apply()
    }
}