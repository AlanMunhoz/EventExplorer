package com.example.eventExplorer.domain.usecase

import com.example.eventExplorer.domain.repository.EventRepository

class GetEventList(private val eventRepository: EventRepository) {
    suspend operator fun invoke() = eventRepository.getEventList()
}