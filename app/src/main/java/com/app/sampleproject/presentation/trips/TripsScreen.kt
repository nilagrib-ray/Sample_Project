package com.app.sampleproject.presentation.trips

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.sampleproject.presentation.trips.components.EmptyStateCard
import com.app.sampleproject.presentation.trips.components.DestinationCard
import com.app.sampleproject.presentation.trips.components.SectionHeader
import com.app.sampleproject.presentation.trips.components.UpcomingTripCard


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripsScreen(
    viewModel: TripsViewModel = hiltViewModel(),
    onLogout: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

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
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
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
                .background(Color(0xFFFAFAFA))
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
                        if (uiState.destinations.isEmpty()) {
                            EmptyStateCard("No next trips available")
                        } else {
                            LazyRow(
                                contentPadding = PaddingValues(horizontal = 20.dp),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                items(uiState.destinations) { trip ->
                                    DestinationCard(trip)
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
                        containerColor = Color(0xFFF5F5F5)
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