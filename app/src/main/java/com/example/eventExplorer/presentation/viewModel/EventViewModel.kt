package com.example.eventExplorer.presentation.viewModel

import androidx.lifecycle.*
import com.example.eventExplorer.domain.model.Checkin
import com.example.eventExplorer.domain.model.Event
import com.example.eventExplorer.domain.model.ResponseResult
import com.example.eventExplorer.domain.usecase.GetEvent
import com.example.eventExplorer.domain.usecase.GetEventList
import com.example.eventExplorer.domain.usecase.PostCheckin
import kotlinx.coroutines.launch
import java.lang.Exception

class EventViewModel(
    private val getEvent: GetEvent,
    private val getEventList: GetEventList,
    private val postCheckin: PostCheckin
) : ViewModel() {

    private var eventId: Int = 1

    private val _requestInProgress = MutableLiveData<Boolean>()
    val requestInProgress: LiveData<Boolean>
        get() = _requestInProgress

    private val _eventListResult = MutableLiveData<ResponseResult<List<Event>>>()
    val eventListResult: LiveData<ResponseResult<List<Event>>>
        get() = _eventListResult

    private val _event = MutableLiveData<ResponseResult<Event>>()
    val event: LiveData<ResponseResult<Event>>
        get() = _event

    var eventList : ArrayList<Event> = ArrayList()

    fun loadEventList() {
        viewModelScope.launch {
            try {
                _requestInProgress.postValue(true)
                getEventList().apply {
                    if(this is ResponseResult.Success) {
                        eventList = ArrayList(data)
                        eventId = (eventList.lastOrNull()?.id?.toIntOrNull() ?: 0) + 1
                    }
                    _eventListResult.postValue(this)
                }
            } catch (e: Exception) {
                _eventListResult.postValue(ResponseResult.Failure("ExceptionMessage: ${e.message}"))
                e.printStackTrace()
            } finally {
                _requestInProgress.postValue(false)
            }
        }
    }

    fun loadEvent() {
        viewModelScope.launch {
            try {
                _requestInProgress.postValue(true)
                getEvent(eventId).apply {
                    if (this is ResponseResult.Success) {
                        eventList.add(data)
                        eventId++
                    }
                    _event.postValue(this)
                }
            } catch (e: Exception) {
                _event.postValue(ResponseResult.Failure("ExceptionMessage: ${e.message}"))
                e.printStackTrace()
            } finally {
                _requestInProgress.postValue(false)
            }
        }
    }

    fun makeCheckin(position: String) {
        val checkin = Checkin(position, "John Doe", "john.doe@gmail.com")
        viewModelScope.launch {
            try {
                postCheckin(checkin)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}