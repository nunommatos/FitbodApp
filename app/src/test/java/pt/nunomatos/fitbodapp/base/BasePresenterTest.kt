package pt.nunomatos.fitbodapp.base

import org.junit.Before
import pt.nunomatos.fitbodapp.utils.TestsHelper

abstract class BasePresenterTest<P>: BaseTest() {

    protected var presenter: P? = null

    @Before
    @Throws(Exception::class)
    fun setUp() {
        TestsHelper.mockRxJavaSchedulers()
        presenter = buildPresenter()
        attachViewToPresenter()
    }

    protected abstract fun attachViewToPresenter()

    protected abstract fun buildPresenter(): P
}