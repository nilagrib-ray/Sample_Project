package com.app.sampleproject.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.app.sampleproject.domain.model.TripDomain
import com.app.sampleproject.presentation.viewmodel.TripsViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripsScreen(
    viewModel: TripsViewModel = hiltViewModel(),
    onLogout: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    var showLogoutDialog by remember { mutableStateOf(false) }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Logout") },
            text = { Text("Are you sure you want to logout?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                        viewModel.logout()
                        onLogout()
                    }
                ) {
                    Text("Yes", color = Color(0xFFFF6600))
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Trips",
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp,
                        color = Color.White
                    )
                },
                actions = {
                    IconButton(onClick = { viewModel.loadAllTrips() }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh",
                            tint = Color.White
                        )
                    }
                    IconButton(onClick = { showLogoutDialog = true }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Logout,
                            contentDescription = "Logout",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFFFF8F0))
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Color(0xFFFF6600)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 20.dp)
                ) {
                    item {
                        SectionHeader("Upcoming Trips")
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    if (uiState.upcomingTrips.isEmpty()) {
                        item {
                            EmptyStateCard("No upcoming trips found")
                            Spacer(modifier = Modifier.height(24.dp))
                        }
                    } else {
                        items(uiState.upcomingTrips) { trip ->
                            UpcomingTripCard(trip)
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                        item { Spacer(modifier = Modifier.height(8.dp)) }
                    }

                    item {
                        SectionHeader("Next Trip")
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    item {
                        if (uiState.nextTrips.isEmpty()) {
                            EmptyStateCard("No next trips available")
                        } else {
                            LazyRow(
                                contentPadding = PaddingValues(horizontal = 20.dp),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                items(uiState.nextTrips) { trip ->
                                    NextTripCard(trip)
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                    }

                    item {
                        SectionHeader("Previous Trips")
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    if (uiState.previousTrips.isEmpty()) {
                        item {
                            EmptyStateCard("You don't have any previous trips")
                        }
                    } else {
                        items(uiState.previousTrips) { trip ->
                            UpcomingTripCard(trip, showDaysToGo = false)
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
            }

            if (uiState.errorMessage != null) {
                Card(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFCE4EC)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = uiState.errorMessage!!,
                        color = Color(0xFFD32F2F),
                        modifier = Modifier.padding(16.dp),
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Black,
        modifier = Modifier.padding(horizontal = 20.dp)
    )
}

@Composable
fun UpcomingTripCard(trip: TripDomain, showDaysToGo: Boolean = true) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box {
            Column {
                AsyncImage(
                    model = trip.tripImage ?: "https://via.placeholder.com/400x240",
                    contentDescription = trip.tripName,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)),
                    contentScale = ContentScale.Crop
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFFFF3E0))
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = formatDate(trip.startDate),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.Black
                            )
                            Text(
                                text = formatYear(trip.startDate),
                                fontSize = 16.sp,
                                color = Color.Gray
                            )
                        }

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "to",
                            tint = Color(0xFFFF6600),
                            modifier = Modifier.size(28.dp)
                        )

                        Column {
                            Text(
                                text = formatDate(trip.endDate),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.Black
                            )
                            Text(
                                text = formatYear(trip.endDate),
                                fontSize = 16.sp,
                                color = Color.Gray
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    HorizontalDivider(color = Color(0xFFE0E0E0), thickness = 1.dp)

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = trip.tripName,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )
                }
            }

            if (!trip.location.isNullOrEmpty()) {
                Card(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White.copy(alpha = 0.5f)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = trip.location,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )
                }
            }

            if (showDaysToGo) {
                val daysToGo = calculateDaysToGo(trip.startDate)
                if (daysToGo > 0) {
                    Card(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White.copy(alpha = 0.5f)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "$daysToGo days to go!",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NextTripCard(trip: TripDomain) {
    Card(
        modifier = Modifier.width(280.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            if (!trip.location.isNullOrEmpty()) {
                Text(
                    text = trip.location,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            AsyncImage(
                model = trip.tripImage ?: "https://via.placeholder.com/280x200",
                contentDescription = trip.tripName,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = formatDate(trip.startDate),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )
                    Text(
                        text = formatYear(trip.startDate),
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "to",
                    tint = Color(0xFFFF6600),
                    modifier = Modifier.size(24.dp)
                )

                Column {
                    Text(
                        text = formatDate(trip.endDate),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )
                    Text(
                        text = formatYear(trip.endDate),
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun EmptyStateCard(message: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFFFFF)
        )
    ) {
        Text(
            text = message,
            modifier = Modifier.padding(24.dp),
            fontSize = 16.sp,
            color = Color.Black,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Italic
        )
    }
}

fun formatDate(dateString: String): String {
    return try {
        val parts = dateString.split("-")
        if (parts.size == 3) {
            val day = parts[2]
            val month = when (parts[1]) {
                "01" -> "January"
                "02" -> "February"
                "03" -> "March"
                "04" -> "April"
                "05" -> "May"
                "06" -> "June"
                "07" -> "July"
                "08" -> "August"
                "09" -> "September"
                "10" -> "October"
                "11" -> "November"
                "12" -> "December"
                else -> parts[1]
            }
            "$day $month"
        } else {
            dateString
        }
    } catch (_: Exception) {
        dateString
    }
}

fun formatYear(dateString: String): String {
    return try {
        val parts = dateString.split("-")
        if (parts.size == 3) parts[0] else dateString
    } catch (_: Exception) {
        dateString
    }
}

fun calculateDaysToGo(dateString: String): Int {
    return try {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val tripDate = format.parse(dateString)
        val currentDate = Date()

        if (tripDate != null && tripDate.after(currentDate)) {
            val diff = tripDate.time - currentDate.time
            TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS).toInt()
        } else {
            0
        }
    } catch (_: Exception) {
        0
    }
}