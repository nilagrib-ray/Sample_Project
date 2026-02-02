package com.app.sampleproject.presentation.trips.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DoubleArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.app.sampleproject.domain.model.TripDomain
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import com.app.sampleproject.domain.model.DestinationCategory

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        fontSize = 24.sp,
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
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box {
            Column {
                AsyncImage(
                    model = trip.destinationImage ?: trip.featuredImage,
                    contentDescription = trip.tripName,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
                    contentScale = ContentScale.Crop
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
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
                            imageVector = Icons.Default.DoubleArrow,
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
                        fontWeight = FontWeight.Normal,
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
                        fontStyle = FontStyle.Italic,
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
                            containerColor = Color(0xFFFFF3E0)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "$daysToGo days to go!",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            fontStyle = FontStyle.Italic,
                            color = Color.Black
                        )
                    }
                }
            }
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

@Composable
fun DestinationCard(
    destination: DestinationCategory,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier.width(240.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        onClick = onClick
    ) {
        Column {
            AsyncImage(
                model = destination.imageUrl.ifEmpty {
                    destination.squareImageUrl },
                contentDescription = destination.categoryName,
                modifier = Modifier
                    .width(240.dp)
                    .height(280.dp)
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = destination.categoryName,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = androidx.compose.ui.Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}