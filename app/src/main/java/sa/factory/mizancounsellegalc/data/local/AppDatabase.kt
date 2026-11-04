package sa.factory.mizancounsellegalc.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [MizanCounselEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun legal_consultingDao(): MizanCounselDao
}
