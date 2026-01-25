package com.example.mentelibre

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mentelibre.data.local.AppDatabase
import com.example.mentelibre.data.mood.MoodRepository
import com.example.mentelibre.data.mood.MoodResult
import com.example.mentelibre.ui.auth.LoginScreen
import com.example.mentelibre.ui.auth.RegisterScreen
import com.example.mentelibre.ui.history.HistoryScreen
import com.example.mentelibre.ui.home.HomeScreen
import com.example.mentelibre.ui.mood.MoodRegisterScreen
import com.example.mentelibre.ui.splash.SplashScreen
import com.example.mentelibre.ui.theme.MenteLibreTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MenteLibreTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // ---------- DATABASE & REPOSITORY ----------
    val db = remember { AppDatabase.getDatabase(context) }
    val moodRepository = remember { MoodRepository(db.moodDao()) }

    // ---------- ESTADOS GLOBALES ----------
    var moodToday by remember { mutableStateOf<MoodResult?>(null) }
    var history by remember { mutableStateOf(emptyList<MoodResult>()) }

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {

        // ---------- SPLASH ----------
        composable("splash") {
            SplashScreen(
                onFinish = {
                    navController.navigate("login") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }

        // ---------- LOGIN ----------
        composable("login") {
            LoginScreen(
                onLogin = { _, _ ->
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onRegister = {
                    navController.navigate("register")
                }
            )
        }

        // ---------- REGISTER ----------
        composable("register") {
            RegisterScreen(
                onGoLogin = {
                    navController.popBackStack()
                }
            )
        }

        // ---------- HOME ----------
        composable("home") {

            LaunchedEffect(Unit) {
                moodToday = moodRepository.getTodayMood()
            }

            HomeScreen(
                onGoRegisterMood = {
                    navController.navigate("mood")
                },
                onGoDiary = {
                    navController.navigate("diary")
                },
                onGoHistory = {
                    scope.launch {
                        history = moodRepository.getAllMoods()
                        navController.navigate("history")
                    }
                },
                moodToday = moodToday
            )
        }

        // ---------- REGISTRAR ÁNIMO ----------
        composable("mood") {
            MoodRegisterScreen(
                onSaved = {
                    scope.launch {
                        moodToday = moodRepository.getTodayMood()
                        navController.popBackStack()
                    }
                }
            )
        }

        // ---------- HISTORIAL ----------
        composable("history") {
            HistoryScreen(
                history = history,
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        // ---------- DIARIO (PRÓXIMO PASO) ----------
        composable("diary") {
            // DiaryScreen() → lo hacemos después
        }
    }
}
