package com.example.eventshop.view.user

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.eventshop.model.Hall
import com.example.eventshop.utils.Resource
import com.example.eventshop.viewmodel.BookingViewModel
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingScreen(
    bookingViewModel: BookingViewModel
) {
    val hallsState by bookingViewModel.halls.collectAsState()
    val bookingState by bookingViewModel.bookingState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    var selectedHall by remember { mutableStateOf<Hall?>(null) }
    var selectedDate by remember { mutableStateOf("") }
    var selectedTime by remember { mutableStateOf("") }
    var numberOfHalls by remember { mutableIntStateOf(1) }
    var hallDropdownExpanded by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    LaunchedEffect(bookingState) {
        when (val state = bookingState) {
            is Resource.Success -> {
                snackbarHostState.showSnackbar("Booking submitted successfully!")
                selectedHall = null
                selectedDate = ""
                selectedTime = ""
                numberOfHalls = 1
                bookingViewModel.resetBookingState()
            }
            is Resource.Error -> {
                snackbarHostState.showSnackbar(state.message)
                bookingViewModel.resetBookingState()
            }
            else -> {}
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text(
                text = "Book a Venue",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Select your preferred venue and schedule",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    Text(
                        text = "Select Hall",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    when (val state = hallsState) {
                        is Resource.Loading -> {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp))
                        }
                        is Resource.Error -> {
                            Text(state.message, color = MaterialTheme.colorScheme.error)
                        }
                        is Resource.Success -> {
                            if (state.data.isEmpty()) {
                                Text(
                                    "No halls available. Please check back later.",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            } else {
                                ExposedDropdownMenuBox(
                                    expanded = hallDropdownExpanded,
                                    onExpandedChange = { hallDropdownExpanded = !hallDropdownExpanded }
                                ) {
                                    OutlinedTextField(
                                        value = selectedHall?.name ?: "",
                                        onValueChange = {},
                                        readOnly = true,
                                        label = { Text("Choose a hall") },
                                        leadingIcon = {
                                            Icon(Icons.Default.MeetingRoom, contentDescription = null)
                                        },
                                        trailingIcon = {
                                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = hallDropdownExpanded)
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .menuAnchor(),
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    ExposedDropdownMenu(
                                        expanded = hallDropdownExpanded,
                                        onDismissRequest = { hallDropdownExpanded = false }
                                    ) {
                                        state.data.forEach { hall ->
                                            DropdownMenuItem(
                                                text = {
                                                    Column {
                                                        Text(
                                                            hall.name,
                                                            fontWeight = FontWeight.Medium
                                                        )
                                                        Text(
                                                            "Rs. ${String.format("%.2f", hall.pricing)} per hall",
                                                            style = MaterialTheme.typography.bodySmall,
                                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                                        )
                                                    }
                                                },
                                                onClick = {
                                                    selectedHall = hall
                                                    hallDropdownExpanded = false
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "Select Date",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                DatePickerDialog(
                                    context,
                                    { _, year, month, day ->
                                        selectedDate = "${year}-${String.format("%02d", month + 1)}-${String.format("%02d", day)}"
                                    },
                                    calendar.get(Calendar.YEAR),
                                    calendar.get(Calendar.MONTH),
                                    calendar.get(Calendar.DAY_OF_MONTH)
                                ).show()
                            }
                    ) {
                        OutlinedTextField(
                            value = selectedDate,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Pick a date") },
                            leadingIcon = {
                                Icon(Icons.Default.CalendarMonth, contentDescription = null)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = false,
                            shape = RoundedCornerShape(12.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "Select Time",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                TimePickerDialog(
                                    context,
                                    { _, hour, minute ->
                                        selectedTime = "${String.format("%02d", hour)}:${String.format("%02d", minute)}"
                                    },
                                    calendar.get(Calendar.HOUR_OF_DAY),
                                    calendar.get(Calendar.MINUTE),
                                    true
                                ).show()
                            }
                    ) {
                        OutlinedTextField(
                            value = selectedTime,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Pick a time") },
                            leadingIcon = {
                                Icon(Icons.Default.AccessTime, contentDescription = null)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = false,
                            shape = RoundedCornerShape(12.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "Number of Halls",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedIconButton(
                            onClick = { if (numberOfHalls > 1) numberOfHalls-- }
                        ) {
                            Icon(Icons.Default.Remove, contentDescription = "Decrease")
                        }

                        Text(
                            text = "$numberOfHalls",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 24.dp)
                        )

                        OutlinedIconButton(
                            onClick = { numberOfHalls++ }
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Increase")
                        }
                    }

                    if (selectedHall != null) {
                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Total: Rs. ${String.format("%.2f", (selectedHall?.pricing ?: 0.0) * numberOfHalls)}",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.secondary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    selectedHall?.let { hall ->
                        if (selectedDate.isNotBlank() && selectedTime.isNotBlank()) {
                            bookingViewModel.submitBooking(hall, selectedDate, selectedTime, numberOfHalls)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                enabled = selectedHall != null && selectedDate.isNotBlank() &&
                        selectedTime.isNotBlank() && bookingState !is Resource.Loading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                if (bookingState is Resource.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(
                        "Submit Booking",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}
