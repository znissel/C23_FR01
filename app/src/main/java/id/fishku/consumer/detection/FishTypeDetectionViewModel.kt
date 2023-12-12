package id.fishku.consumer.detection

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.fishku.consumer.model.FishTypeDetection
import id.fishku.consumer.model.FishTypeDetectionData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FishTypeDetectionViewModel : ViewModel() {

    private val _fishList = MutableLiveData<List<FishTypeDetection>>()
    val fishList: LiveData<List<FishTypeDetection>> get() = _fishList

    // Call this function to fetch the fish data
    fun fetchFishData() {
        GlobalScope.launch(Dispatchers.IO) {
            // Simulate fetching data from a remote source or database
            val fishData = FishTypeDetectionData.fishDetection

            withContext(Dispatchers.Main) {
                _fishList.value = fishData
            }
        }
    }
}