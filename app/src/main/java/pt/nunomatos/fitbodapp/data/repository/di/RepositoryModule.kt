package pt.nunomatos.fitbodapp.data.repository.di

import org.koin.dsl.module
import pt.nunomatos.fitbodapp.data.repository.FitbodRepository

val repositoryModule = module {
    single { FitbodRepository() }
}