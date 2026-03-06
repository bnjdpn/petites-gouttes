package com.bnjdpn.petitesgouttes.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bnjdpn.petitesgouttes.ui.components.AlertBanner
import com.bnjdpn.petitesgouttes.ui.components.BarChart
import com.bnjdpn.petitesgouttes.ui.components.ChartEntry
import com.bnjdpn.petitesgouttes.ui.theme.AlertDanger
import com.bnjdpn.petitesgouttes.ui.theme.AlertDangerText
import com.bnjdpn.petitesgouttes.viewmodel.StatsViewModel
import java.time.format.DateTimeFormatter

@Composable
fun StatsScreen(
    viewModel: StatsViewModel = viewModel()
) {
    val allBags by viewModel.allBags.collectAsState(initial = emptyList())

    val dailyVolumes = viewModel.getDailyVolumes(allBags, 30)
    val weeklyVolumes = viewModel.getWeeklyVolumes(allBags, 12)
    val avg7d = viewModel.getAverageDailyVolume(allBags, 7)
    val avg30d = viewModel.getAverageDailyVolume(allBags, 30)
    val thisMonth = viewModel.getMonthlyTotal(allBags, 0)
    val lastMonth = viewModel.getMonthlyTotal(allBags, 1)

    val lactationDrop = avg30d > 0 && avg7d < avg30d * 0.8
    val trend = if (avg30d > 0) ((avg7d - avg30d) / avg30d * 100) else 0.0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Statistiques",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )

        if (lactationDrop) {
            AlertBanner(
                message = "Baisse de lactation d\u00e9tect\u00e9e (moyenne 7j < 80% de la moyenne 30j)",
                backgroundColor = AlertDanger,
                textColor = AlertDangerText
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Card(modifier = Modifier.weight(1f)) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Moyenne 7 jours",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${avg7d.toInt()} ml/j",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (trend >= 0) Icons.AutoMirrored.Filled.TrendingUp else Icons.AutoMirrored.Filled.TrendingDown,
                            contentDescription = null,
                            tint = if (trend >= 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${if (trend >= 0) "+" else ""}${trend.toInt()}%",
                            style = MaterialTheme.typography.labelSmall,
                            color = if (trend >= 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
            Card(modifier = Modifier.weight(1f)) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Moyenne 30 jours",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${avg30d.toInt()} ml/j",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Card(modifier = Modifier.weight(1f)) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Ce mois", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("${thisMonth} ml", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                }
            }
            Card(modifier = Modifier.weight(1f)) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Mois pr\u00e9c\u00e9dent", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("${lastMonth} ml", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                }
            }
        }

        val dailyFormatter = DateTimeFormatter.ofPattern("dd/MM")
        val dailyEntries = dailyVolumes.map {
            ChartEntry(it.date.format(dailyFormatter), it.totalMl.toFloat())
        }
        BarChart(
            entries = dailyEntries,
            title = "Volume par jour (30 derniers jours)",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        val weeklyFormatter = DateTimeFormatter.ofPattern("dd/MM")
        val weeklyEntries = weeklyVolumes.map {
            ChartEntry(it.weekStart.format(weeklyFormatter), it.totalMl.toFloat())
        }
        BarChart(
            entries = weeklyEntries,
            title = "Volume par semaine (12 derni\u00e8res semaines)",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}
