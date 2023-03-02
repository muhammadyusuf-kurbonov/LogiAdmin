package uz.qmgroup.logiadmin.features.startshipment

sealed class StartShipmentDialogState {
    object AwaitingInput: StartShipmentDialogState()

    object Saving: StartShipmentDialogState()

    object Updated: StartShipmentDialogState()
}