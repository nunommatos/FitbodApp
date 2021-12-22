package pt.nunomatos.fitbodapp.ui.di

import org.koin.dsl.module
import pt.nunomatos.fitbodapp.ui.detail.WorkoutDetailsPresenter
import pt.nunomatos.fitbodapp.ui.main.MainPresenter

val uiModule = module {
    factory { MainPresenter(fitbodRepository = get()) }
    factory { WorkoutDetailsPresenter(fitbodRepository = get()) }
}