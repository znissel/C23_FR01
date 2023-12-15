package id.fishku.consumer.core.domain.model

data class Market(
    val marketID: Int,
    val name: String,
    val location: String = "",
    val photoUrl: String = "",
)