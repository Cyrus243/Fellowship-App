package com.indelible.fellowship.core.service.impl

import android.net.Uri
import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import com.indelible.fellowship.core.service.StorageService
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class StorageServiceImpl @Inject constructor(
    private val fireStorage: FirebaseStorage,
): StorageService {

    override suspend fun uploadFile(
        uri: Uri,
        path: String,
        onComplete: (String) -> Unit
    ) {
        val reference  = fireStorage.reference.child(path)

        reference.putFile(uri).continueWith {
            reference.downloadUrl.addOnCompleteListener {
                onComplete(it.result.toString())
                Log.d("AccountService", "uploadFile: ${it.result.toString()}")
            }
        }.await()
    }
}