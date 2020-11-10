package com.smerkis.weamther

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.smerkis.weamther.di.DaggerTestComponent
import com.smerkis.weamther.di.TestComponent
import com.smerkis.weamther.di.modules.AppModule
import org.junit.*
import org.junit.runner.RunWith
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
class ModuleTest {

    companion object {
        @BeforeClass
        @JvmStatic
        fun setup() {

        }

        @AfterClass
        @JvmStatic
        fun tearDown() {
        }
    }

    @Inject
    lateinit var app: MyApp


    @Before
    fun before() {
        val testComponent: TestComponent =
            DaggerTestComponent.builder().appModule(AppModule(MyApp.instance)).build()
        testComponent.inject(this)
    }

    @After
    fun after() {
    }

    @Test
    fun appName() {
        Assert.assertEquals(app.appName, "weaMther")
    }
}
