package com.example.eventExplorer.presentation.screen

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.squareup.picasso.Picasso
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

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
        viewBinding.lifecycleOwner = this

        viewModel.event.observe(this, Observer { responseResult ->
            when (responseResult) {
                is ResponseResult.Success -> showResult(responseResult.data)
                is ResponseResult.Failure -> showError(responseResult.message)
            }
        })

        /** maps **/
        mapFragment = supportFragmentManager.findFragmentById(viewBinding.mapView.id) as SupportMapFragment
        mapFragment?.getMapAsync(this)


        eventExtra?.let { event ->
            viewBinding.apply {
                Picasso.get()
                    .load(event.image)
                    .placeholder(R.drawable.placeholder_img)
                    .error(R.drawable.error_img)
                    .into(viewBinding.photoView)

                dateLabelView.text = Date(event.date).toFormattedString()
                hourLabelView.text = Date(event.date).toHour()
                cityLabelView.text = getCity(event.latitude, event.longitude)
                addressLabelView.text = getFullAddress(event.latitude, event.longitude)
                priceLabelView.text = event.price.toCurrency()
                descLabelView.text = event.description
            }
        }

    }

    private fun showResult(result: Event) {
        Toast.makeText(this, result.title, Toast.LENGTH_LONG).show()
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun onMapReady(p0: GoogleMap?) {

        mMap = p0

        mMap?.setOnMarkerClickListener { arg0 ->
            true
        }

        mMap?.setOnMapClickListener{ }

        eventExtra?.apply {
            setLocation(latitude, longitude)
        }
    }

    private fun setLocation(lat: Double, long: Double) {
        val position = LatLng(lat, long)
        val name = "Name"
        val address = "Address"
        mMap?.addMarker(
            MarkerOptions().position(position).title(name).snippet(address)
        )
        /** posiciona câmera **/
        mMap?.moveCamera(CameraUpdateFactory.newLatLng(position))
        mMap?.animateCamera(CameraUpdateFactory.zoomTo(15f), 1000, null)
        /** select type of map **/
        mMap?.mapType = GoogleMap.MAP_TYPE_NORMAL
    }

    companion object {
        private const val EVENT_EXTRA = "event_extra"
        fun invoke(context: Context, event: Event) {
            val intent = Intent(context, DetailsActivity::class.java)
            intent.putExtra(EVENT_EXTRA, event)
            context.startActivity(intent)
        }
    }

}
