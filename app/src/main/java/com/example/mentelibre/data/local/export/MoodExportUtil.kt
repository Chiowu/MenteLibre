package com.example.mentelibre.data.export

import android.content.Context
import com.example.mentelibre.data.local.AppDatabase
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object MoodExportUtil {

    suspend fun exportToCsv(context: Context): File {
        val db = AppDatabase.getDatabase(context)
        val moods = db.moodDao().getAll()

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        val now = LocalDateTime.now().format(formatter)

        val fileName = "historial_animo_$now.csv"
            .replace(":", "-")

        val file = File(context.filesDir, fileName)

        file.printWriter().use { out ->
            out.println("Fecha,Estado,Score")

            moods.forEach {
                out.println("${it.dateTime},${it.mood},${it.score}")
            }
        }

        return file
    }
}
