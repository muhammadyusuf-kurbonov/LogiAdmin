package uz.qmgroup.logiadmin.features.transports.allscreen

import uz.qmgroup.logiadmin.features.transports.models.Transport

sealed class TransportsScreenState {
    object Loading: TransportsScreenState()

    data class DataFetched(val list: List<Transport>): TransportsScreenState()

    object NoData: TransportsScreenState()
}