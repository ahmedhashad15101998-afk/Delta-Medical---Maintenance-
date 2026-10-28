package com.example.data.model

data class WarrantyClient(
  val id: String,
  val clientName: String,
  val facilityType: String, // معمل خاص / مستشفى عام / مركز متخصص
  val deviceCount: Int,
  val deviceTypes: String, // EMG + EEG
  val warrantyExpiryDate: String,
  val location: String,
  val city: String,
  val contactPerson: String,
  val phone: String,
  val serialNumbers: List<String>,
  val visitsPerYear: Int = 4, // زيارة دورية مجانية كل 3 شهور
  val visitsCompleted: Int = 2,
  val impedanceCalibrationVerified: Boolean = true,
  val operationalStatusPercent: Int = 100,
  val assignedEngineer: String = "م. محمد زيدان"
)
