package id.fishku.consumer.core.data.source.local.room

import androidx.room.*
import id.fishku.consumer.core.data.source.local.entity.FishEntity
import id.fishku.consumer.core.data.source.local.entity.MarketEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FishDao {
    @Query("SELECT * FROM fish")
    fun getAllFish(): Flow<List<FishEntity>>

    /*TAMBAHAN TODO*/
    /*@Query("SELECT * FROM market")
    fun getAllMarket(): Flow<List<MarketEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllMarket(fishes: List<MarketEntity>)*/

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllFish(fishes: List<FishEntity>)
}