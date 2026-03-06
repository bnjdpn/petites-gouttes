package com.bnjdpn.petitesgouttes.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bnjdpn.petitesgouttes.ui.components.MilkBagCard
import com.bnjdpn.petitesgouttes.viewmodel.FreezerViewModel
import com.bnjdpn.petitesgouttes.viewmodel.SortOrder

@Composable
fun FreezerListScreen(
    onAddBag: () -> Unit,
    onEditBag: (Long) -> Unit,
    viewModel: FreezerViewModel = viewModel()
) {
    val bags by viewModel.activeBags.collectAsState(initial = emptyList())
    val sortOrder by viewModel.sortOrder.collectAsState()
    val sortedBags = viewModel.sortBags(bags, sortOrder)
    var showSortMenu by remember { mutableStateOf(false) }

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
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Cong\u00e9lateur",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${bags.size} pochette(s)",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Box {
                    IconButton(onClick = { showSortMenu = true }) {
                        Icon(Icons.Default.FilterList, contentDescription = "Trier")
                    }
                    DropdownMenu(
                        expanded = showSortMenu,
                        onDismissRequest = { showSortMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Date (ancien \u2192 r\u00e9cent)") },
                            onClick = { viewModel.setSortOrder(SortOrder.DATE_ASC); showSortMenu = false }
                        )
                        DropdownMenuItem(
                            text = { Text("Date (r\u00e9cent \u2192 ancien)") },
                            onClick = { viewModel.setSortOrder(SortOrder.DATE_DESC); showSortMenu = false }
                        )
                        DropdownMenuItem(
                            text = { Text("Volume (petit \u2192 grand)") },
                            onClick = { viewModel.setSortOrder(SortOrder.VOLUME_ASC); showSortMenu = false }
                        )
                        DropdownMenuItem(
                            text = { Text("Volume (grand \u2192 petit)") },
                            onClick = { viewModel.setSortOrder(SortOrder.VOLUME_DESC); showSortMenu = false }
                        )
                    }
                }
            }

            if (sortedBags.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Aucune pochette dans le cong\u00e9lateur",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(sortedBags, key = { it.id }) { bag ->
                        MilkBagCard(
                            bag = bag,
                            onEdit = { onEditBag(bag.id) },
                            onRemove = { viewModel.removeBag(bag.id) },
                            onDelete = { viewModel.deleteBag(bag) }
                        )
                    }
                }
            }
        }
    }
}
