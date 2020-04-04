package com.example.eventExplorer.presentation.screen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eventExplorer.R
import com.example.eventExplorer.data.util.toEventView
import com.example.eventExplorer.databinding.ActivityMainBinding
import com.example.eventExplorer.domain.model.Event
import com.example.eventExplorer.domain.model.ResponseResult
import com.example.eventExplorer.presentation.viewModel.EventViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(), EventAdapter.ClickCallback {

    private lateinit var viewBinding: ActivityMainBinding
    private val viewModel: EventViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewBinding.viewModel = viewModel
        viewBinding.lifecycleOwner = this

        viewModel.event.observe(this, Observer { responseResult ->
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
    }

    private fun showResult(result: Event) {
        viewBinding.textView.text = result.title
        viewBinding.rvList.apply { (adapter as EventAdapter).insert(result.toEventView()) }
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun cardClick(eventAdapterItem: EventView) {
        startActivity(Intent(this, DetailsActivity::class.java))
    }

    override fun checkinClick(eventAdapterItem: EventView) {
        Toast.makeText(this, "icon click", Toast.LENGTH_LONG).show()
    }
}
