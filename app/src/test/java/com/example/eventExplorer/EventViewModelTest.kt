package com.example.eventExplorer

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.eventExplorer.domain.model.Cupons
import com.example.eventExplorer.domain.model.Event
import com.example.eventExplorer.domain.model.People
import com.example.eventExplorer.domain.model.ResponseResult
import com.example.eventExplorer.domain.usecase.GetEvent
import com.example.eventExplorer.domain.usecase.GetEventList
import com.example.eventExplorer.domain.usecase.PostCheckin
import com.example.eventExplorer.presentation.viewModel.EventViewModel
import io.mockk.*
import io.mockk.impl.annotations.MockK
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class EventViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @MockK
    val getEventUseCase = mockk<GetEvent>()
    @MockK
    val getEventListUseCase = mockk<GetEventList>()
    @MockK
    val postCheckinUseCase = mockk<PostCheckin>()

    @MockK(relaxed = true)
    lateinit var eventObserver: Observer<ResponseResult<Event>>
    @MockK(relaxed = true)
    lateinit var eventListObserver: Observer<ResponseResult<List<Event>>>

    private lateinit var viewModel: EventViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = EventViewModel(getEventUseCase, getEventListUseCase, postCheckinUseCase)
    }

    @Test
    fun `should get event`() {
        val expectedResponseResult = ResponseResult.Success(event)
        coEvery { getEventUseCase.invoke(any()) } returns expectedResponseResult
        viewModel.event.observeForever(eventObserver)
        viewModel.loadEvent()
        assert(viewModel.event.value == expectedResponseResult)
        verify { eventObserver.onChanged(expectedResponseResult) }
    }

    @Test
    fun `should get event list`() {
        val expectedResponseResultList = ResponseResult.Success(listOf(event, event, event))
        coEvery { getEventListUseCase.invoke() } returns expectedResponseResultList
        viewModel.eventListResult.observeForever(eventListObserver)
        viewModel.loadEventList()
        assert(viewModel.eventListResult.value == expectedResponseResultList)
        verifyOrder {
            viewModel.requestInProgress.value == true
            eventListObserver.onChanged(expectedResponseResultList)
            viewModel.requestInProgress.value == false
        }
    }

    companion object {
        val event = Event(
            arrayListOf(People("1", "1", "name 1", "pictute 1")),
            1534784400000,
            "O Patas Dadas estará na Redenção",
            "",
            -51.2146267,
            -30.0392981,
            29.99f,
            "Feira de adoção de animais na Redenção",
            "1",
            arrayListOf(Cupons("1","1",62))
        )
    }

}