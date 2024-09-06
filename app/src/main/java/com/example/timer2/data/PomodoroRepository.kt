package com.example.timer2.data

import kotlinx.coroutines.flow.Flow

class PomodoroRepository(private val pomodoroSessionDao: PomodoroSessionDao) {

    val allSessions: Flow<List<PomodoroSession>> = pomodoroSessionDao.getAllSessions()

    suspend fun insertSession(session: PomodoroSession) {
        pomodoroSessionDao.insertSession(session)
    }

    suspend fun updateSession(session: PomodoroSession) {
        pomodoroSessionDao.updateSession(session)
    }

    suspend fun deleteSession(session: PomodoroSession) {
        pomodoroSessionDao.deleteSession(session)
    }
}
