package com.bnjdpn.petitesgouttes.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Kitchen
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.bnjdpn.petitesgouttes.data.database.MilkBagEntity
import com.bnjdpn.petitesgouttes.ui.theme.DlcGreen
import com.bnjdpn.petitesgouttes.ui.theme.DlcOrange
import com.bnjdpn.petitesgouttes.ui.theme.DlcRed
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@Composable
fun MilkBagCard(
    bag: MilkBagEntity,
    onEdit: (() -> Unit)? = null,
    onRemove: (() -> Unit)? = null,
    onRestore: (() -> Unit)? = null,
    onDelete: (() -> Unit)? = null,
    showActions: Boolean = true,
    modifier: Modifier = Modifier
) {
    val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val zone = ZoneId.systemDefault()
    val pumpDate = Instant.ofEpochMilli(bag.pumpDate).atZone(zone).toLocalDate()
    val expiryDate = Instant.ofEpochMilli(bag.expiryDate).atZone(zone).toLocalDate()
    val today = LocalDate.now()
    val daysUntilExpiry = ChronoUnit.DAYS.between(today, expiryDate)

    val dlcColor = when {
        daysUntilExpiry < 14 -> DlcRed
        daysUntilExpiry < 30 -> DlcOrange
        else -> DlcGreen
    }

    var showDeleteDialog by remember { mutableStateOf(false) }

    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(64.dp)
            ) {
                Text(
                    text = "${bag.volumeMl}",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "ml",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Tirage : ${pumpDate.format(dateFormatter)}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "DLC : ${expiryDate.format(dateFormatter)}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(dlcColor.copy(alpha = 0.15f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = if (daysUntilExpiry < 0) "Expir\u00e9" else "${daysUntilExpiry}j",
                            style = MaterialTheme.typography.labelSmall,
                            color = dlcColor,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                if (bag.removedFromFreezer && bag.removalDate != null) {
                    val removalDate = Instant.ofEpochMilli(bag.removalDate).atZone(zone).toLocalDate()
                    Text(
                        text = "Sorti le : ${removalDate.format(dateFormatter)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            if (showActions) {
                Column {
                    if (onEdit != null) {
                        IconButton(onClick = onEdit) {
                            Icon(Icons.Default.Edit, contentDescription = "Modifier", tint = MaterialTheme.colorScheme.primary)
                        }
                    }
                    if (onRemove != null) {
                        IconButton(onClick = onRemove) {
                            Icon(Icons.Default.Kitchen, contentDescription = "Sortir du cong\u00e9lateur", tint = MaterialTheme.colorScheme.secondary)
                        }
                    }
                    if (onRestore != null) {
                        IconButton(onClick = onRestore) {
                            Icon(Icons.AutoMirrored.Filled.Undo, contentDescription = "Remettre au cong\u00e9lateur", tint = MaterialTheme.colorScheme.secondary)
                        }
                    }
                    if (onDelete != null) {
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(Icons.Default.Delete, contentDescription = "Supprimer", tint = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            }
        }
    }

    if (showDeleteDialog && onDelete != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Supprimer cette pochette ?") },
            text = { Text("Cette action est irr\u00e9versible.") },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteDialog = false
                    onDelete()
                }) {
                    Text("Supprimer", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Annuler")
                }
            }
        )
    }
}
