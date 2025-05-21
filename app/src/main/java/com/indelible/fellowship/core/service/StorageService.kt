package com.indelible.fellowship.core.service

import android.net.Uri

interface StorageService {
    suspend fun uploadFile(uri: Uri, path: String, onComplete:(String) -> Unit)
}