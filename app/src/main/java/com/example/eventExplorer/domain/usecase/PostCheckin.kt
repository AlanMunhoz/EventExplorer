package com.example.eventExplorer.domain.usecase

import com.example.eventExplorer.domain.model.Checkin
import com.example.eventExplorer.domain.repository.EventRepository

class PostCheckin(private val eventRepository: EventRepository) {
    suspend operator fun invoke(checkin: Checkin) = eventRepository.postCheckin(checkin)
}