package com.app.sampleproject.presentation.tripdetails

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material.icons.outlined.Hotel
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.app.sampleproject.presentation.tripdetails.components.ActionRequiredCard
import com.app.sampleproject.presentation.tripdetails.components.CountdownCard
import com.app.sampleproject.presentation.tripdetails.components.DestinationLinkCard
import com.app.sampleproject.presentation.tripdetails.components.DestinationMap
import com.app.sampleproject.presentation.tripdetails.components.LinkFriendsCard
import com.app.sampleproject.presentation.tripdetails.components.SectionHeaderRow
import com.app.sampleproject.presentation.tripdetails.components.TravellerCard
import com.app.sampleproject.presentation.tripdetails.components.formatAmount
import com.app.sampleproject.presentation.tripdetails.components.formatDateDayMonth
import com.app.sampleproject.presentation.tripdetails.components.formatDateNumeric
import com.app.sampleproject.presentation.tripdetails.components.formatDateYear

@Composable
fun TripDetailsScreen(
    packageId: Int?,
    bookingId: Int?,
    orderId: String?,
    onNavigateBack: () -> Unit,
    viewModel: TripDetailsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadTripDetails(packageId, bookingId, orderId)
    }

    Scaffold(
        containerColor = Color.White
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Color(0xFFFF6600)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 0.dp)
                ) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(260.dp)
                        ) {
                            AsyncImage(
                                model = uiState.destinationImage ?: uiState.featuredImage,
                                contentDescription = uiState.tripName,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
                                    .height(260.dp)
                                    .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp, bottomStart = 8.dp, bottomEnd = 8.dp)),
                                contentScale = ContentScale.Crop
                            )

                            IconButton(
                                onClick = onNavigateBack,
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(32.dp)
                                    .size(32.dp)
                                    .background(
                                        Color.Black.copy(alpha = 0.4f),
                                        CircleShape
                                    )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Close",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                    }

                    item {
                        Column(
                            modifier = Modifier.padding(horizontal = 20.dp)
                        ) {
                            Text(
                                text = uiState.destinationName?.takeIf { it.isNotEmpty() }
                                    ?: uiState.tripName,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.height(4.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                val bookingDisplay = when {
                                    uiState.bookingNumber.isNotEmpty() -> "Booking # ${uiState.bookingNumber}"
                                    else -> "Booking information unavailable"
                                }
                                Text(
                                    text = bookingDisplay,
                                    fontSize = 14.sp,
                                    color = Color.Gray
                                )
                                if (uiState.bookingNumber.isNotEmpty()) {
                                    Icon(
                                        imageVector = Icons.Default.ContentCopy,
                                        contentDescription = "Copy booking number",
                                        tint = Color.Gray,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = formatDateDayMonth(uiState.arrivalDate),
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.Black
                                )
                                Text(
                                    text = formatDateYear(uiState.arrivalDate),
                                    fontSize = 16.sp,
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
                                    text = formatDateDayMonth(uiState.departureDate),
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.Black
                                )
                                Text(
                                    text = formatDateYear(uiState.departureDate),
                                    fontSize = 16.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                    }

                    item {
                        CountdownCard(
                            days = uiState.daysToGo,
                            hours = uiState.hoursToGo,
                            minutes = uiState.minutesToGo
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                    }

                    if (!uiState.destinationName.isNullOrEmpty()) {
                        item {
                            DestinationLinkCard(uiState.destinationName!!)
                            Spacer(modifier = Modifier.height(20.dp))
                        }
                    }

                    item {
                        LinkFriendsCard()
                        Spacer(modifier = Modifier.height(24.dp))
                    }

                    if (uiState.actionsRequired.isNotEmpty()) {
                        item {
                            SectionHeaderRow(
                                icon = Icons.Outlined.Error,
                                title = "Actions Required",
                                iconTint = Color(0xFFFF6600)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                        }

                        items(uiState.actionsRequired) { action ->
                            ActionRequiredCard(action)
                            Spacer(modifier = Modifier.height(12.dp))
                        }

                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }

                    if (uiState.travellers.isNotEmpty()) {
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = null,
                                        tint = Color.Black,
                                        modifier = Modifier.size(28.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "${uiState.travellers.size} Travellers",
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black
                                    )
                                }
                                if (uiState.actionsRequired.isNotEmpty()) {
                                    Icon(
                                        imageVector = Icons.Outlined.Error,
                                        contentDescription = "Warning",
                                        tint = Color(0xFFFF6600),
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                        }

                        items(uiState.travellers) { traveller ->
                            TravellerCard(traveller)
                            if (traveller != uiState.travellers.last()) {
                                HorizontalDivider(
                                    modifier = Modifier.padding(horizontal = 20.dp),
                                    color = Color(0xFFE0E0E0)
                                )
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(20.dp))
                        }
                    }

                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.CalendarToday,
                                    contentDescription = null,
                                    tint = Color.Black,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Today's Events",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                            }
                            Text(
                                text = "See all",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFFFF6600)
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    item {
                        if (uiState.todayEvents.isEmpty()) {
                            Text(
                                text = "No events found for today",
                                modifier = Modifier.padding(horizontal = 20.dp),
                                fontSize = 16.sp,
                                color = Color.Gray
                            )
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                    }

                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = Color.Black,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Where you're going",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                            }
                            Text(
                                text = "Expand",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFFFF6600)
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    item {
                        val hasValidCoordinates = uiState.destinationLatitude != null
                                && uiState.destinationLongitude != null
                                && uiState.destinationLatitude != 0.0
                                && uiState.destinationLongitude != 0.0

                        if (hasValidCoordinates) {
                            DestinationMap(
                                latitude = uiState.destinationLatitude!!,
                                longitude = uiState.destinationLongitude!!,
                                destinationName = uiState.destinationName ?: uiState.tripName
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(250.dp)
                                    .padding(horizontal = 20.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFFE0E0E0)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Location not available",
                                    fontSize = 16.sp,
                                    color = Color.Gray
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))
                    }

                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Travel Details",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = "Details",
                                tint = Color.Black,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    item {
                        Column(
                            modifier = Modifier.padding(horizontal = 20.dp)
                        ) {
                            HorizontalDivider(color = Color(0xFFE0E0E0))
                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "Scheduled Flight Arrival",
                                fontSize = 14.sp,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "${formatDateNumeric(uiState.arrivalDate)} at ${(uiState.arrivalTime ?: "00:00").take(5)}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )

                            Spacer(modifier = Modifier.height(16.dp))
                            HorizontalDivider(color = Color(0xFFE0E0E0))
                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "Scheduled Flight Departure",
                                fontSize = 14.sp,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "${formatDateNumeric(uiState.departureDate)} at ${(uiState.departureTime ?: "20:00").take(5)}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )

                            Spacer(modifier = Modifier.height(16.dp))
                            HorizontalDivider(color = Color(0xFFE0E0E0))
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                    }

                    if (!uiState.hotel.isNullOrEmpty()) {
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Outlined.Hotel,
                                        contentDescription = null,
                                        tint = Color.Black,
                                        modifier = Modifier.size(28.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Hotel",
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black
                                    )
                                }
                                Icon(
                                    imageVector = Icons.Outlined.Error,
                                    contentDescription = "Info",
                                    tint = Color(0xFFFF6600),
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))

                            Column(
                                modifier = Modifier.padding(horizontal = 20.dp)
                            ) {
                                Text(
                                    text = "Staying at",
                                    fontSize = 14.sp,
                                    color = Color.Gray
                                )
                                Text(
                                    text = uiState.hotel!!,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.Black
                                )
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                        }
                    }

                    if (uiState.bookingTotal != null) {
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Payments",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                                Icon(
                                    imageVector = Icons.Default.ChevronRight,
                                    contentDescription = "Details",
                                    tint = Color.Black,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                        }

                        item {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.White
                                ),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(20.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = "${uiState.currencySymbol} Per Person",
                                            fontSize = 14.sp,
                                            color = Color(0xFFFF6600)
                                        )
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Default.Person,
                                                contentDescription = null,
                                                tint = Color.Black,
                                                modifier = Modifier.size(18.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = "X ${uiState.travellers.size}",
                                                fontSize = 14.sp,
                                                color = Color.Black
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(16.dp))
                                    HorizontalDivider(color = Color(0xFFE0E0E0))
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = "Booking Total:",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = Color.Black
                                        )
                                        Text(
                                            text = "${uiState.currencySymbol}${formatAmount(uiState.bookingTotal)}",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.Black
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = "Outstanding:",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = Color.Black
                                        )
                                        Text(
                                            text = "${uiState.currencySymbol}${formatAmount(uiState.bookingBalance ?: "0.00")}",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.Black
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(32.dp))
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
