package pt.nunomatos.fitbodapp.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RepMaxWeightModel(
    val date: String,
    val maxWeight: Double
) : Parcelable
