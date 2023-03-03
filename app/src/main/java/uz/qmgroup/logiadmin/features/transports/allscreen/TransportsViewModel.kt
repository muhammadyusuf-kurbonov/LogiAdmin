package uz.qmgroup.logiadmin.features.transports.allscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import uz.qmgroup.logiadmin.features.transports.remotestore.datasource.TransportsDataSource

class TransportsViewModel(private val dataSource: TransportsDataSource): ViewModel() {
    private val _state = MutableStateFlow<TransportsScreenState>(TransportsScreenState.Loading)
    val state = _state.asStateFlow()

    private var currentQueryListener: Job? = null

    fun search(query: String) {
        currentQueryListener?.cancel()

        currentQueryListener = viewModelScope.launch {
            _state.emit(TransportsScreenState.Loading)

            delay(2000)

            _state.emitAll(dataSource.getTransports(query).map { transports ->
                if (transports.isEmpty())
                    TransportsScreenState.NoData
                else
                    TransportsScreenState.DataFetched(transports)
            })
        }
    }

}