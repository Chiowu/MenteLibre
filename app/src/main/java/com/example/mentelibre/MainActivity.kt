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
import com.example.mentelibre.ui.auth.LoginScreen
import com.example.mentelibre.ui.auth.RegisterScreen
import com.example.mentelibre.ui.home.HomeScreen
import com.example.mentelibre.ui.home.HomeViewModel
import com.example.mentelibre.ui.home.HomeViewModelFactory
import com.example.mentelibre.ui.profile.ProfileScreen
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

    // Repository Room
    val repository = remember {
        MoodRepository(
            AppDatabase.getDatabase(context).moodDao()
        )
    }

    // ViewModel Home
    val homeViewModel: HomeViewModel = viewModel(
        factory = HomeViewModelFactory(repository)
    )

    val moodToday by homeViewModel.todayMood.collectAsState()

    LaunchedEffect(Unit) {
        homeViewModel.loadTodayMood()
    }

    Scaffold(
        bottomBar = {
            val currentRoute =
                navController.currentBackStackEntryAsState().value?.destination?.route

            // ❌ No mostrar bottom bar en login / register
            if (currentRoute !in listOf("login", "register")) {
                BottomNavigationBar(navController)
            }
        }
    ) { paddingValues ->

        NavHost(
            navController = navController,
            startDestination = "login",
            modifier = Modifier.padding(paddingValues)
        ) {

            // 🔐 LOGIN
            composable("login") {
                LoginScreen(
                    onLogin = { email, password ->
                        // aquí luego puedes validar con BD si quieres
                        navController.navigate("home") {
                            popUpTo("login") { inclusive = true }
                        }
                    },
                    onRegister = {
                        navController.navigate("register")
                    }
                )
            }

            // 📝 REGISTER
            composable("register") {
                RegisterScreen(
                    onGoLogin = {
                        navController.navigate("login") {
                            popUpTo("register") { inclusive = true }
                        }
                    }
                )
            }

            // 🏠 HOME
            composable("home") {
                HomeScreen(
                    moodToday = moodToday,
                    onGoProfile = {
                        navController.navigate("profile")
                    }
                )
            }

            // 👤 PERFIL
            composable("profile") {
                ProfileScreen()
            }

            // 📊 HISTORIAL
            composable("history") {
                Text("Pantalla Historial", modifier = Modifier.padding(24.dp))
            }

            // 📓 DIARIO
            composable("diary") {
                Text("Pantalla Diario", modifier = Modifier.padding(24.dp))
            }

            // ⚙️ AJUSTES
            composable("settings") {
                Text("Pantalla Ajustes", modifier = Modifier.padding(24.dp))
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

    NavigationBar(
        containerColor = Color(0xFFFFF4F8),
        tonalElevation = 8.dp
    ) {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo("home")
                        launchSingleTop = true
                    }
                },
                icon = {
                    Icon(item.icon, contentDescription = item.label)
                },
                label = {
                    Text(item.label, fontSize = 12.sp)
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF8C2F45),
                    selectedTextColor = Color(0xFF8C2F45),
                    unselectedIconColor = Color(0xFFB8A7AE),
                    unselectedTextColor = Color(0xFFB8A7AE),
                    indicatorColor = Color(0xFFF6D8E8)
                )
            )
        }
    }
}

data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)
