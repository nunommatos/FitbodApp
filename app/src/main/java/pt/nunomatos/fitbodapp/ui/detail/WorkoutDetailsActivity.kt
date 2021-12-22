package pt.nunomatos.fitbodapp.ui.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ViewFlipper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import pt.nunomatos.fitbodapp.base.BaseActivity
import pt.nunomatos.fitbodapp.data.model.FitbodWorkoutModel
import pt.nunomatos.fitbodapp.data.model.RepMaxWeightModel
import pt.nunomatos.fitbodapp.databinding.ActivityWorkoutDetailsBinding
import pt.nunomatos.fitbodapp.utils.convertToDate
import pt.nunomatos.fitbodapp.utils.showChildAtIndex


class WorkoutDetailsActivity : BaseActivity<WorkoutDetailsContract.View,
        WorkoutDetailsContract.Presenter>(), WorkoutDetailsContract.View {

    companion object {

        private const val EXTRA_WORKOUT_HISTORY = "extra_workout_history"

        private const val FLIPPER_POSITION_CONTENT = 0
        private const val FLIPPER_POSITION_LOADING = 1
        private const val FLIPPER_POSITION_EMPTY_VIEW = 2
        private const val FLIPPER_POSITION_ERROR = 3

        fun getIntent(context: Context, workoutExercise: FitbodWorkoutModel) =
            Intent(context, WorkoutDetailsActivity::class.java).apply {
                putExtra(EXTRA_WORKOUT_HISTORY, workoutExercise)
            }
    }

    private lateinit var binding: ActivityWorkoutDetailsBinding

    private lateinit var historyList: RecyclerView
    private lateinit var viewFlipper: ViewFlipper

    private lateinit var workoutExercise: FitbodWorkoutModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (intent?.hasExtra(EXTRA_WORKOUT_HISTORY) == true) {
            workoutExercise = intent?.extras?.getParcelable(EXTRA_WORKOUT_HISTORY)!!

            showLoading()
            setupUI()

            presenter?.extractValuesFromWorkout(workoutExercise)
        } else {
            finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun bindViews() {
        binding = ActivityWorkoutDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        historyList = binding.historyList
        viewFlipper = binding.viewFlipper
    }

    override fun createPresenter(): WorkoutDetailsContract.Presenter {
        val presenter: WorkoutDetailsPresenter by inject { parametersOf(this) }
        return presenter
    }

    override fun onWorkoutValuesReceived(values: List<RepMaxWeightModel>) {
        historyList.apply {
            layoutManager = LinearLayoutManager(
                this@WorkoutDetailsActivity,
                LinearLayoutManager.VERTICAL, false
            )
            adapter = WorkoutDetailsListAdapter(
                repMaxGraphList = values.sortedBy { it.date.convertToDate() },
                repMaxList = workoutExercise.repMaxWeight.sortedByDescending { it.date.convertToDate() },
                historyRepMax = workoutExercise.repMaxWeight.maxByOrNull { it.maxWeight }
            )
        }
    }

    override fun showContent() {
        viewFlipper.showChildAtIndex(FLIPPER_POSITION_CONTENT)
    }

    override fun showLoading() {
        viewFlipper.showChildAtIndex(FLIPPER_POSITION_LOADING)
    }

    override fun showError() {
        viewFlipper.showChildAtIndex(FLIPPER_POSITION_ERROR)
    }

    override fun showEmptyView() {
        viewFlipper.showChildAtIndex(FLIPPER_POSITION_EMPTY_VIEW)
    }

    private fun setupUI() {
        title = workoutExercise.exerciseName
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }
}