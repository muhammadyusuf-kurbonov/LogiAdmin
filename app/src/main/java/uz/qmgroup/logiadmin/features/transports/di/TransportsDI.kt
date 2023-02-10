package uz.qmgroup.logiadmin.features.transports.di

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import uz.qmgroup.logiadmin.features.transports.TransportsViewModel
import uz.qmgroup.logiadmin.features.transports.datasource.FirebaseTransportDataSource
import uz.qmgroup.logiadmin.features.transports.datasource.TransportsDataSource
import uz.qmgroup.logiadmin.features.transports.new_edit.TransportEditViewModel

val transportsDIModule = module {
    single {
        Firebase.firestore
    }
    singleOf(::FirebaseTransportDataSource).bind<TransportsDataSource>()
    viewModelOf(::TransportsViewModel)
    viewModelOf(::TransportEditViewModel)
}