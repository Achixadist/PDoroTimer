package com.example.timer2.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PomodoroSessionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: PomodoroSession)

    @Update
    suspend fun updateSession(session: PomodoroSession)

    @Delete
    suspend fun deleteSession(session: PomodoroSession)

    @Query("SELECT * FROM pomodoro_session_table")
    fun getAllSessions(): Flow<List<PomodoroSession>>
}
