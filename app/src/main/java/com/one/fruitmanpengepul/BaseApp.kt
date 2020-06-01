package com.one.fruitmanpengepul

import android.app.Application
import android.util.Log.DEBUG
import com.one.fruitmanpengepul.repositories.OrderRepository
import com.one.fruitmanpengepul.repositories.ProductRepository
import com.one.fruitmanpengepul.repositories.UserRepository
import com.one.fruitmanpengepul.viewmodels.OrderViewModel
import com.one.fruitmanpengepul.viewmodels.ProductViewModel
import com.one.fruitmanpengepul.viewmodels.UserViewModel
import com.one.fruitmanpengepul.webservices.ApiClient
import org.koin.android.ext.koin.androidLogger
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module

class BaseApp : Application(){
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@BaseApp)
            modules(listOf(retrofitModule, viewModelModules, repositoryModule))
        }
    }
}

val repositoryModule = module {
    factory { ProductRepository(get()) }
    factory { OrderRepository(get()) }
    factory { UserRepository(get()) }
}

val viewModelModules = module {
    viewModel { ProductViewModel(get()) }
    viewModel { OrderViewModel(get()) }
    viewModel { UserViewModel(get()) }
}

val retrofitModule = module {
    single { ApiClient.instance() }
}

