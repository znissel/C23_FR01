package id.fishku.consumer.core.utils

enum class FishFilterType {
    PRICE_DESC,
    PRICE_ASC,
    LOCATION,
    BEST_SELLER,
    RELEASE_TIME
}

//TODO ubah di viewmodel, mungkin gini:
/*
class SearchViewModel(private val fishRepository: FishRepository) : ViewModel() {

    private val _filter = MutableLiveData<FishFilterType>()

    val fish: LiveData<PagedList<Fish/FishEntities>> = _filter.switchMap {
        remoteDataSource.searchFishes(it).first())
    }

    init {
        _filter.value = FishFilterType.START_TIME //default filter, ganti, atau ga usah
    }

    fun filter(filterType: FishFilterType) {
        _filter.value = filterType
    }

}
*/

//TODO: di setLocationActivity, data lokasi kan tersimpan,
// gimana caranya supaya lokasi itu bisa diakses buat query