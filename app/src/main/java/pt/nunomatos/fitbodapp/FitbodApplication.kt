package pt.nunomatos.fitbodapp

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext
import pt.nunomatos.fitbodapp.data.repository.di.repositoryModule
import pt.nunomatos.fitbodapp.ui.di.uiModule

class FitbodApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        GlobalContext.startKoin {
            androidContext(this@FitbodApplication)
            modules(uiModule, repositoryModule)
        }
    }
}