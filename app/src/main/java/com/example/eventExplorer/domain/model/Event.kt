package com.example.eventExplorer.domain.model

import android.os.Parcelable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.example.eventExplorer.R
import com.example.eventExplorer.data.util.*
import com.squareup.picasso.Picasso
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
class Event (
    val people: List<People>,
    val date: Long,
    val description: String,
    val image: String,
    val longitude: Double,
    val latitude: Double,
    val price: Float,
    val title: String,
    val id: String,
    val cupons: List<Cupons>
) : Parcelable {

    fun getDateLabel() = Date(date).toFormattedString()
    fun getHourLabel() = Date(date).toHour()
    fun getCityLabel() = getCity(latitude, longitude)
    fun getAddressLabel() = getFullAddress(latitude, longitude)
    fun getPriceLabel() = price.toCurrency()

    companion object {
        @JvmStatic
        @BindingAdapter( "loadImage" )
        fun loadImage(iv: ImageView, urlImage: String ){
            Picasso.get()
                .load(urlImage)
                .placeholder(R.drawable.placeholder_img)
                .error(R.drawable.error_img)
                .into(iv)
        }
    }
}