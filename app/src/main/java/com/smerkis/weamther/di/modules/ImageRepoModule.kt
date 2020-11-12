package com.smerkis.weamther.di.modules

import com.smerkis.weamther.api.ApiFactory
import com.smerkis.weamther.repository.image.ImageRepo
import com.smerkis.weamther.repository.image.PaperImageRepo
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Singleton
@Module(includes = [ApiFactoryModule::class])
class ImageRepoModule {

    @Singleton
    @Provides
    fun getImageRepo(@Named("paper") repo: ImageRepo): ImageRepo = repo

    @Provides
    @Named("paper")
    fun getPaperImageRepo(apiFactory: ApiFactory): ImageRepo = PaperImageRepo(apiFactory)
}