package com.example.data.model

enum class BreakdownType(val labelArabic: String) {
  COMPLETE("عطل كلي (توقف تام عن العمل)"),
  PARTIAL("عطل جزئي (تشويش إشارة / كابل)")
}

enum class TicketStatus(val labelArabic: String) {
  OPEN("تذكرة مفتوحة"),
  DIAGNOSED("تمت المعاينة والتشخيص"),
  CONVERTED_TO_ESTIMATE("تم التحويل لمقايسة رسمية"),
  RESOLVED("تم الإصلاح والتشغيل")
}

data class DiagnosticReport(
  val reportDate: String,
  val findings: String,
  val damagedParts: List<String>,
  val pcbBoardSerialNumbers: List<String>,
  val technicalRecommendations: String,
  val estimatedRepairCost: Long = 0
)

data class FieldServiceTicket(
  val id: String, // e.g. TKT-2026-081
  val clientName: String,
  val deviceModel: String,
  val breakdownType: BreakdownType,
  val reportDate: String,
  val assignedEngineer: String,
  val inspectionFee: Long = 2500, // رسوم المعاينة والانتقال
  val isFeeCollected: Boolean = true,
  val status: TicketStatus = TicketStatus.OPEN,
  val diagnosticReport: DiagnosticReport? = null,
  val contactPhone: String = "",
  val location: String = ""
)
