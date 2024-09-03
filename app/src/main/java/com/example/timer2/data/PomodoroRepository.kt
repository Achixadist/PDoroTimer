package com.example.timer2.data

import kotlinx.coroutines.flow.Flow

class PomodoroRepository(private val dao: PomodoroSessionDao) {

    val allSessions: Flow<List<PomodoroSession>> = dao.getAllSessions()

    suspend fun insertSession(session: PomodoroSession) {
        dao.insertSession(session)
    }

    suspend fun updateSession(session: PomodoroSession) {
        dao.updateSession(session)
    }

    suspend fun deleteSession(session: PomodoroSession) {
        dao.deleteSession(session)
    }
}
