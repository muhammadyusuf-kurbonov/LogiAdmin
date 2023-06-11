package uz.qmgroup.logiadmin.features.transports.di

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import uz.qmgroup.logiadmin.features.transports.allscreen.TransportsViewModel
import uz.qmgroup.logiadmin.features.transports.new_edit.TransportEditViewModel

val transportsDIModule = module {
    viewModelOf(::TransportsViewModel)
    viewModelOf(::TransportEditViewModel)
}