package uz.qmgroup.logiadmin.features.transports.new_edit

sealed class TransportEditScreenState {
    object Default: TransportEditScreenState()

    object SavePending: TransportEditScreenState()

    object SaveCompleted: TransportEditScreenState()

    class SaveFailed(val error: String): TransportEditScreenState()
}