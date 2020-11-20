package com.smerkis.weamther

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.smerkis.weamther.repository.image.ImageRepo
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import org.junit.Assert.*
import org.junit.Test

private const val TEST_URL = "https://live.staticflickr.com/424/19743825518_f4aa88de9b.jpg"

class ImageRepoInstrumentalTest : BaseInstrumentalTest() {

    lateinit var imageRepo: ImageRepo

    private val bitmapMock = Bitmap.createBitmap(500, 255, Bitmap.Config.ARGB_8888)

    override fun setup() {
        super.setup()
    }

    @Test
    fun get_photo_list_from_flickr_ok() {
        mockResponse()
        runBlocking {
            imageRepo.getPhotoListFromFlickr(TEST_CITY).collect {
                assertEquals(2, it?.photos?.photo?.size)
            }
        }
    }


    @Test
    fun write_to_cahce_ok() {
        mockResponse()
        runBlocking {
            imageRepo.writeToCache(bitmapMock, TEST_CITY).collect {
                assertEquals(true, it)
            }
        }
    }

    @Test
    fun get_image_file_from_cahce_ok() {
        mockResponse()
        runBlocking {
            imageRepo.getImageFileFromCache(TEST_CITY).collect {
                val bitmap = BitmapFactory.decodeFile(it?.absolutePath)
                assertEquals(bitmap.width, bitmapMock.width)
            }
        }
    }

    @Test
    fun get_random_photo_ok() {
        mockResponse()
        runBlocking {
            imageRepo.getRandomPhotoUrl(TEST_CITY).collect {

                assertEquals(it, TEST_URL)
            }
        }
    }

    override fun createResponse(): MockResponse {
        return getResponse(
            """{
    "photos": {
        "page": 1,
        "pages": 5599,
        "perpage": 2,
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
        )
    }

}