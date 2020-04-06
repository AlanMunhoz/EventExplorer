package com.example.eventExplorer.presentation.screen

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.eventExplorer.R
import com.example.eventExplorer.data.util.toEventView
import com.example.eventExplorer.databinding.ActivityMainBinding
import com.example.eventExplorer.domain.model.Checkin
import com.example.eventExplorer.domain.model.Event
import com.example.eventExplorer.domain.model.ResponseResult
import com.example.eventExplorer.presentation.viewModel.EventViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(),
    EventAdapter.ClickCallback,
    SwipeRefreshLayout.OnRefreshListener {

    private lateinit var viewBinding: ActivityMainBinding
    private val viewModel: EventViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewBinding.viewModel = viewModel
        viewBinding.lifecycleOwner = this

        viewModel.eventListResult.observe(this, Observer { responseResult ->
            viewBinding.srSwipeRefresh.isRefreshing = false
            when (responseResult) {
                is ResponseResult.Success -> showResult(responseResult.data)
                is ResponseResult.Failure -> showError(responseResult.message)
            }
        })

        viewBinding.rvList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = EventAdapter(
                ArrayList(),
                this@MainActivity
            )
        }

        /**
         * Set listener to Swipe Refresh
         */
        viewBinding.srSwipeRefresh.setOnRefreshListener(this)
        viewModel.loadEventList()
    }

    private fun showResult(result: List<Event>) {
        viewBinding.rvList.apply { (adapter as EventAdapter).setList(result.map { it.toEventView() }) }
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun cardClick(position: Int) {
        DetailsActivity.invoke(this, viewModel.eventList[position])
    }

    override fun checkinClick(position: Int) {
        viewModel.makeCheckin(Checkin("$position", "John Doe", "john.joe@gmail.com"))
    }

    override fun onRefresh() {
        viewBinding.srSwipeRefresh.isRefreshing = true
        viewModel.loadEventList()
    }
}
