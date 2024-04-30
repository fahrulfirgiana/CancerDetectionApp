package com.dicoding.asclepius.data

import android.app.Application
import androidx.lifecycle.LiveData
import com.dicoding.asclepius.data.local.entity.HistoryEntity
import com.dicoding.asclepius.data.local.room.HistoryDao
import com.dicoding.asclepius.data.local.room.HistoryDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class HistoryRepository(application: Application) {
    private val mHistoryDao: HistoryDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()
    init {
        val db = HistoryDatabase.getInstance(application)
        mHistoryDao = db.hisdao
    }
    fun getAllHistory(): LiveData<List<HistoryEntity>> = mHistoryDao.getAllHistory()
    fun insert(his: HistoryEntity) {
        executorService.execute { mHistoryDao.insert(his) }
    }
    fun delete(his: HistoryEntity) {
        executorService.execute { mHistoryDao.delete(his) }
    }
    fun isFavorite(username: String): LiveData<Boolean> {
        return mHistoryDao.getHistoryById(username)
    }

}