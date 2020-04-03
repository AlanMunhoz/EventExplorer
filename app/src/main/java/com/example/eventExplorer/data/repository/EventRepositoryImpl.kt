package com.example.eventExplorer.data.repository

import com.example.eventExplorer.data.network.EventService
import com.example.eventExplorer.data.util.responseResult
import com.example.eventExplorer.domain.model.Checkin
import com.example.eventExplorer.domain.model.Event
import com.example.eventExplorer.domain.model.ResponseResult
import com.example.eventExplorer.domain.repository.EventRepository

class EventRepositoryImpl(
    private val retrofitService: EventService
) : EventRepository {

    companion object {
        val LOG_TAG = EventRepositoryImpl::class.java.name.split(".").lastOrNull()?.toString()
    }

    override suspend fun getEvent(eventId: Int): ResponseResult<Event> {
        return retrofitService.getEventResponse(eventId).responseResult()
    }

    override suspend fun postCheckin(checkin: Checkin) {
        retrofitService.postCheckin(checkin)
    }
}

