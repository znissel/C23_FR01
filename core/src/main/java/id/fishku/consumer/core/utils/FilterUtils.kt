package id.fishku.consumer.core.utils

import androidx.sqlite.db.SimpleSQLiteQuery

object FilterUtils {
    fun getFilteredFish(filterType: FishFilterType, location: String?): SimpleSQLiteQuery {
        val simpleQuery = StringBuilder().append("SELECT * FROM fish ")
        when (filterType) {
            FishFilterType.PRICE_DESC -> {
                simpleQuery.append("ORDER BY price DESC") //TODO dibuat kolom price?
            }
            FishFilterType.PRICE_ASC -> {
                simpleQuery.append("ORDER BY price ASC") //TODO dibuat kolom price?
            }
            FishFilterType.LOCATION -> {
                simpleQuery.append("ORDER BY ABS(location - :location) ASC")
            }
            FishFilterType.BEST_SELLER -> {
                simpleQuery.append("ORDER BY sold_total DESC") //TODO tambahin kolom jumlah penjualan?
            }
            FishFilterType.RELEASE_TIME -> {
                simpleQuery.append("ORDER BY release_date DESC") //TODO tambahin kolom tanggal?
            }
        }
        return SimpleSQLiteQuery(simpleQuery.toString())
    }
}