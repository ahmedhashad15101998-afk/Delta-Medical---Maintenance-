package com.example.data.model

enum class OverdueSeverity {
  CRITICAL, // +14 days (Red)
  MEDIUM    // +7 days (Amber)
}

data class OverdueTask(
  val id: String,
  val title: String,
  val facilityName: String,
  val deviceType: String,
  val taskType: String, // زيارة دورية وقائية / تسليم مقايسة / فحص معاوقة / معاينة ميدانية
  val dueDate: String,
  val daysOverdue: Int,
  val severity: OverdueSeverity,
  val assignedEngineer: String,
  val isCompleted: Boolean = false
)
