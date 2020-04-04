package com.example.eventExplorer.presentation.screen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.eventExplorer.R
import com.example.eventExplorer.databinding.ActivityDetailsBinding
import com.example.eventExplorer.domain.model.Event
import com.example.eventExplorer.domain.model.ResponseResult
import com.example.eventExplorer.presentation.viewModel.EventViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailsActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityDetailsBinding
    private val viewModel: EventViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_details)
        viewBinding.viewModel = viewModel
        viewBinding.lifecycleOwner = this

        viewModel.event.observe(this, Observer { responseResult ->
            when (responseResult) {
                is ResponseResult.Success -> showResult(responseResult.data)
                is ResponseResult.Failure -> showError(responseResult.message)
            }
        })
    }

    private fun showResult(result: Event) {
        Toast.makeText(this, result.title, Toast.LENGTH_LONG).show()
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

}
