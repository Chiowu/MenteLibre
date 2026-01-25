package com.example.mentelibre.data.mood

import com.example.mentelibre.data.local.dao.MoodDao
import java.time.LocalDate

class MoodRepository(
    private val moodDao: MoodDao
) {

    // ---------- ÁNIMO DE HOY ----------
    suspend fun getTodayMood(): MoodResult? {
        val today = LocalDate.now().toString()
        val entry = moodDao.getMoodByDate(today) ?: return null

        val type = MoodType.valueOf(entry.mood)

        return MoodResult(
            mood = type,
            percentage = MoodScore.toPercentage(entry.score),
            level = MoodScore.level(entry.score)
        )
    }
    // ---------- PROMEDIO SEMANAL ----------
    suspend fun getWeekAverage(): Int {
        val list = moodDao.getLast7Days()
        if (list.isEmpty()) return 0

        val avg = list.map { it.score }.average()
        return MoodScore.toPercentage(avg.toFloat())
    }

    // ---------- PROMEDIO MENSUAL ----------
    suspend fun getMonthAverage(): Int {
        val list = moodDao.getLast30Days()
        if (list.isEmpty()) return 0

        val avg = list.map { it.score }.average()
        return MoodScore.toPercentage(avg.toFloat())
    }


    // ---------- HISTORIAL COMPLETO ----------
    suspend fun getAllMoods(): List<MoodResult> {
        return moodDao.getAll().map { entry ->
            val type = MoodType.valueOf(entry.mood)

            MoodResult(
                mood = type,
                percentage = MoodScore.toPercentage(entry.score),
                level = MoodScore.level(entry.score)
            )
        }
    }
}
