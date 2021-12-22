package pt.nunomatos.fitbodapp.utils

import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers

class TestsHelper {

    companion object {

        fun mockRxJavaSchedulers() {
            RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
            RxAndroidPlugins.initMainThreadScheduler { Schedulers.trampoline() }
        }
    }
}