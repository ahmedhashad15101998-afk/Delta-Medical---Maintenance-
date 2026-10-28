package com.example.data.model

enum class ContractStatus {
  ACTIVE,
  EXPIRING_SOON,
  EXPIRED
}

data class Contract(
  val id: String,
  val facilityName: String,
  val annualValue: Long, // in EGP
  val deviceType: String,
  val deviceCount: Int,
  val visitsPerYear: Int,
  val visitsCompleted: Int = 0,
  val contactPerson: String,
  val phone: String,
  val governorate: String,
  val city: String,
  val address: String,
  val startDate: String,
  val endDate: String,
  val serialNumbers: List<String>,
  val includesSpareParts: Boolean = false,
  val status: ContractStatus = ContractStatus.ACTIVE,
  val assignedEngineer: String = "م. أحمد حشاد",
  val notes: String = ""
)
