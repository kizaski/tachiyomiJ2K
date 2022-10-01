package eu.kanade.tachiyomi.ui.reader.translator

import androidx.room.Dao
import androidx.room.Query

@Dao
interface EntryOptimizedDao {
    @Query("SELECT * FROM entryoptimized WHERE kanji LIKE :kanji")
    fun findByName(kanji: String): List<EntryOptimized>
}
