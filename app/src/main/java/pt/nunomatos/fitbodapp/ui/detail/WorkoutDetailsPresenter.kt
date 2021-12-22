package pt.nunomatos.fitbodapp.ui.detail

import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import pt.nunomatos.fitbodapp.base.BasePresenter
import pt.nunomatos.fitbodapp.data.model.FitbodWorkoutModel
import pt.nunomatos.fitbodapp.data.repository.FitbodRepository

class WorkoutDetailsPresenter(private val fitbodRepository: FitbodRepository) :
    BasePresenter<WorkoutDetailsContract.View>(), WorkoutDetailsContract.Presenter {

    companion object {
        private val TAG = WorkoutDetailsPresenter::class.java.name
    }

    override fun extractValuesFromWorkout(workoutExercise: FitbodWorkoutModel) {

        view?.showLoading()

        fitbodRepository.extractValuesFromWorkout(workoutExercise)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onSuccess = {
                if (!it.isNullOrEmpty()) {
                    view?.onWorkoutValuesReceived(it)
                    view?.showContent()
                } else {
                    view?.showEmptyView()
                }
            }, onError = {
                Log.d(TAG, "Error: " + it.message)
                view?.showError()
            })
            .addTo(disposables)
    }
}