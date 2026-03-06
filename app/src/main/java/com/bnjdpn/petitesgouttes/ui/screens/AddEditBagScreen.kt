package com.bnjdpn.petitesgouttes.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.bnjdpn.petitesgouttes.data.database.AppDatabase
import com.bnjdpn.petitesgouttes.data.repository.MilkBagRepository
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditBagScreen(
    bagId: Long? = null,
    onNavigateBack: () -> Unit,
    application: android.app.Application
) {
    val scope = rememberCoroutineScope()
    val dao = AppDatabase.getInstance(application).milkBagDao()
    val repository = MilkBagRepository(dao)

    var volumeText by remember { mutableStateOf("") }
    var pumpDate by remember { mutableStateOf(LocalDate.now()) }
    var isLoading by remember { mutableStateOf(bagId != null) }
    var volumeError by remember { mutableStateOf<String?>(null) }
    var dateError by remember { mutableStateOf<String?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }

    val isEditMode = bagId != null
    val expiryDate = pumpDate.plusMonths(4)
    val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    LaunchedEffect(bagId) {
        if (bagId != null) {
            val bag = repository.getBagById(bagId)
            if (bag != null) {
                volumeText = bag.volumeMl.toString()
                pumpDate = Instant.ofEpochMilli(bag.pumpDate).atZone(ZoneId.systemDefault()).toLocalDate()
            }
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditMode) "Modifier la pochette" else "Ajouter une pochette") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Retour")
                    }
                }
            )
        }
    ) { padding ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = volumeText,
                    onValueChange = {
                        volumeText = it.filter { c -> c.isDigit() }
                        volumeError = null
                    },
                    label = { Text("Volume (ml)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = volumeError != null,
                    supportingText = volumeError?.let { { Text(it) } },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = pumpDate.format(dateFormatter),
                    onValueChange = {},
                    label = { Text("Date de tirage") },
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { showDatePicker = true }) {
                            Icon(Icons.Default.CalendarToday, contentDescription = "Choisir une date")
                        }
                    },
                    isError = dateError != null,
                    supportingText = dateError?.let { { Text(it) } },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = expiryDate.format(dateFormatter),
                    onValueChange = {},
                    label = { Text("Date limite de consommation") },
                    readOnly = true,
                    enabled = false,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        val volume = volumeText.toIntOrNull()
                        var valid = true

                        if (volume == null || volume <= 0) {
                            volumeError = "Le volume doit \u00eatre sup\u00e9rieur \u00e0 0"
                            valid = false
                        }
                        if (pumpDate.isAfter(LocalDate.now())) {
                            dateError = "La date ne peut pas \u00eatre dans le futur"
                            valid = false
                        }

                        if (valid && volume != null) {
                            scope.launch {
                                if (isEditMode && bagId != null) {
                                    repository.updateBag(bagId, volume, pumpDate)
                                } else {
                                    repository.addBag(volume, pumpDate)
                                }
                                onNavigateBack()
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    Text(
                        text = if (isEditMode) "Modifier" else "Ajouter",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = pumpDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        )
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val selected = Instant.ofEpochMilli(millis).atZone(ZoneId.of("UTC")).toLocalDate()
                        if (!selected.isAfter(LocalDate.now())) {
                            pumpDate = selected
                            dateError = null
                        } else {
                            dateError = "La date ne peut pas \u00eatre dans le futur"
                        }
                    }
                    showDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Annuler")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}
