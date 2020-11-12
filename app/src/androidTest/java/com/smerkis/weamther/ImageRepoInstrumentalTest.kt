package com.smerkis.weamther

import okhttp3.mockwebserver.MockResponse

class ImageRepoInstrumentalTest : BaseInstrumentalTest() {


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
                "url_m": "https://live.staticflickr.com/7391/10078957545_158914608e.jpg",
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
                "url_m": "https://live.staticflickr.com/7403/10078957355_879e22011b.jpg",
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