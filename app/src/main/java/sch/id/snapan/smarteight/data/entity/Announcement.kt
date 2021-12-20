package sch.id.snapan.smarteight.data.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Announcement (
    val uid: String = "",
    val id: String = "",
    val imageUrl: String? = "",
    var title: String = "",
    var message: String = "",
    var date: String = ""
        ): Parcelable