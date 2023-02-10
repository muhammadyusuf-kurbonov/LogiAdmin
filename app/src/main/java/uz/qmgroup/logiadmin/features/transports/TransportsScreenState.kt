package uz.qmgroup.logiadmin.features.transports

import uz.qmgroup.logiadmin.features.transports.models.Transport

sealed class TransportsScreenState {
    object Loading: TransportsScreenState()

    data class DataFetched(val list: List<Transport>): TransportsScreenState()

    object NoData: TransportsScreenState()
}