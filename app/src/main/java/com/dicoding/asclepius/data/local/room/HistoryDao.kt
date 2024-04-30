package com.dicoding.asclepius.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dicoding.asclepius.data.local.entity.HistoryEntity

@Dao
interface HistoryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(his: HistoryEntity)
    @Delete
    fun delete(his: HistoryEntity)
    @Query("SELECT * from prediction_history")
    fun getAllHistory(): LiveData<List<HistoryEntity>>
    @Query("SELECT EXISTS(SELECT 1 FROM prediction_history WHERE id = :username)")
    fun getHistoryById(username: String): LiveData<Boolean>
}