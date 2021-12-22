package pt.nunomatos.fitbodapp.ui.detail

import pt.nunomatos.fitbodapp.base.BaseContract
import pt.nunomatos.fitbodapp.data.model.FitbodWorkoutModel
import pt.nunomatos.fitbodapp.data.model.RepMaxWeightModel

interface WorkoutDetailsContract {

    interface View : BaseContract.View {
        fun onWorkoutValuesReceived(values: List<RepMaxWeightModel>)
        fun showContent()
        fun showLoading()
        fun showError()
        fun showEmptyView()
    }

    interface Presenter : BaseContract.Presenter<View> {
        fun extractValuesFromWorkout(workoutExercise: FitbodWorkoutModel)
    }
}