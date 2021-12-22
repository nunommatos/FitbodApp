package pt.nunomatos.fitbodapp.ui.details

import android.os.Looper.getMainLooper
import io.reactivex.Single
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import pt.nunomatos.fitbodapp.base.BasePresenterTest
import pt.nunomatos.fitbodapp.data.model.FitbodWorkoutModel
import pt.nunomatos.fitbodapp.data.model.RepMaxWeightModel
import pt.nunomatos.fitbodapp.data.repository.FitbodRepository
import pt.nunomatos.fitbodapp.ui.detail.WorkoutDetailsContract
import pt.nunomatos.fitbodapp.ui.detail.WorkoutDetailsPresenter

@RunWith(RobolectricTestRunner::class)
class WorkoutDetailsPresenterTest : BasePresenterTest<WorkoutDetailsPresenter>() {

    @Mock
    private val fitbodRepository = mock(FitbodRepository::class.java)

    @Mock
    private val view = mock(WorkoutDetailsContract.View::class.java)

    @Mock
    private val workoutExercise = mock(FitbodWorkoutModel::class.java)

    override fun buildPresenter(): WorkoutDetailsPresenter {
        return WorkoutDetailsPresenter(fitbodRepository)
    }

    override fun attachViewToPresenter() {
        presenter?.attachView(view)
    }

    @Test
    fun testExtractValuesFromWorkout() {

        val mockList = listOf(
            RepMaxWeightModel(
                date = "Nov 14 2020",
                maxWeight = 25.0
            ),
            RepMaxWeightModel(
                date = "Nov 24 2020",
                maxWeight = 35.0
            ),
            RepMaxWeightModel(
                date = "Nov 29 2020",
                maxWeight = 37.0
            )
        )

        `when`(fitbodRepository.extractValuesFromWorkout(workoutExercise)).thenReturn(
            Single.just(
                mockList
            )
        )

        presenter?.extractValuesFromWorkout(workoutExercise)
        shadowOf(getMainLooper()).idle()

        verify(view).showLoading()
        verify(view).onWorkoutValuesReceived(mockList)
        verify(view).showContent()
    }

    @Test
    fun testExtractValuesFromWorkout_EmptyList() {

        `when`(fitbodRepository.extractValuesFromWorkout(workoutExercise)).thenReturn(
            Single.just(
                emptyList()
            )
        )

        presenter?.extractValuesFromWorkout(workoutExercise)
        shadowOf(getMainLooper()).idle()

        verify(view).showLoading()
        verify(view).showEmptyView()
    }

    @Test
    fun testExtractValuesFromWorkout_Error() {

        `when`(fitbodRepository.extractValuesFromWorkout(workoutExercise)).thenReturn(
            Single.error(
                Throwable()
            )
        )

        presenter?.extractValuesFromWorkout(workoutExercise)
        shadowOf(getMainLooper()).idle()

        verify(view).showLoading()
        verify(view).showError()

    }
}