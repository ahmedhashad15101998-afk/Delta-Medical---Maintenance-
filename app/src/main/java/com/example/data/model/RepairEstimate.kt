package com.example.data.model

enum class EstimateStatus {
  APPROVED, // معتمدة
  IN_PROGRESS, // قيد التنفيذ
  COMPLETED // مكتملة
}

data class RepairEstimate(
  val id: String,
  val facilityName: String,
  val deviceModel: String,
  val scopeOfWork: String,
  val totalAmount: Long, // in EGP
  val approvalDate: String,
  val status: EstimateStatus = EstimateStatus.APPROVED,
  val targetCompletionDate: String,
  val assignedEngineer: String,
  val sourceTicketId: String? = null,
  val partsRequired: List<String> = emptyList()
)
