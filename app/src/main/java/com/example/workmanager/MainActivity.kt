package com.example.workmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.work.WorkInfo
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.work.*
import coil.compose.AsyncImage
import com.example.workmanager.ui.theme.WorkManagerTheme
import java.util.concurrent.TimeUnit


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SurfaceWithPeriodicWork()
        }
    }

    @Composable
    fun MainScreen() {
        val context = LocalContext.current
        var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            selectedImageUri = uri
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(onClick = {
                if (checkPermissions(context)) {
                    launcher.launch("image/*")
                } else {
                    requestPermissions(context)
                }

            }) {
                Text("Select Image")
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (selectedImageUri != null) {
                AsyncImage(
                    model = selectedImageUri,
                    contentDescription = "Selected Image",
                    modifier = Modifier.size(200.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {
                    startImageUploadWork(context, selectedImageUri!!)
                }) {
                    Text("Upload Image")
                }
            }
        }
    }

    private fun startImageUploadWork(context: Context, imageUri: Uri) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .build()

        val data = Data.Builder()
            .putString("image_uri", imageUri.toString())
            .build()

        val uploadWorkRequest = OneTimeWorkRequestBuilder<ImageUploadWorker>()
            .setConstraints(constraints)
            .setInputData(data)
            .setBackoffCriteria(
                BackoffPolicy.LINEAR,
                WorkRequest.MIN_BACKOFF_MILLIS, // Changed this line
                TimeUnit.MILLISECONDS
            )
            .build()

        WorkManager.getInstance(context).enqueue(uploadWorkRequest)

        WorkManager.getInstance(context).getWorkInfoByIdLiveData(uploadWorkRequest.id)
            .observeForever { workInfo ->
                if (workInfo != null && workInfo.state.isFinished) {
                    // Work completed
                    val result = workInfo.outputData.getString("result")
                    Toast.makeText(context, "Upload Result: $result", Toast.LENGTH_SHORT).show()
                }
            }
    }


    private fun checkPermissions(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun requestPermissions(context: Context) {
        // This is a simplified example. You should use ActivityResultContracts.RequestPermission for a cleaner approach.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            (context as MainActivity).requestPermissions(
                arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
                100
            )
        } else {
            (context as MainActivity).requestPermissions(
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                100
            )
        }
    }


    // periodic task
    @Composable
    fun SurfaceWithPeriodicWork() {
        val context = LocalContext.current

        // Using LaunchedEffect to ensure the work is scheduled only once when the composable is first launched
        LaunchedEffect(key1 = true) {
            startPeriodicSync(context)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Periodic Work Scheduled. Check Logcat.")

            Spacer(modifier = Modifier.padding(16.dp))

        }
    }

    private fun startPeriodicSync(context: android.content.Context) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val syncWorkRequest = PeriodicWorkRequestBuilder<DataSyncWorker>(
            repeatInterval = 3, // Every 3 hours
            repeatIntervalTimeUnit = TimeUnit.HOURS
        )
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "data_sync",  // Unique name
            ExistingPeriodicWorkPolicy.KEEP, // or REPLACE
            syncWorkRequest
        )
    }



    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        WorkManagerTheme {
            MainScreen()
        }
    }
}
