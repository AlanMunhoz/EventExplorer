package com.example.eventExplorer.data.network

import com.example.eventExplorer.domain.model.Checkin
import com.example.eventExplorer.domain.model.Event
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface EventService {

    @GET("events")
    suspend fun getEventsResponse(): Response<List<Event>>

    @GET("events/{id}")
    suspend fun getEventResponse(@Path(value = "id") eventId: Int): Response<Event>

    @POST("checkin")
    suspend fun postCheckin(@Body checkin: Checkin)

}