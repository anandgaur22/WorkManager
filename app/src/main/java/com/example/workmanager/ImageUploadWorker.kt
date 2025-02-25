package com.example.workmanager

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import kotlinx.coroutines.delay
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class ImageUploadWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val imageUriString = inputData.getString("image_uri")

        return try {
            if (imageUriString != null) {
                val imageUri = Uri.parse(imageUriString)
                // Simulate image uploading process
                delay(2000)  // Simulate network latency
                val result = simulateUpload(imageUri) // Simulate the upload
                val outputData = Data.Builder()
                    .putString("result", result)
                    .build()
                Result.success(outputData)
            } else {
                Result.failure(Data.Builder().putString("result", "Image URI is null").build())
            }
        } catch (e: Exception) {
            Log.e("ImageUploadWorker", "Error uploading image", e)
            Result.failure(Data.Builder().putString("result", "Upload failed: ${e.message}").build())
        }
    }

    private fun simulateUpload(imageUri: Uri): String {
        // Simulate uploading the image by copying it to a temporary file
        val inputStream = applicationContext.contentResolver.openInputStream(imageUri) ?: throw IOException("Cannot open input stream")
        val outputFile = File(applicationContext.cacheDir, "uploaded_image_${UUID.randomUUID()}.jpg")
        val outputStream = FileOutputStream(outputFile)

        inputStream.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }

        return "Image uploaded successfully to ${outputFile.absolutePath}"
    }
}