package com.example.eventExplorer.domain.repository

import com.example.eventExplorer.domain.model.Checkin
import com.example.eventExplorer.domain.model.Event
import com.example.eventExplorer.domain.model.ResponseResult

interface EventRepository {
    suspend fun getEvent(eventId: Int) : ResponseResult<Event>
    suspend fun postCheckin(checkin: Checkin)
}