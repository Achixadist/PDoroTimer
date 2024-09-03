package com.example.timer2.timer

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface TimerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTimer(timer: TimerEntity)

    @Update
    suspend fun updateTimer(timer: TimerEntity)

    @Query("SELECT * FROM timer WHERE id = :id")
    suspend fun getTimerById(id: Int): TimerEntity?

    @Query("DELETE FROM timer WHERE id = :id")
    suspend fun deleteTimerById(id: Int)
}
