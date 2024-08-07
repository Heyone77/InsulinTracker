package com.heysoft.insulintracker

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.heysoft.insulintracker.network.ApiService
import com.heysoft.insulintracker.ui.screens.MainScreen
import com.heysoft.insulintracker.ui.theme.InsulinTrackerTheme
import com.heysoft.insulintracker.utils.addUser
import com.heysoft.insulintracker.utils.getDeviceUUID
import com.heysoft.insulintracker.viewmodel.SharedViewModel
import com.heysoft.insulintracker.workers.NotificationWorker
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var apiService: ApiService

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Do something when the permission is granted
        } else {
            // Do something when the permission is denied
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        NotificationWorker.createNotificationChannel(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        setContent {
            val sharedViewModel: SharedViewModel = hiltViewModel()
            val isDarkTheme by sharedViewModel.isDarkTheme.collectAsState()

            Log.d("MainActivity", "isDarkTheme: $isDarkTheme")

            InsulinTrackerTheme(
                isDarkTheme = isDarkTheme
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }

        val deviceUUID = getDeviceUUID(this)
        Log.e("MainActivity", "ID: $deviceUUID")
        addUser(apiService, deviceUUID)
    }
}
