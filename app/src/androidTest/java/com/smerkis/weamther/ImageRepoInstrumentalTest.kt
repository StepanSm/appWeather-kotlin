package com.smerkis.weamther

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.smerkis.weamther.diTest.imageRepoMockedModule
import com.smerkis.weamther.diTest.networkMockedComponent
import com.smerkis.weamther.repository.image.BOOK_IMAGES
import com.smerkis.weamther.repository.image.ImageRepo
import io.paperdb.Paper
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.test.inject
import retrofit2.HttpException
import java.net.HttpURLConnection
import java.util.*

private const val TEST_URL = "https://live.staticflickr.com/424/19743825518_f4aa88de9b.jpg"
private const val PERPAGE_TEST = 2

@FlowPreview
class ImageRepoInstrumentalTest : BaseInstrumentalTest() {

    private val imageRepo: ImageRepo by inject()

    private val bitmapMock = Bitmap.createBitmap(500, 255, Bitmap.Config.ARGB_8888)

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
    fun get_random_photoUrl() {
        mockResponse()
        runBlocking {
            imageRepo.downloadImage(TEST_CITY).let {
                assertNotNull(it)
                assertTrue(
                    Regex(pattern = "(https?://(?:www\\.|(?!www))[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|www\\.[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|https?://(?:www\\.|(?!www))[a-zA-Z0-9]+\\.[^\\s]{2,}|www\\.[a-zA-Z0-9]+\\.[^\\s]{2,})").matches(
                        it.toString()
                    )
                )
            }
        }
    }


    @Test
    fun write_to_cahce_ok() {
        mockResponse()
        runBlocking {
            imageRepo.writeToCache(bitmapMock, TEST_CITY)
            val imagePath =
                Paper.book(BOOK_IMAGES).read<String?>(city.toLowerCase(Locale.getDefault()).trim())
            }
        }


    @Test
    fun get_image_file_from_cahce_ok() {
        mockResponse()
        runBlocking {
            imageRepo.getImageFileFromCache(TEST_CITY).collect {
                val bitmap = BitmapFactory.decodeFile(it?.absolutePath)
                assertNotNull(it)
                assertEquals(bitmap.width, bitmapMock.width)
            }
        }
    }

    @Test
    fun get_random_photo_ok() {
        mockResponse()
        runBlocking {
            imageRepo.downloadImage(TEST_CITY).collect {
                assertNotNull(it)
                assertEquals(it, TEST_URL)
            }
        }
    }

    @Test(expected = HttpException::class)
    fun get_photo_list_from_flickr_error() {
        mockResponse(HttpURLConnection.HTTP_BAD_REQUEST)
        runBlocking {
            imageRepo.getPhotoListFromFlickr(TEST_CITY).collect {
                assertEquals(it?.photos?.photo?.isNullOrEmpty(), true)
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
                "title": "Kurgan",
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
                "title": "Kurgan",
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