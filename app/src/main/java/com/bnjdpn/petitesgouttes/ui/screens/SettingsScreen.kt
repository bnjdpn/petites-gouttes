package com.bnjdpn.petitesgouttes.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bnjdpn.petitesgouttes.viewmodel.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    viewModel: SettingsViewModel = viewModel()
) {
    val threshold by viewModel.lowStockThreshold.collectAsState(initial = 1500)
    val dailyConsumption by viewModel.dailyConsumption.collectAsState(initial = 300)
    val daycareDays by viewModel.daycareDays.collectAsState(initial = 5)

    var thresholdText by remember(threshold) { mutableStateOf(threshold.toString()) }
    var consumptionText by remember(dailyConsumption) { mutableStateOf(dailyConsumption.toString()) }
    var daysText by remember(daycareDays) { mutableStateOf(daycareDays.toString()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Param\u00e8tres") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Retour")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = "Alertes & Consommation",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            OutlinedTextField(
                value = thresholdText,
                onValueChange = { input ->
                    thresholdText = input.filter { it.isDigit() }
                    thresholdText.toIntOrNull()?.let { viewModel.updateLowStockThreshold(it) }
                },
                label = { Text("Seuil alerte stock bas (ml)") },
                supportingText = { Text("Alerte si le stock total est inf\u00e9rieur \u00e0 cette valeur") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = consumptionText,
                onValueChange = { input ->
                    consumptionText = input.filter { it.isDigit() }
                    consumptionText.toIntOrNull()?.let { viewModel.updateDailyConsumption(it) }
                },
                label = { Text("Consommation quotidienne b\u00e9b\u00e9 (ml)") },
                supportingText = { Text("Volume consomm\u00e9 par jour en garde") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = daysText,
                onValueChange = { input ->
                    daysText = input.filter { it.isDigit() }
                    daysText.toIntOrNull()?.let {
                        if (it in 1..7) viewModel.updateDaycareDays(it)
                    }
                },
                label = { Text("Jours de garde par semaine") },
                supportingText = { Text("Entre 1 et 7 jours") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            HorizontalDivider()

            Text(
                text = "\u00c0 propos",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Petites Gouttes v1.0.0",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Toutes les donn\u00e9es sont stock\u00e9es localement sur votre appareil.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
