package id.fishku.consumer.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.fishku.consumer.core.domain.usecase.AuthUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val authUseCase: AuthUseCase) : ViewModel() {

    fun getFirstLaunch() = authUseCase.getFirstLaunch().asLiveData()
}