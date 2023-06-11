package uz.qmgroup.logiadmin.features.repositories

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.binds
import org.koin.dsl.module
import uz.qmgroup.logiadmin.features.repositories.local.AppDatabase
import uz.qmgroup.logiadmin.features.shipments.datasource.ShipmentDataSource
import uz.qmgroup.logiadmin.features.transports.TransportsDataSource

val repositoriesDI = module {
    single {
        Firebase.firestore
    }
    single {
        AppDatabase.getInstance(get())
    }
    singleOf(::AppRepository).binds(arrayOf(TransportsDataSource::class, ShipmentDataSource::class))
}