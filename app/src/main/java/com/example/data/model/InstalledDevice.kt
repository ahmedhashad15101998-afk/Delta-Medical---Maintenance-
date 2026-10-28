package com.example.data.model

data class InstalledDevice(
  val id: String,
  val facilityName: String,
  val governorate: String,
  val deviceModel: String,
  val modality: String, // EMG (عضلات) / EEG (مخ)
  val serialNumber: String,
  val installationDate: String,
  val coverageType: String, // عقد صيانة / فترة ضمان / خارج التغطية
  val status: String = "يعمل بكفاءة 100%",
  val contactPhone: String = ""
)

data class PMCheckItem(
  val title: String,
  val category: String,
  var isChecked: Boolean = false,
  val notes: String = ""
)
