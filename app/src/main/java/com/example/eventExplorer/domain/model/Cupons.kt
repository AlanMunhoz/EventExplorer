package com.example.eventExplorer.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Cupons(val id: String, val eventId: String, val discount: Int) : Parcelable