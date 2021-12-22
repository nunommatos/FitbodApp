package pt.nunomatos.fitbodapp.ui.main

import pt.nunomatos.fitbodapp.base.BaseContract
import pt.nunomatos.fitbodapp.data.model.FitbodWorkoutModel
import java.io.BufferedReader

interface MainContract {

    interface View : BaseContract.View {
        fun showContent()
        fun showEmptyView()
        fun showLoading()
        fun showError()
        fun onWorkoutExercisesReceived(workoutExercises: List<FitbodWorkoutModel>)
    }

    interface Presenter : BaseContract.Presenter<View> {
        fun loadWorkoutExercisesHistory(reader: BufferedReader)
    }
}