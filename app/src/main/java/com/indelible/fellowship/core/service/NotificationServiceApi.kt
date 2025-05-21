package com.indelible.fellowship.core.service

import com.indelible.fellowship.core.model.UserNotification
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface NotificationServiceApi {

    @POST("/clients/{registrationToken}")
    @Headers("Content-Type: $CONTENT_TYPE")
    suspend fun sendNotification(
        @Body notification: UserNotification,
        @Path("registrationToken") registrationToken: String
    ): Response<String>
    companion object{
        private const val CONTENT_TYPE = "application/json"
        const val BASE_URL = "http://sololaserver-env.eba-f8xx6tsg.us-east-2.elasticbeanstalk.com"
    }
}