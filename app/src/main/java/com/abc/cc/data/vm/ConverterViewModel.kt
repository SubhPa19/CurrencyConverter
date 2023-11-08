package com.abc.cc.data.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abc.cc.data.repo.Repository
import com.abc.cc.util.Converter
import com.abc.cc.util.DispatchersProvider
import com.abc.cc.util.Resources
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConverterViewModel @Inject constructor(
    private val repository: Repository, private val dispatchers: DispatchersProvider
) : ViewModel() {

    private val _dataLoaded = MutableStateFlow(false)
    val dataLoaded: StateFlow<Boolean> = _dataLoaded

    private val _exchangeRates = MutableStateFlow<UIEvent>(UIEvent.Empty)
    val exchangeRates: StateFlow<UIEvent> = _exchangeRates

    fun getExchangeRates() {
        viewModelScope.launch(dispatchers.io) {
            _exchangeRates.value = UIEvent.Loading

            val currenciesDeferred = async { repository.getCurrencies() }
            val latestRatesDeferred = async { repository.getLatestRates() }

            val currenciesResult = currenciesDeferred.await()
            val latestRatesResult = latestRatesDeferred.await()

            when (currenciesResult) {
                is Resources.Error -> _exchangeRates.value =
                    UIEvent.Error(currenciesResult.message!!)

                is Resources.Success -> {
                    when (latestRatesResult) {
                        is Resources.Error -> _exchangeRates.value =
                            UIEvent.Error(latestRatesResult.message!!)

                        is Resources.Success -> {
                            val exchangeRateList = Converter().margeAPIResponse(
                                currenciesResult.data!!,
                                latestRatesResult.data
                            )
                            _exchangeRates.value = UIEvent.ExchangeRates(exchangeRateList)
                            _dataLoaded.value = true
                        }
                    }
                }
            }
        }
    }


}

sealed class UIEvent {
    class ExchangeRates(val exchangeRates: List<com.abc.cc.data.model.ExchangeRates>) : UIEvent()
    class Error(val errorText: String) : UIEvent()
    object Loading : UIEvent()
    object Empty : UIEvent()
}