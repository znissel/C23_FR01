package id.fishku.consumer.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FishNutrition (
    val id: String,
    val fishName: String,
    val photoFishUrl: String,
    val fishProtein: String,
    val fishKalium: String,
    val fishVitB12: String,
    val fishVitB6: String,
    val fishZatBesi: String,
    val fishMagnesium: String,
    val fishFosfor: String,
    val fishKarbohidrat: String,
    val fishLemak: String,
    val fishNatrium: String,
    val fishKalsium: String,
    val nutritionalBenefits1: String,
    val nutritionalBenefits2: String,
    val nutritionalBenefits3: String,
    val nutritionalBenefits4: String
): Parcelable