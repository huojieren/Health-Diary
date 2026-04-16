package com.huojieren.healthdiary.ui

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.huojieren.healthdiary.database.HealthDatabase
import com.huojieren.healthdiary.model.Record
import com.huojieren.healthdiary.model.RecordType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordListScreen(database: HealthDatabase, recordType: RecordType) {
    var records by remember { mutableStateOf<List<Record>>(emptyList()) }
    var showAddDialog by remember { mutableStateOf(false) }

    LaunchedEffect(recordType) {
        records = database.getRecords(recordType)
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "添加记录")
            }
        }
    ) { padding ->
        if (records.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "暂无记录，点击+添加",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(records, key = { it.id }) { record ->
                    RecordCard(
                        record = record,
                        onDelete = {
                            database.deleteRecord(record.id, recordType)
                            records = database.getRecords(recordType)
                        }
                    )
                }
            }
        }
    }

    if (showAddDialog) {
        AddRecordDialog(
            recordType = recordType,
            onDismiss = { showAddDialog = false },
            onConfirm = { description ->
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val record = Record(
                    date = dateFormat.format(Date()),
                    description = description,
                    type = recordType
                )
                database.insertRecord(record)
                records = database.getRecords(recordType)
                showAddDialog = false
            }
        )
    }
}

@Composable
fun RecordCard(record: Record, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = record.date,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = record.description,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "删除",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRecordDialog(
    recordType: RecordType,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var description by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                when (recordType) {
                    RecordType.DIET -> "添加饮食记录"
                    RecordType.EXERCISE -> "添加运动记录"
                    RecordType.SLEEP -> "添加睡眠记录"
                }
            )
        },
        text = {
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("记录内容") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3
            )
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(description) },
                enabled = description.isNotBlank()
            ) {
                Text("保存")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("取消")
            }
        }
    )
}