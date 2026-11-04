package sa.factory.mizancounsellegalc.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MizanCounselDao {
    @Query("SELECT * FROM legal_consulting_items_cache ORDER BY id ASC")
    fun observeAllItems(): Flow<List<MizanCounselEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItems(items: List<MizanCounselEntity>)

    @Query("DELETE FROM legal_consulting_items_cache")
    suspend fun clearAll()
}
