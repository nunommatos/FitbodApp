package pt.nunomatos.fitbodapp.base

interface BaseContract {

    interface View

    interface Presenter<in V : View> {
        fun attachView(v: V)
        fun detachView()
    }
}