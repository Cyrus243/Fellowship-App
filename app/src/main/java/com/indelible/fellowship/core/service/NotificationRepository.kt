package com.indelible.fellowship.core.service


import com.indelible.fellowship.core.model.UserNotification
import retrofit2.Response

class NotificationRepository(
    private val api: NotificationServiceApi
) {
    suspend fun postNotification(
        notification: UserNotification,
        token: String
    ): Response<String> {
        return api.sendNotification(notification, token)
    }
}
