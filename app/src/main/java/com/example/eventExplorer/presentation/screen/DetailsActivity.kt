package com.example.eventExplorer.presentation.screen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.eventExplorer.R
import com.example.eventExplorer.data.util.*
import com.example.eventExplorer.databinding.ActivityDetailsBinding
import com.example.eventExplorer.domain.model.Event
import com.example.eventExplorer.domain.model.ResponseResult
import com.example.eventExplorer.presentation.viewModel.EventViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailsActivity : AppCompatActivity(), OnMapReadyCallback {

    private val eventExtra by lazy { intent.getParcelableExtra(EVENT_EXTRA) as? Event }
    private lateinit var viewBinding: ActivityDetailsBinding
    private val viewModel: EventViewModel by viewModel()
    private var mapFragment: SupportMapFragment? = null
    private var mMap: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_details)
        viewBinding.viewModel = viewModel
        viewBinding.eventBinding = eventExtra
        viewBinding.lifecycleOwner = this

        viewModel.event.observe(this, Observer { responseResult ->
            when (responseResult) {
                is ResponseResult.Success -> showResult(responseResult.data)
                is ResponseResult.Failure -> showError(responseResult.message)
            }
        })

        initLayout()
    }

    private fun initLayout() {

        setSupportActionBar(viewBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        viewBinding.collapseToolbar.setExpandedTitleColor(ContextCompat.getColor(this, R.color.primaryTextColor))
        viewBinding.collapseToolbar.setCollapsedTitleTextColor(ContextCompat.getColor(this, R.color.primaryTextColor))
        viewBinding.collapseToolbar.setContentScrimColor(ContextCompat.getColor(this, R.color.primaryColor))

        mapFragment = supportFragmentManager.findFragmentById(viewBinding.mapView.id) as SupportMapFragment
        mapFragment?.getMapAsync(this)

        viewBinding.checkinFab.setOnClickListener {
            eventExtra?.apply {
                viewModel.makeCheckin(id)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showResult(result: Event) {}

    private fun showError(message: String) {}

    override fun onMapReady(p0: GoogleMap?) {
        mMap = p0
        mMap?.uiSettings?.setAllGesturesEnabled(false)
        eventExtra?.apply {
            setLocation(latitude, longitude)
        }
    }

    private fun setLocation(lat: Double, long: Double) {
        val position = LatLng(lat, long)
        val name = eventExtra?.title
        val address = getCity(lat, long)
        mMap?.apply {
            addMarker(MarkerOptions().position(position).title(name).snippet(address))
            animateCamera(CameraUpdateFactory.newLatLngZoom(position, 15f))
            mapType = GoogleMap.MAP_TYPE_NORMAL
        }
    }

    companion object {
        private const val EVENT_EXTRA = "event_extra"
        fun start(context: Context, event: Event) {
            val intent = Intent(context, DetailsActivity::class.java)
            intent.putExtra(EVENT_EXTRA, event)
            context.startActivity(intent)
        }
    }
}
