package pt.nunomatos.fitbodapp.base

import io.reactivex.disposables.CompositeDisposable

abstract class BasePresenter<V : BaseContract.View> : BaseContract.Presenter<V> {

    protected var view: V? = null
    protected val disposables = CompositeDisposable()

    override fun attachView(v: V) {
        view = v
    }

    override fun detachView() {
        view = null
        disposables.clear()
    }
}