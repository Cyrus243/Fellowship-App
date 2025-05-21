package com.indelible.fellowship.core.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.GsonBuilder
import com.indelible.fellowship.core.service.NotificationRepository
import com.indelible.fellowship.core.service.NotificationServiceApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FireBaseModule{
    @Provides fun auth(): FirebaseAuth = FirebaseAuth.getInstance()
    @Provides fun fireStore(): FirebaseFirestore = FirebaseFirestore.getInstance()
    @Provides fun fireStorage(): FirebaseStorage = FirebaseStorage.getInstance()
    @Provides fun fireCloudMessaging(): FirebaseMessaging = FirebaseMessaging.getInstance()

    @Provides
    @Singleton
    fun provideGsonConverterFactory(): GsonConverterFactory =
        GsonConverterFactory.create(
            GsonBuilder()
                .setLenient()
                .create()
        )

    @Provides
    @Singleton
    fun provideCloudMessagingApi(factory: GsonConverterFactory): NotificationServiceApi =
        Retrofit.Builder()
            .baseUrl(NotificationServiceApi.BASE_URL)
            .addConverterFactory(factory)
            .build()
            .create(NotificationServiceApi::class.java)

    @Provides
    @Singleton
    fun provideCloudMessagingRepository(
        api: NotificationServiceApi
    ): NotificationRepository = NotificationRepository(api)
}