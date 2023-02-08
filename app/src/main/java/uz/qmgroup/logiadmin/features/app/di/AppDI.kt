package uz.qmgroup.logiadmin.features.app.di

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import uz.qmgroup.logiadmin.features.app.AppViewModel

val appDIModule = module {
    viewModelOf(::AppViewModel)
}