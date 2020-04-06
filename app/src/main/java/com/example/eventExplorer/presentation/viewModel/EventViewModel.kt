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

    var checkin = Checkin("1", "John Doe", "john.doe@gmail.com")

    var eventId: Int = 1

    private val _requestInProgress = MutableLiveData<Boolean>()
    val requestInProgress: LiveData<Boolean>
        get() = _requestInProgress

    private val _event = MutableLiveData<ResponseResult<Event>>()
    val event: LiveData<ResponseResult<Event>>
        get() = _event

    private val _eventListResult = MutableLiveData<ResponseResult<List<Event>>>()
    val eventListResult: LiveData<ResponseResult<List<Event>>>
        get() = _eventListResult

    var eventList : ArrayList<Event> = ArrayList()

    fun loadEvent(eventId: Int) {
        viewModelScope.launch {
            try {
                _requestInProgress.postValue(true)
                _event.postValue(getEvent(eventId))
                this@EventViewModel.eventId++
            } catch (e: Exception) {
                _event.postValue(ResponseResult.Failure("ExceptionMessage: ${e.message}"))
                e.printStackTrace()
            } finally {
                _requestInProgress.postValue(false)
            }
        }
    }

    fun loadEventList() {
        viewModelScope.launch {
            try {
                _requestInProgress.postValue(true)
                getEventList().apply {
                    if(this is ResponseResult.Success) {
                        eventList = ArrayList(data)
                    }
                    _eventListResult.postValue(this)
                }
            } catch (e: Exception) {
                _event.postValue(ResponseResult.Failure("ExceptionMessage: ${e.message}"))
                e.printStackTrace()
            } finally {
                _requestInProgress.postValue(false)
            }
        }
    }

    fun makeCheckin(checkin: Checkin) {
        viewModelScope.launch {
            try {
                _requestInProgress.postValue(true)
                postCheckin(checkin)
            } catch (e: Exception) {
                _event.postValue(ResponseResult.Failure("ExceptionMessage: ${e.message}"))
                e.printStackTrace()
            } finally {
                _requestInProgress.postValue(false)
            }
        }
    }

}