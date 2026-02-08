package com.app.sampleproject.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.automirrored.outlined.Message
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.FlightTakeoff
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.app.sampleproject.presentation.login.LoginScreen
import com.app.sampleproject.presentation.messages.MessagesScreen
import com.app.sampleproject.presentation.profile.ProfileScreen
import com.app.sampleproject.presentation.tripdetails.TripDetailsScreen
import com.app.sampleproject.presentation.trips.TripsScreen

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Messages : Screen("messages")
    object Trips : Screen("trips")
    object Profile : Screen("profile")
    object TripDetails : Screen("trip_details/{packageId}/{bookingId}/{orderId}") {
        fun createRoute(packageId: Int?, bookingId: Int?, orderId: String?): String {
            return "trip_details/${packageId ?: 0}/${bookingId ?: 0}/${orderId ?: "null"}"
        }
    }
}

data class BottomNavItem(
    val route: String,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

val bottomNavItems = listOf(
    BottomNavItem(
        route = Screen.Messages.route,
        title = "Messages",
        selectedIcon = Icons.AutoMirrored.Filled.Message,
        unselectedIcon = Icons.AutoMirrored.Outlined.Message
    ),
    BottomNavItem(
        route = Screen.Trips.route,
        title = "Trips",
        selectedIcon = Icons.Filled.FlightTakeoff,
        unselectedIcon = Icons.Outlined.FlightTakeoff
    ),
    BottomNavItem(
        route = Screen.Profile.route,
        title = "Profile",
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person
    )
)

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Login.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Messages.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Messages.route) {
            MainScreen(navController = navController, startRoute = Screen.Messages.route)
        }

        composable(Screen.Trips.route) {
            MainScreen(navController = navController, startRoute = Screen.Trips.route)
        }

        composable(Screen.Profile.route) {
            MainScreen(navController = navController, startRoute = Screen.Profile.route)
        }

        composable(
            route = Screen.TripDetails.route,
            arguments = listOf(
                navArgument("packageId") { type = NavType.IntType },
                navArgument("bookingId") { type = NavType.IntType },
                navArgument("orderId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val packageId = backStackEntry.arguments?.getInt("packageId")
            val bookingId = backStackEntry.arguments?.getInt("bookingId")
            val orderId = backStackEntry.arguments?.getString("orderId")

            TripDetailsScreen(
                packageId = if (packageId == 0) null else packageId,
                bookingId = if (bookingId == 0) null else bookingId,
                orderId = if (orderId == "null") null else orderId,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}

@Composable
fun MainScreen(
    navController: NavHostController,
    startRoute: String
) {
    val mainNavController = rememberNavController()
    val navBackStackEntry by mainNavController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                contentColor = Color.Black,
                tonalElevation = 0.dp
            ) {
                bottomNavItems.forEach { item ->
                    val selected = currentDestination?.hierarchy?.any { it.route == item.route } == true

                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            mainNavController.navigate(item.route) {
                                popUpTo(mainNavController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                                contentDescription = item.title,
                                tint = if (selected) Color(0xFFFF6600) else Color(0xFF666666)
                            )
                        },
                        label = {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = item.title,
                                    fontSize = 12.sp,
                                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (selected) Color(0xFFFF6600) else Color(0xFF666666)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Box(
                                    modifier = Modifier
                                        .width(40.dp)
                                        .height(2.dp)
                                        .background(
                                            color = if (selected) Color(0xFFFF6600) else Color.Transparent,
                                            shape = RoundedCornerShape(1.dp)
                                        )
                                )
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color(0xFFFF6600),
                            selectedTextColor = Color(0xFFFF6600),
                            unselectedIconColor = Color(0xFF666666),
                            unselectedTextColor = Color(0xFF666666),
                            indicatorColor = Color.Transparent
                        )
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = mainNavController,
            startDestination = startRoute,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Screen.Messages.route) {
                MessagesScreen()
            }

            composable(Screen.Trips.route) {
                TripsScreen(
                    onLogout = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    onTripClick = { packageId, bookingId, orderId ->
                        navController.navigate(
                            Screen.TripDetails.createRoute(packageId, bookingId, orderId)
                        )
                    }
                )
            }

            composable(Screen.Profile.route) {
                ProfileScreen(
                    onLogout = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}
