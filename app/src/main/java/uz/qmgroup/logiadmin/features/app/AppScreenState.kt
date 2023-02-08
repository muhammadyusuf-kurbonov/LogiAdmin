package uz.qmgroup.logiadmin.features.app

sealed class AppScreenState {
    object Shipments: AppScreenState()
    object Transports: AppScreenState()

    operator fun compareTo(other: AppScreenState): Int {
        val screens = listOf<AppScreenState>(
            Shipments,
            Transports,
        )
        return screens.indexOf(this) - screens.indexOf(other)
    }
}