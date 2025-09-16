package com.example.travelapp2

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.travelapp2.ui.theme.TravelApp2Theme
import org.osmdroid.config.Configuration
import com.example.travelapp2.presentation.login.LoginScreen
import com.example.travelapp2.presentation.registration.RegistrationScreen
import com.example.travelapp2.navigation.Main
import dagger.hilt.android.AndroidEntryPoint
import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        Configuration.getInstance().load(this, getPreferences(MODE_PRIVATE))
        hideSystemNavigationBar(this)
        setContent {
            TravelApp2Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                101
            )
        }
    }


    @Composable
    fun AppNavigation() {
        val navController = rememberNavController()
        val context = LocalContext.current

        val permissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { isGranted ->
                if (!isGranted) {
                    Toast.makeText(context, "Разрешение на геолокацию не предоставлено", Toast.LENGTH_LONG).show()
                }
            }
        )

        LaunchedEffect(Unit) {
            val hasPermission = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

            if (!hasPermission) {
                permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }

        val sharedPreferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("is_logged_in", false)
        val startDestination = if (isLoggedIn) "home" else "login"

        NavHost(navController = navController, startDestination = startDestination) {
            composable("login") {
                LoginScreen(navController = navController)
            }
            composable("registration") {
                RegistrationScreen(navController = navController)
            }
            composable("home") {
                Main(navController)
            }
        }
    }


}


@Suppress("DEPRECATION")
private fun hideSystemNavigationBar(activity: ComponentActivity) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        activity.window.insetsController?.let {
            it.hide(WindowInsets.Type.navigationBars())
            it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    } else {
        activity.window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                )
    }
}
