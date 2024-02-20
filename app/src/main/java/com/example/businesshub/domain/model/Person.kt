package com.example.businesshub.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Person(
    private val name:String,
    private val surname:String,
    private val email: String? = null,
    private val phone: String?,
):Parcelable