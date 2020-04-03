package com.example.eventExplorer.domain.usecase

import com.example.eventExplorer.domain.repository.EventRepository

class GetEvent(private val eventRepository: EventRepository) {
    suspend operator fun invoke(eventId: Int) = eventRepository.getEvent(eventId)
}