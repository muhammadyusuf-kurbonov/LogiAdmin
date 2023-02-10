package uz.qmgroup.logiadmin.features.transports.di

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import uz.qmgroup.logiadmin.features.transports.TransportsViewModel
import uz.qmgroup.logiadmin.features.transports.datasource.TestTransportsDataSource
import uz.qmgroup.logiadmin.features.transports.datasource.TransportsDataSource

val transportsDIModule = module {
    singleOf(::TestTransportsDataSource).bind<TransportsDataSource>()
    viewModelOf(::TransportsViewModel)
}