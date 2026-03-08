package com.example.cruelty_free_app.domain.repository

import com.example.cruelty_free_app.domain.model.ScanEntry

interface ScanRepository {
    fun getAll(): List<ScanEntry>
    fun save(entry: ScanEntry)
    fun delete(entry: ScanEntry)
}