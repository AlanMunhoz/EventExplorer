package com.example.eventExplorer.presentation.screen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.eventExplorer.R
import com.example.eventExplorer.databinding.ActivityMainBinding
import com.example.eventExplorer.domain.model.ResponseResult
import com.example.eventExplorer.presentation.viewModel.EventViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

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
                is ResponseResult.Success -> showResult(responseResult.data.title)
                is ResponseResult.Failure -> showError(responseResult.message)
            }
        })
    }

    private fun showResult(result: String) {
        viewBinding.textView.text = "$result"
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
