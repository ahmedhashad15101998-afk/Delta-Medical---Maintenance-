package com.example.data.model

enum class InvoiceType(val labelArabic: String) {
  MAINTENANCE_CONTRACT("عقد صيانة سنوي"),
  REPAIR_ESTIMATE("مقايسة إصلاح معتمدة"),
  SPARE_PARTS("قطع غيار ومستلزمات"),
  EMERGENCY_VISIT("زيارة طارئة / معاينة")
}

enum class PaymentStatus(val labelArabic: String) {
  PAID_FULL("مدفوع بالكامل"),
  PARTIALLY_PAID("مدفوع جزئياً"),
  DUE("مستحق السداد")
}

data class Invoice(
  val id: String, // e.g. INV-2026-104
  val facilityName: String,
  val invoiceType: InvoiceType,
  val issueDate: String,
  val dueDate: String,
  val totalAmount: Long, // EGP
  val paidAmount: Long,
  val paymentStatus: PaymentStatus
) {
  val remainingAmount: Long get() = (totalAmount - paidAmount).coerceAtLeast(0)
}
