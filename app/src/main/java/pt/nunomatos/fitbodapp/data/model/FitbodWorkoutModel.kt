package pt.nunomatos.fitbodapp.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FitbodWorkoutModel(
    val exerciseName: String,
    val repMaxWeight: List<RepMaxWeightModel>
) : Parcelable
