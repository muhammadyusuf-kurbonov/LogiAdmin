package uz.qmgroup.logiadmin.features.shipments.di

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import uz.qmgroup.logiadmin.features.shipments.new_edit.ShipmentAddEditViewModel
import uz.qmgroup.logiadmin.features.shipments.ShipmentViewModel
import uz.qmgroup.logiadmin.features.shipments.datasource.FirebaseShipmentDataSource
import uz.qmgroup.logiadmin.features.shipments.datasource.ShipmentDataSource

val shipmentDIModule = module {
    single {
        Firebase.firestore
    }
    singleOf(::FirebaseShipmentDataSource).bind(ShipmentDataSource::class)
    viewModelOf(::ShipmentViewModel)
    viewModelOf(::ShipmentAddEditViewModel)
}