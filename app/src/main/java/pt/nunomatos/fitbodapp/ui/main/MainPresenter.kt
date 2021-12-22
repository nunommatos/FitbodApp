package pt.nunomatos.fitbodapp.ui.main

import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import pt.nunomatos.fitbodapp.base.BasePresenter
import pt.nunomatos.fitbodapp.data.repository.FitbodRepository
import java.io.BufferedReader

class MainPresenter(private val fitbodRepository: FitbodRepository) :
    BasePresenter<MainContract.View>(), MainContract.Presenter {

    companion object {
        private val TAG = MainPresenter::class.java.name
    }

    override fun loadWorkoutExercisesHistory(reader: BufferedReader) {

        view?.showLoading()

        fitbodRepository.getWorkouts(reader)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onSuccess = {
                if (!it.isNullOrEmpty()) {
                    view?.onWorkoutExercisesReceived(it)
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