package com.example.timer2.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PomodoroSessionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: PomodoroSession)

    @Update
    suspend fun updateSession(session: PomodoroSession)

    @Delete
    suspend fun deleteSession(session: PomodoroSession)

    @Query("SELECT * FROM pomodoro_sessions ORDER BY id DESC")
    fun getAllSessions(): Flow<List<PomodoroSession>>
}
