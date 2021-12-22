package pt.nunomatos.fitbodapp.data.repository

import android.util.Log
import io.reactivex.Single
import pt.nunomatos.fitbodapp.data.model.FitbodWorkoutHistoryModel
import pt.nunomatos.fitbodapp.data.model.FitbodWorkoutModel
import pt.nunomatos.fitbodapp.data.model.RepMaxWeightModel
import pt.nunomatos.fitbodapp.utils.convertToDate
import java.io.BufferedReader
import kotlin.math.min

class FitbodRepository : IFitbodRepository {

    companion object {
        private const val MAX_X_VALUES_TO_DISPLAY = 8
    }

    override fun getWorkouts(reader: BufferedReader): Single<List<FitbodWorkoutModel>> {

        return Single.just(reader.use(BufferedReader::readText))
            .flatMap { workoutHistory ->

                val workoutExerciseNames = hashMapOf<String, ArrayList<FitbodWorkoutHistoryModel>>()

                // get every entry of the workout history
                workoutHistory.lines()
                    .filter { it.isNotEmpty() }
                    .forEach { entry ->
                        try {
                            val entryInfo = entry.split(",")
                            if (entryInfo.size == 5) {
                                val name = entryInfo[1]

                                val workoutHistoryEntry = FitbodWorkoutHistoryModel(
                                    date = entryInfo.first(),
                                    name = name,
                                    sets = entryInfo[2].toInt(),
                                    reps = entryInfo[3].toInt(),
                                    weight = entryInfo.last().toDouble()
                                )

                                // populate the map with the different exercise names and their corresponding
                                // list of exercises
                                if (workoutExerciseNames.containsKey(name)) {
                                    workoutExerciseNames[name]?.add(workoutHistoryEntry)
                                } else {
                                    workoutExerciseNames[name] = arrayListOf(workoutHistoryEntry)
                                }
                            }
                        } catch (exception: NumberFormatException) {
                            Log.d(FitbodRepository::class.java.name, "Error: " + exception.message)
                        }
                    }

                Single.just(workoutExerciseNames.keys.map { name ->

                    // get all the obtained names
                    val performances = workoutExerciseNames[name].orEmpty()

                    // for every existing name, group the exercises by date
                    val exercisePerformances = performances
                        .groupBy { it.date }
                        .map { mapEntry ->

                            // for every date, get the lifted weights
                            val date = mapEntry.key
                            val valuesForDate = mapEntry.value

                            var repMaxDay = 0.0
                            valuesForDate.forEach { value ->
                                val repMax = calculateOneRepMax(value.reps, value.weight)
                                if (repMax > repMaxDay) {
                                    repMaxDay = repMax
                                }
                            }

                            RepMaxWeightModel(
                                date = date,
                                maxWeight = repMaxDay
                            )
                        }

                    FitbodWorkoutModel(name, exercisePerformances)
                })
            }
    }

    override fun extractValuesFromWorkout(workoutExercise: FitbodWorkoutModel): Single<List<RepMaxWeightModel>> {
        return Single.just(workoutExercise)
            .flatMap {

                val allValues = workoutExercise.repMaxWeight.sortedBy {
                    it.date.convertToDate()
                }

                // we add 2 so we can make an "empty space" in the beginning and in the end of x axis
                val xAxisValuesToDisplay = min(allValues.size, MAX_X_VALUES_TO_DISPLAY) + 2

                val valuesToDisplay = arrayListOf<RepMaxWeightModel>()

                // if there are less dates than the max to display, we show all of them
                if (allValues.size <= xAxisValuesToDisplay) {
                    return@flatMap Single.just(allValues)
                }

                val maxValue = allValues.maxByOrNull { it.maxWeight }
                val maxValueIndex = allValues.indexOf(maxValue)

                // add the first existing record
                valuesToDisplay.add(allValues.first())

                // subtract 4 because we've already added the first record and will add the last record
                //and the from the initial calculation for the number of values to display
                val displayValues = xAxisValuesToDisplay - 4
                val valueInc = ((allValues.size - 2) / displayValues)
                var firstItemIndex = valueInc

                for (i in 0 until displayValues) {

                    // if the max value is the first or the last one it'll be added to the list, so
                    //there's no need to add it here
                    if (maxValueIndex != 0 && maxValueIndex != allValues.lastIndex) {

                        val nextInc = firstItemIndex + valueInc

                        // if the max value index is greater than the current iteration and less than
                        //the next iteration, we need to add it here, otherwise it won't be added at all
                        if (maxValueIndex in (firstItemIndex + 1) until nextInc) {
                            valuesToDisplay.add(allValues[maxValueIndex])
                        } else {
                            valuesToDisplay.add(allValues[firstItemIndex])
                        }

                    } else {
                        valuesToDisplay.add(allValues[firstItemIndex])
                    }

                    firstItemIndex += valueInc
                }

                // add the last existing record
                valuesToDisplay.add(allValues.last())

                return@flatMap Single.just(valuesToDisplay)
            }
    }

    private fun calculateOneRepMax(reps: Int, weight: Double): Double {

        // we're using the Brzycki Formula to calculate the OneRepMax
        return if (reps == 37) {
            weight
        } else {
            (36 * weight) / (37 - reps)
        }
    }
}