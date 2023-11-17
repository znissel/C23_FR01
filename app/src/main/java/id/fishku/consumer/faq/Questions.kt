package id.fishku.consumer.faq

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Questions(
    val question: String,
    val answer: String,
    var expandable: Boolean = false
) : Parcelable {
}
