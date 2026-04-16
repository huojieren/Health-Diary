package com.huojieren.healthdiary.model

data class Record(
    val id: Long = 0,
    val date: String,
    val description: String,
    val type: RecordType
)

enum class RecordType(val tableName: String) {
    DIET("diet"),
    EXERCISE("exercise"),
    SLEEP("sleep")
}