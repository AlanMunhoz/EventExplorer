package com.example.eventExplorer.data.util

import android.location.Geocoder
import com.example.eventExplorer.MainApplication
import com.example.eventExplorer.R
import com.example.eventExplorer.domain.model.Event
import com.example.eventExplorer.domain.model.ResponseResult
import com.example.eventExplorer.presentation.model.EventView
import retrofit2.Response
import java.text.DecimalFormat
import java.text.NumberFormat
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
    local = getCity(this.latitude, this.longitude)
)

fun Date?.toFormattedString() =
    this?.let {
        "${it.toWeekDay()}, ${it.toDayOfMonth()} de ${it.toMonth()}"
    } ?: MainApplication.getContext()?.getString(R.string.data_format_error)

fun Date?.toDayOfMonth() =
    this?.let {
        SimpleDateFormat("d", Locale("pt")).format(this)
    } ?: MainApplication.getContext()?.getString(R.string.data_format_error) ?: ""

fun Date?.toMonth() =
    this?.let {
        SimpleDateFormat("MMMM", Locale("pt")).format(this).capitalize()
    } ?: MainApplication.getContext()?.getString(R.string.data_format_error) ?: ""

fun Date?.toWeekDay() =
        this?.let {
        SimpleDateFormat("EEEE", Locale("pt")).format(this).capitalize()
    } ?: MainApplication.getContext()?.getString(R.string.data_format_error) ?:""

fun Date?.toHour() =
    this?.let {
        SimpleDateFormat("HH:mm", Locale("pt")).format(this)
    } ?: MainApplication.getContext()?.getString(R.string.data_format_error) ?:""

fun Float.toCurrency(symbol: String = "R$"): String =
    (NumberFormat.getNumberInstance(Locale("pt")) as DecimalFormat)
        .apply {
            maximumFractionDigits = 2
            minimumFractionDigits = 2
            applyPattern("###,###,##0.00")
        }
        .format(this)
        .let {
            if (it.isNotEmpty())
                "$symbol $it"
            else it
        }

fun getFullAddress(latitude: Double, longitude: Double): String {
    val geocode = Geocoder(MainApplication.getContext(), Locale.getDefault())
    return geocode.getFromLocation(latitude, longitude, 1).firstOrNull() ?.
        getAddressLine(0) ?: MainApplication.getContext()?.getString(R.string.address_format_error) ?:""
}

fun getCity(latitude: Double, longitude: Double): String {
    val geocode = Geocoder(MainApplication.getContext(), Locale.getDefault())
    return geocode.getFromLocation(latitude, longitude, 1).firstOrNull() ?.
        subAdminArea ?: MainApplication.getContext()?.getString(R.string.address_format_error) ?:""
}

