package com.example.eventExplorer.di

import com.example.eventExplorer.data.network.RetrofitProvider.Companion.provideEventApi
import com.example.eventExplorer.data.network.RetrofitProvider.Companion.provideEventService
import com.example.eventExplorer.data.network.RetrofitProvider.Companion.provideHttpClient
import com.example.eventExplorer.data.network.RetrofitProvider.Companion.provideLoginInterceptor
import com.example.eventExplorer.data.repository.EventRepositoryImpl
import com.example.eventExplorer.domain.repository.EventRepository
import com.example.eventExplorer.presentation.viewModel.EventViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val dataModule = module {
    single { provideLoginInterceptor() }
    single { provideHttpClient(get()) }
    single { provideEventApi(get()) }
    single { provideEventService(get()) }
    single<EventRepository> { EventRepositoryImpl(get()) }
    viewModel { EventViewModel(get(), get()) }
}