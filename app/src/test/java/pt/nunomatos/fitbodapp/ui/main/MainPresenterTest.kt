package pt.nunomatos.fitbodapp.ui.main

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
import java.io.BufferedReader

@RunWith(RobolectricTestRunner::class)
class MainPresenterTest : BasePresenterTest<MainPresenter>() {

    @Mock
    private val fitbodRepository = mock(FitbodRepository::class.java)

    @Mock
    private val view = mock(MainContract.View::class.java)

    @Mock
    private val bufferedReader = mock(BufferedReader::class.java)

    override fun buildPresenter(): MainPresenter {
        return MainPresenter(fitbodRepository)
    }

    override fun attachViewToPresenter() {
        presenter?.attachView(view)
    }

    @Test
    fun testLoadWorkoutExercises() {

        val mockList = listOf(
            FitbodWorkoutModel(
                exerciseName = "Mock 1",
                repMaxWeight = listOf(
                    RepMaxWeightModel(
                        date = "Oct 14 2020",
                        maxWeight = 10.0
                    ),
                    RepMaxWeightModel(
                        date = "Oct 24 2020",
                        maxWeight = 20.0
                    ),
                    RepMaxWeightModel(
                        date = "Oct 29 2020",
                        maxWeight = 30.0
                    )
                )
            ),
            FitbodWorkoutModel(
                exerciseName = "Mock 2",
                repMaxWeight = listOf(
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
            )
        )

        `when`(fitbodRepository.getWorkouts(bufferedReader)).thenReturn(Single.just(mockList))

        presenter?.loadWorkoutExercisesHistory(bufferedReader)
        shadowOf(getMainLooper()).idle()

        verify(view).showLoading()
        verify(view).onWorkoutExercisesReceived(mockList)
        verify(view).showContent()
    }

    @Test
    fun testLoadWorkoutExercises_EmptyList() {

        `when`(fitbodRepository.getWorkouts(bufferedReader)).thenReturn(Single.just(emptyList()))

        presenter?.loadWorkoutExercisesHistory(bufferedReader)
        shadowOf(getMainLooper()).idle()

        verify(view).showLoading()
        verify(view).showEmptyView()
    }

    @Test
    fun testLoadWorkoutExercises_Error() {

        `when`(fitbodRepository.getWorkouts(bufferedReader)).thenReturn(Single.error(Throwable()))

        presenter?.loadWorkoutExercisesHistory(bufferedReader)
        shadowOf(getMainLooper()).idle()

        verify(view).showLoading()
        verify(view).showError()

    }
}