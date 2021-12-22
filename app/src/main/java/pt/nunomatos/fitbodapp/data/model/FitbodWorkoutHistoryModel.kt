package pt.nunomatos.fitbodapp.data.model

data class FitbodWorkoutHistoryModel(
    val date: String,
    val name: String,
    val sets: Int,
    val reps: Int,
    val weight: Double
)