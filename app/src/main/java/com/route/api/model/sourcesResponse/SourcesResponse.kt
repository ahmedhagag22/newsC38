package com.route.api.model.sourcesResponse

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class SourcesResponse(

    @field:SerializedName("sources")
    val sources: List<Source?>? = null,
    @field:SerializedName("status")
    val status: String? = null,

    @field:SerializedName("message")
    val message: String? = null,
    @field:SerializedName("code")
    val code: String? = null,

    ) : Parcelable