package com.smerkis.weamther

import android.graphics.Bitmap
import com.smerkis.weamther.api.ApiFactory
import com.smerkis.weamther.diTest.imageRepoMockedModule
import com.smerkis.weamther.diTest.networkMockedComponent
import com.smerkis.weamther.repository.image.ImageRepo
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.test.inject

private const val TEST_URL = "https://live.staticflickr.com/424/19743825518_f4aa88de9b.jpg"
private const val PERPAGE_TEST = 2

@FlowPreview
class ImageRepoInstrumentalTest : BaseInstrumentalTest() {

    private val imageRepo: ImageRepo by inject()
    private val apiFactory: ApiFactory by inject()

    override fun setup() {
        super.setup()

        startKoin {
            modules(
                listOf(
                    networkMockedComponent(webServer.url("/").toString()),
                    imageRepoMockedModule()
                )
            )
        }
    }


    @Test
    fun test_image_api() {
        mockResponse()
        runBlocking {
            val result = apiFactory.getImageApi().getImages(TEST_CITY)
            assertTrue(result.photos.photo[0].title.toLowerCase().contains(TEST_CITY.toLowerCase()))
            assertEquals(result.photos.photo[1].url_m, TEST_URL)
        }
    }

    @Test
    fun get_random_photoUrl() {
        mockResponse()
        runBlocking {
            imageRepo.downloadImage(TEST_CITY).collect {
                assertNotNull(it)
                assertTrue(
                    Regex(pattern = "(https?://(?:www\\.|(?!www))[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|www\\.[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|https?://(?:www\\.|(?!www))[a-zA-Z0-9]+\\.[^\\s]{2,}|www\\.[a-zA-Z0-9]+\\.[^\\s]{2,})").matches(
                        it
                    )
                )
            }
        }
    }


    override fun createResponse() =
        """{
    "photos": {
        "page": 1,
        "pages": 5599,
        "perpage": $PERPAGE_TEST,
        "total": "11197",
        "photo": [
            {
                "id": "10078957545",
                "owner": "104170484@N07",
                "secret": "158914608e",
                "server": "7391",
                "farm": 8,
                "title": "sdfs d NEW-YORK dsf",
                "ispublic": 1,
                "isfriend": 0,
                "isfamily": 0,
                "url_m": "$TEST_URL",
                "height_m": 334,
                "width_m": 500
            },
            {
                "id": "10078957355",
                "owner": "104170484@N07",
                "secret": "879e22011b",
                "server": "7403",
                "farm": 8,
                "title": "sdfs d new-york dsf",
                "ispublic": 1,
                "isfriend": 0,
                "isfamily": 0,
                "url_m": "$TEST_URL",
                "height_m": 176,
                "width_m": 500
            }
        ]
    },
    "stat": "ok"
}"""

}