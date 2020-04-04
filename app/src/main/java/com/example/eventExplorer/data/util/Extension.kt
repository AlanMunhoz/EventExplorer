package com.example.eventExplorer.data.util

import android.location.Geocoder
import com.example.eventExplorer.MainApplication
import com.example.eventExplorer.domain.model.Event
import com.example.eventExplorer.domain.model.ResponseResult
import com.example.eventExplorer.presentation.screen.EventView
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

fun <T : Any> Response<T>.responseResult(): ResponseResult<T> {
    return this.body()?.let {
        ResponseResult.Success(it)
    } ?: run {
        ResponseResult.Failure("HttpCode: ${this.code()}")
    }
}

fun Event.toEventView() = EventView(
    title = this.title,
    image = this.image,
    day = Date(this.date).toDayOfMonth(),
    month = Date(this.date).toMonth(),
    local = getShortAddress(this.latitude, this.longitude)
)

fun Date?.toFormattedString() =
    this?.let {
        SimpleDateFormat("dd/MM/yyy HH:mm", Locale("pt")).format(this)
    } ?: "Date couldn't be converted"

fun Date?.toDayOfMonth() =
    this?.let {
        SimpleDateFormat("d", Locale("pt")).format(this)
    } ?: "Date couldn't be converted"

fun Date?.toMonth() =
    this?.let {
        SimpleDateFormat("MMM", Locale("pt")).format(this).capitalize()
    } ?: "Date couldn't be converted"

fun getFullAddress(latitude: Double, longitude: Double): String {
    val geocode = Geocoder(MainApplication.getContext(), Locale.getDefault())
    return geocode.getFromLocation(latitude, longitude, 1).firstOrNull() ?.
        getAddressLine(0) ?: "Address couldn't be converted"
}

fun getShortAddress(latitude: Double, longitude: Double): String {
    val geocode = Geocoder(MainApplication.getContext(), Locale.getDefault())
    return geocode.getFromLocation(latitude, longitude, 1).firstOrNull() ?.
        subAdminArea ?: "Address couldn't be converted"
}
