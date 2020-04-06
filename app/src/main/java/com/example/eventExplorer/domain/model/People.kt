package com.example.eventExplorer.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class People (val id: String, val eventId: String, val name: String, val picture: String) : Parcelable