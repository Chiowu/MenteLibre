package com.example.mentelibre.data.mood

import androidx.compose.ui.graphics.Color
import com.example.mentelibre.data.local.dao.MoodDao
import com.example.mentelibre.data.local.entity.MoodEntryEntity
import com.example.mentelibre.ui.mood.MoodChartPoint
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.TextStyle
import java.util.Locale

class MoodRepository(
    private val moodDao: MoodDao
) {

    // ---------------- GUARDAR ÁNIMO ----------------
    suspend fun saveMood(moodType: MoodType) {
        moodDao.insert(
            MoodEntryEntity(
                dateTime = LocalDateTime.now().toString(),
                mood = moodType.name,
                score = moodType.score
            )
        )
    }

    // ---------------- ÁNIMO DE HOY ----------------
    suspend fun getTodayMood(): MoodResult? {
        val entry = moodDao.getTodayLastMood() ?: return null
        val type = MoodType.valueOf(entry.mood)

        return MoodResult(
            mood = type,
            percentage = MoodScore.toPercentage(entry.score),
            level = MoodScore.level(entry.score)
        )
    }

    // ---------------- GRÁFICO 7 DÍAS ----------------
    suspend fun getLast7DaysChart(): List<MoodChartPoint> {
        return buildDailyChart(moodDao.getLast7Days())
    }

    // ---------------- GRÁFICO 30 DÍAS ----------------
    suspend fun getLast30DaysChart(): List<MoodChartPoint> {
        return buildDailyChart(moodDao.getLast30Days())
    }

    // ---------------- PROMEDIOS ----------------
    suspend fun getWeekAverage(): Int {
        val list = moodDao.getLast7Days()
        if (list.isEmpty()) return 0
        return MoodScore.toPercentage(list.map { it.score }.average().toFloat())
    }

    suspend fun getMonthAverage(): Int {
        val list = moodDao.getLast30Days()
        if (list.isEmpty()) return 0
        return MoodScore.toPercentage(list.map { it.score }.average().toFloat())
    }

    // ---------------- HISTORIAL ----------------
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

    // ---------------- HELPERS ----------------
    private fun buildDailyChart(
        entries: List<MoodEntryEntity>
    ): List<MoodChartPoint> {

        // Agrupar por día y tomar el último ánimo de cada día
        val groupedByDay = entries
            .groupBy {
                LocalDate.parse(it.dateTime.substring(0, 10))
            }
            .mapValues { (_, values) ->
                values.maxBy { it.dateTime }
            }

        return groupedByDay.values.map { entry ->
            val date = LocalDate.parse(entry.dateTime.substring(0, 10))
            val label = date.dayOfWeek.getDisplayName(
                TextStyle.SHORT,
                Locale("es", "ES")
            )

            val normalized = entry.score.coerceIn(0f, 1f)

            val color = when {
                normalized >= 0.6f -> Color(0xFFA6E7A6)
                normalized >= 0.4f -> Color.LightGray
                else -> Color(0xFFFFB3B3)
            }

            MoodChartPoint(
                label = label,
                value = normalized,
                color = color
            )
        }.sortedBy { it.label }
    }
}
