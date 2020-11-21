package com.smerkis.weamther.diTest

import com.smerkis.weamther.repository.image.ImageRepo
import com.smerkis.weamther.repository.image.PaperImageRepo
import kotlinx.coroutines.FlowPreview
import org.koin.dsl.bind
import org.koin.dsl.module

@FlowPreview
fun imageRepoMockedModule() = module {
    single { PaperImageRepo(get()) } bind (ImageRepo::class)
}