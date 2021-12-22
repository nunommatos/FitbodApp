package pt.nunomatos.fitbodapp.base

import android.content.Context
import org.junit.Before
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

abstract class BaseTest {

    @Mock
    protected val context = Mockito.mock(Context::class.java)

    @Before
    fun initMockitoAnnotations() {
        MockitoAnnotations.initMocks(this)
    }
}