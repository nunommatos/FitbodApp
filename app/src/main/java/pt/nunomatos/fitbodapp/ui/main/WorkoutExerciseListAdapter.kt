package pt.nunomatos.fitbodapp.ui.main

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pt.nunomatos.fitbodapp.R
import pt.nunomatos.fitbodapp.data.model.FitbodWorkoutModel
import pt.nunomatos.fitbodapp.utils.inflateLayoutOnParent

class WorkoutExerciseListAdapter(
    private val workoutExercises: List<FitbodWorkoutModel>,
    private val onExerciseClicked: (FitbodWorkoutModel) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount(): Int {
        return workoutExercises.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return WorkoutViewHolder(parent.inflateLayoutOnParent(R.layout.item_workout_exercise_list))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? WorkoutViewHolder)?.bind(workoutExercises[position])
    }

    inner class WorkoutViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val exerciseName = itemView.findViewById<TextView>(R.id.exerciseName)
        private val repMaxValue = itemView.findViewById<TextView>(R.id.repMaxValue)

        fun bind(exercise: FitbodWorkoutModel) {
            val repMaxWeight = exercise.repMaxWeight.maxByOrNull { it.maxWeight }
            exerciseName.text = exercise.exerciseName
            repMaxValue.text = String.format("%.2f", repMaxWeight?.maxWeight)

            itemView.setOnClickListener {
                onExerciseClicked(exercise)
            }
        }

    }
}