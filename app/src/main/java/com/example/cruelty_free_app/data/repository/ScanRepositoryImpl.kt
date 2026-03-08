package com.example.cruelty_free_app.data.repository

import com.example.cruelty_free_app.data.local.datasource.ScanLocalDataSource
import com.example.cruelty_free_app.domain.model.ScanEntry
import com.example.cruelty_free_app.domain.repository.ScanRepository

class ScanRepositoryImpl(
    private val dataSource: ScanLocalDataSource
) : ScanRepository {
    override fun getAll(): List<ScanEntry> = dataSource.getAll()
    override fun save(entry: ScanEntry) = dataSource.save(entry)
    override fun delete(entry: ScanEntry) = dataSource.delete(entry)
}