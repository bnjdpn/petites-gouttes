package com.bnjdpn.petitesgouttes.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bnjdpn.petitesgouttes.ui.components.AlertBanner
import com.bnjdpn.petitesgouttes.ui.components.MilkBagCard
import com.bnjdpn.petitesgouttes.ui.theme.AlertDanger
import com.bnjdpn.petitesgouttes.ui.theme.AlertDangerText
import com.bnjdpn.petitesgouttes.ui.theme.AlertWarning
import com.bnjdpn.petitesgouttes.ui.theme.AlertWarningText
import com.bnjdpn.petitesgouttes.viewmodel.DashboardViewModel

@Composable
fun DashboardScreen(
    onAddBag: () -> Unit,
    viewModel: DashboardViewModel = viewModel()
) {
    val bagCount by viewModel.activeBagCount.collectAsState(initial = 0)
    val totalVolume by viewModel.totalActiveVolume.collectAsState(initial = 0)
    val nextBag by viewModel.nextBagToUse.collectAsState(initial = null)
    val expiringSoon by viewModel.bagsExpiringSoon.collectAsState(initial = emptyList())
    val threshold by viewModel.lowStockThreshold.collectAsState(initial = 1500)
    val dailyConsumption by viewModel.dailyConsumption.collectAsState(initial = 300)
    val daycareDays by viewModel.daycareDays.collectAsState(initial = 5)

    val volume = totalVolume ?: 0
    val stockDays = if (dailyConsumption > 0) volume / dailyConsumption else 0
    val isLowStock = volume < threshold

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddBag,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Ajouter une pochette")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Tableau de bord",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SummaryCard(
                    title = "Pochettes",
                    value = "$bagCount",
                    icon = Icons.Default.Inventory2,
                    modifier = Modifier.weight(1f)
                )
                SummaryCard(
                    title = "Volume total",
                    value = "${volume} ml",
                    icon = Icons.Default.WaterDrop,
                    modifier = Modifier.weight(1f)
                )
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.AcUnit,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Stock restant",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "$stockDays jours",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "Bas\u00e9 sur ${dailyConsumption}ml/jour, ${daycareDays}j/semaine",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                        )
                    }
                }
            }

            if (isLowStock) {
                AlertBanner(
                    message = "Stock bas ! Il reste ${stockDays} jours de stock (seuil : ${threshold}ml)",
                    backgroundColor = AlertWarning,
                    textColor = AlertWarningText
                )
            }

            if (expiringSoon.isNotEmpty()) {
                AlertBanner(
                    message = "${expiringSoon.size} pochette(s) expirent dans les 14 prochains jours",
                    backgroundColor = AlertDanger,
                    textColor = AlertDangerText
                )
            }

            nextBag?.let { bag ->
                Text(
                    text = "Prochaine pochette \u00e0 utiliser",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                MilkBagCard(bag = bag, showActions = false)
            }

            if (bagCount == 0) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Aucune pochette en stock",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Appuyez sur + pour en ajouter une",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SummaryCard(
    title: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
