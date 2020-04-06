package com.example.eventExplorer.di

import com.example.eventExplorer.domain.usecase.GetEvent
import com.example.eventExplorer.domain.usecase.GetEventList
import com.example.eventExplorer.domain.usecase.PostCheckin
import org.koin.dsl.module

val domainModule = module {
    factory { GetEvent(get()) }
    factory { GetEventList(get()) }
    factory { PostCheckin(get()) }
}