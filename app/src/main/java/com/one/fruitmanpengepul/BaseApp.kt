package com.one.fruitmanpengepul

import android.app.Application
import com.one.fruitmanpengepul.ui.order_in_progress_activity.OrderInViewModel
import com.one.fruitmanpengepul.repositories.OrderRepository
import com.one.fruitmanpengepul.repositories.ProductRepository
import com.one.fruitmanpengepul.repositories.UserRepository
import com.one.fruitmanpengepul.ui.buyer_in_progress_activity.BuyerInProgressViewModel
import com.one.fruitmanpengepul.ui.register_activity.RegisterViewModel
import com.one.fruitmanpengepul.ui.seller_in_progress_activity.SellerInProgressViewModel
import com.one.fruitmanpengepul.ui.waiting_activity.WaitingOrderViewModel
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
    viewModel { RegisterViewModel(get()) }
    viewModel { ProductViewModel(get()) }
    viewModel { UserViewModel(get()) }
    viewModel { OrderInViewModel(get()) }
    viewModel { WaitingOrderViewModel(get()) }
    viewModel { SellerInProgressViewModel(get()) }
    viewModel { BuyerInProgressViewModel(get()) }
    viewModel { OrderViewModel(get()) }
}

val retrofitModule = module {
    single { ApiClient.instance() }
}

