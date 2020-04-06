package com.example.eventExplorer.presentation.screen

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.eventExplorer.R
import com.example.eventExplorer.data.util.toEventView
import com.example.eventExplorer.databinding.ActivityMainBinding
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
            when (responseResult) {
                is ResponseResult.Success -> showResult(responseResult.data)
                is ResponseResult.Failure -> showError()
            }
        })

        viewModel.requestInProgress.observe(this, Observer {
            viewBinding.srSwipeRefresh.isRefreshing = it
        })

        initLayout()
    }

    private fun initLayout() {
        setUpToolbar()
        viewBinding.rvList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = EventAdapter(ArrayList(), this@MainActivity)
            addOnScrollListener(onScrollListener)
        }
        viewBinding.srSwipeRefresh.setOnRefreshListener(this)
        viewModel.loadEventList()
    }

    private fun setUpToolbar() {
        setSupportActionBar(viewBinding.toolbar)
        supportActionBar?.apply {
            title = getString(R.string.app_name)
        }
    }

    private val onScrollListener by lazy {
        object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (dy == 0) return

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                if (layoutManager.findLastCompletelyVisibleItemPosition() == layoutManager.itemCount - 1) {
                    viewModel.loadEvent()
                }
            }
        }
    }

    private fun showResult(result: List<Event>) {
        viewBinding.rvList.apply { (adapter as EventAdapter).setList(result.map { it.toEventView() }) }
    }

    private fun showError() {
        Toast.makeText(this, getString(R.string.request_error), Toast.LENGTH_LONG).show()
    }

    override fun cardClick(position: Int) {
        DetailsActivity.start(this, viewModel.eventList[position])
    }

    override fun shareClick(position: Int) {
        val target = Intent(Intent.ACTION_SEND)
        target.type = "text/plain"
        target.putExtra(Intent.EXTRA_TEXT, "${viewModel.eventList[position].title}")
        startActivity(target)
    }

    override fun onRefresh() {
        viewModel.loadEventList()
    }
}
