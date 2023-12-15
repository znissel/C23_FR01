package id.fishku.consumer.core.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "market")
data class MarketEntity(
    @PrimaryKey
    @ColumnInfo(name = "idMarket")
    var idMarket: Int,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "location")
    var location: String,

    @ColumnInfo(name = "photo_url")
    var photoUrl: String,
)