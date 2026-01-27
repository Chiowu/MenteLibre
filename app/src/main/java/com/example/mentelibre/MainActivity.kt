package com.example.mentelibre

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.*
import com.example.mentelibre.data.local.AppDatabase
import com.example.mentelibre.data.mood.MoodRepository
import com.example.mentelibre.data.session.SessionManager
import com.example.mentelibre.ui.auth.LoginScreen
import com.example.mentelibre.ui.auth.RegisterScreen
import com.example.mentelibre.ui.home.HomeScreen
import com.example.mentelibre.ui.home.HomeViewModel
import com.example.mentelibre.ui.home.HomeViewModelFactory
import com.example.mentelibre.ui.home.SecurityScreen
import com.example.mentelibre.ui.home.SettingsScreen
import com.example.mentelibre.ui.mood.MoodRegisterScreen
import com.example.mentelibre.ui.profile.ProfileScreen
import com.example.mentelibre.ui.splash.SplashScreen
import com.example.mentelibre.ui.theme.MenteLibreTheme

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
    val session = remember { SessionManager(context) }


    val repository = remember {
        MoodRepository(
            AppDatabase.getDatabase(context).moodDao()
        )
    }

    val homeViewModel: HomeViewModel = viewModel(
        factory = HomeViewModelFactory(repository)
    )

    val moodToday by homeViewModel.todayMood.collectAsState()

    // 🔥 ESCUCHA CUANDO VOLVEMOS DESDE REGISTRAR ÁNIMO
    val savedStateHandle =
        navController.currentBackStackEntry?.savedStateHandle
    val shouldRefresh by savedStateHandle
        ?.getStateFlow("mood_saved", false)
        ?.collectAsState() ?: remember { mutableStateOf(false) }

    LaunchedEffect(shouldRefresh) {
        if (shouldRefresh) {
            homeViewModel.loadTodayMood()
            savedStateHandle?.set("mood_saved", false)
        }
    }
    // Decidir destino inicial según sesión
    val startDestination = if (session.getUserId() != null) "home" else "splash"

    Scaffold(
        bottomBar = {
            val currentRoute =
                navController.currentBackStackEntryAsState().value?.destination?.route

            if (currentRoute !in listOf("login", "register")) {
                BottomNavigationBar(navController)
            }
        }
    ) { paddingValues ->

        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(paddingValues)
        ) {
            // SPLASH
            composable("splash") {
                SplashScreen(
                    onFinish = {
                        navController.navigate("register") {
                            popUpTo("splash") { inclusive = true }
                        }
                    },
                    onGoLogin = {
                        navController.navigate("login") {
                            popUpTo("splash") { inclusive = true }
                        }
                    }
                )
            }

            // LOGIN
            composable("login") {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate("home") {
                            popUpTo("login") { inclusive = true }
                        }
                    },
                    onRegister = {
                        navController.navigate("register")
                    }
                )
            }

            // REGISTER
            composable("register") {
                RegisterScreen(
                    onGoLogin = {
                        navController.navigate("login") {
                            popUpTo("register") { inclusive = true }
                        }
                    },
                    onGoHome = {
                        navController.navigate("home") {
                            popUpTo("register") { inclusive = true }
                        }
                    }
                )
            }

            composable("home") {
                HomeScreen(
                    moodToday = moodToday,
                    onGoProfile = { navController.navigate("profile") },
                    onRefresh = { homeViewModel.loadTodayMood() },
                    onGoMood = { navController.navigate("mood_register") }
                )
            }

            // ⭐ REGISTRAR ÁNIMO (AQUÍ ESTÁ LA MAGIA)
            composable("mood_register") {
                MoodRegisterScreen(
                    repository = repository,
                    onBack = {
                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set("mood_saved", true)

                        navController.popBackStack()
                    }
                )
            }

            // PERFIL
            composable("profile") {
                ProfileScreen(onBack = { navController.popBackStack() })
            }
            composable("history") { Text("Pantalla Historial", Modifier.padding(24.dp)) }
            composable("diary") { Text("Pantalla Diario", Modifier.padding(24.dp)) }
            // AJUSTES
            composable("settings") {
                SettingsScreen(
                    onBack = { navController.popBackStack() },
                    onProfile = { navController.navigate("profile") },
                    onSecurity = { navController.navigate("security") },
                    onSupport = { /* navegar a soporte */ },
                    onSuggestions = { /* navegar a sugerencias */ },
                    onDeleteAccount = {
                        val sessionManager = SessionManager(context)
                        sessionManager.clearSession()
                        AppDatabase.getDatabase(context).clearAllTables()
                        navController.navigate("register") { popUpTo(0) { inclusive = true } }
                    },
                    onLogout = {
                        val sessionManager = SessionManager(context)
                        sessionManager.clearSession() // Borra la sesión
                        navController.navigate("register") { popUpTo(0) { inclusive = true } }
                    }
                )
            }

            // AJUSTES -> SEGURIDAD
            composable("security") {
                SecurityScreen(
                    onBack = { navController.popBackStack() },
                    onChangePassword = {
                        // Aquí puedes navegar a otra pantalla de cambiar contraseña si existe
                        navController.navigate("change_password")
                    }
                )
            }
        }
    }
}



@Composable
fun BottomNavigationBar(navController: NavController) {

    val items = listOf(
        BottomNavItem("home", "Inicio", Icons.Filled.Home),
        BottomNavItem("history", "Historial", Icons.Filled.BarChart),
        BottomNavItem("diary", "Diario", Icons.Filled.Book),
        BottomNavItem("settings", "Ajustes", Icons.Filled.Settings)
    )

    val currentRoute =
        navController.currentBackStackEntryAsState().value?.destination?.route

    NavigationBar(containerColor = Color(0xFFFFF4F8)) {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo("home")
                        launchSingleTop = true
                    }
                },
                icon = { Icon(item.icon, contentDescription = null) },
                label = { Text(item.label, fontSize = 12.sp) }
            )
        }
    }
}

data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)
