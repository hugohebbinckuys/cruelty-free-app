package com.example.cruelty_free_app.data

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

data class ScanEntry(
    val barcode: String,
    val scannedAt: Long = System.currentTimeMillis()
)

object ScanStorage {

    private const val PREFS_NAME = "scan_history"
    private const val KEY_ENTRIES = "entries"

    fun getAll(context: Context): List<ScanEntry> {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val json = prefs.getString(KEY_ENTRIES, "[]") ?: "[]"
        val array = JSONArray(json)
        val list = mutableListOf<ScanEntry>()
        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)
            list.add(ScanEntry(
                barcode = obj.getString("barcode"),
                scannedAt = obj.getLong("scannedAt")
            ))
        }
        return list
    }

    fun save(context: Context, entry: ScanEntry) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val existing = getAll(context)
        val array = JSONArray()
        val newObj = JSONObject().apply {
            put("barcode", entry.barcode)
            put("scannedAt", entry.scannedAt)
        }
        array.put(newObj)
        existing.forEach { e ->
            array.put(JSONObject().apply {
                put("barcode", e.barcode)
                put("scannedAt", e.scannedAt)
            })
        }
        prefs.edit().putString(KEY_ENTRIES, array.toString()).apply()
    }

    fun delete(context: Context, entry: ScanEntry) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val filtered = getAll(context).filter { it.scannedAt != entry.scannedAt }
        val array = JSONArray()
        filtered.forEach { e ->
            array.put(JSONObject().apply {
                put("barcode", e.barcode)
                put("scannedAt", e.scannedAt)
            })
        }
        prefs.edit().putString(KEY_ENTRIES, array.toString()).apply()
    }
}