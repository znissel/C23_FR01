package id.fishku.consumer.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

class MarketItem(
    @field:SerializedName("id_market")
    val marketID: Int,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("location")
    val location: String? = null,

    @field:SerializedName("photo_url")
    val photoUrl: String? = null,
)