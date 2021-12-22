package pt.nunomatos.fitbodapp.ui.main

import android.os.Bundle
import android.util.Log
import android.widget.ViewFlipper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import pt.nunomatos.fitbodapp.base.BaseActivity
import pt.nunomatos.fitbodapp.data.model.FitbodWorkoutModel
import pt.nunomatos.fitbodapp.databinding.ActivityMainBinding
import pt.nunomatos.fitbodapp.ui.detail.WorkoutDetailsActivity
import pt.nunomatos.fitbodapp.utils.showChildAtIndex
import java.io.IOException

class MainActivity : BaseActivity<MainContract.View, MainContract.Presenter>(), MainContract.View {

    companion object {

        private const val FILE_NAME = "workout_data.txt"

        private const val FLIPPER_POSITION_CONTENT = 0
        private const val FLIPPER_POSITION_LOADING = 1
        private const val FLIPPER_POSITION_EMPTY_VIEW = 2
        private const val FLIPPER_POSITION_ERROR = 3
    }

    private lateinit var binding: ActivityMainBinding

    private lateinit var workoutExercisesList: RecyclerView
    private lateinit var viewFlipper: ViewFlipper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        showLoading()
        loadFile()
    }

    override fun bindViews() {

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        workoutExercisesList = binding.workoutExercisesList.apply {
            layoutManager =
                LinearLayoutManager(
                    this@MainActivity, LinearLayoutManager.VERTICAL,
                    false
                )
        }

        viewFlipper = binding.viewFlipper
    }

    override fun createPresenter(): MainContract.Presenter {
        val presenter: MainPresenter by inject { parametersOf(this) }
        return presenter
    }

    override fun onWorkoutExercisesReceived(workoutExercises: List<FitbodWorkoutModel>) {
        workoutExercisesList.adapter = WorkoutExerciseListAdapter(workoutExercises,
            onExerciseClicked = { exercise ->
                startActivity(WorkoutDetailsActivity.getIntent(this, exercise))
            })
    }

    override fun showContent() {
        viewFlipper.showChildAtIndex(FLIPPER_POSITION_CONTENT)
    }

    override fun showLoading() {
        viewFlipper.showChildAtIndex(FLIPPER_POSITION_LOADING)
    }

    override fun showEmptyView() {
        viewFlipper.showChildAtIndex(FLIPPER_POSITION_EMPTY_VIEW)
    }

    override fun showError() {
        viewFlipper.showChildAtIndex(FLIPPER_POSITION_ERROR)
    }

    private fun loadFile() {
        try {
            val reader = assets.open(FILE_NAME).bufferedReader()
            presenter?.loadWorkoutExercisesHistory(reader)
        } catch (exception: IOException) {
            Log.d(MainActivity::class.java.name, "Error: " + exception.message)
            showError()
        }

    }
}