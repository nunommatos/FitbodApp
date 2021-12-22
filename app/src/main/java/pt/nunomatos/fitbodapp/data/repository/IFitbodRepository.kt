package pt.nunomatos.fitbodapp.data.repository

import io.reactivex.Single
import pt.nunomatos.fitbodapp.data.model.FitbodWorkoutModel
import pt.nunomatos.fitbodapp.data.model.RepMaxWeightModel
import java.io.BufferedReader

interface IFitbodRepository {

    fun getWorkouts(reader: BufferedReader): Single<List<FitbodWorkoutModel>>

    fun extractValuesFromWorkout(workoutExercise: FitbodWorkoutModel): Single<List<RepMaxWeightModel>>
}