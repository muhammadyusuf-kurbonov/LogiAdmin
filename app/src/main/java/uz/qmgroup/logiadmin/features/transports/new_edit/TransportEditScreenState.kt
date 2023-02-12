package uz.qmgroup.logiadmin.features.transports.new_edit

import uz.qmgroup.logiadmin.features.transports.models.Transport

sealed class TransportEditScreenState {
    object Default: TransportEditScreenState()

    object SavePending: TransportEditScreenState()

    class SaveCompleted(val transport: Transport): TransportEditScreenState()

    class SaveFailed(val error: String): TransportEditScreenState()
}