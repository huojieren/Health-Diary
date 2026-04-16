package com.huojieren.healthdiary.ui

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.huojieren.healthdiary.database.HealthDatabase
import com.huojieren.healthdiary.model.RecordType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SummaryScreen(database: HealthDatabase) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    val dietRecords = remember { database.getRecords(RecordType.DIET) }
    val exerciseRecords = remember { database.getRecords(RecordType.EXERCISE) }
    val sleepRecords = remember { database.getRecords(RecordType.SLEEP) }

    val summaryText = buildString {
        append("=== 健康日记汇总 ===\n\n")
        append("【饮食记录】 (${dietRecords.size}条)\n")
        dietRecords.forEach { append("${it.date}: ${it.description}\n") }
        append("\n【运动记录】 (${exerciseRecords.size}条)\n")
        exerciseRecords.forEach { append("${it.date}: ${it.description}\n") }
        append("\n【睡眠记录】 (${sleepRecords.size}条)\n")
        sleepRecords.forEach { append("${it.date}: ${it.description}\n") }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val intent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, summaryText)
                    }
                    context.startActivity(Intent.createChooser(intent, "分享汇总"))
                },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Share, contentDescription = "分享")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SummaryCard(
                title = "饮食记录",
                count = dietRecords.size,
                records = dietRecords.map { "${it.date}: ${it.description}" }
            )
            SummaryCard(
                title = "运动记录",
                count = exerciseRecords.size,
                records = exerciseRecords.map { "${it.date}: ${it.description}" }
            )
            SummaryCard(
                title = "睡眠记录",
                count = sleepRecords.size,
                records = sleepRecords.map { "${it.date}: ${it.description}" }
            )
        }
    }
}

@Composable
fun SummaryCard(
    title: String,
    count: Int,
    records: List<String>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "$title (${count}条)",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            records.forEach { record ->
                Text(
                    text = record,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            if (records.isEmpty()) {
                Text(
                    text = "暂无记录",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}