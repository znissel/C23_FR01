package id.fishku.consumer.fishprice

import androidx.lifecycle.ViewModel
import id.fishku.consumer.model.FishPricePrediction
import id.fishku.consumer.model.FishPricePredictionData

class FishPriceViewModel : ViewModel() {
    private val fishPrices: List<FishPricePrediction> = FishPricePredictionData.price

    fun getFishPrices(): List<FishPricePrediction> {
        return fishPrices
    }
}