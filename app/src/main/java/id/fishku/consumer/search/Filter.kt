package id.fishku.consumer.search

data class Filter(
    val value: String = "",
    val displayText: String = "",
    val filterID: Int,
    val filterName: String,
    val filterType: String,
    val filterValue: String,
)