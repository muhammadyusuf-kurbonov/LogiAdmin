package uz.qmgroup.logiadmin.features.startshipment.di

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import uz.qmgroup.logiadmin.features.shipments.datasource.FirebaseShipmentDataSource
import uz.qmgroup.logiadmin.features.shipments.datasource.ShipmentDataSource
import uz.qmgroup.logiadmin.features.startshipment.StartShipmentViewModel

val startShipmentDIModule = module {
    single {
        Firebase.firestore
    }
    singleOf(::FirebaseShipmentDataSource).bind(ShipmentDataSource::class)
    viewModelOf(::StartShipmentViewModel)
}