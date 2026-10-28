package com.example.data.model

data class Engineer(
  val id: String,
  val name: String,
  val title: String,
  val phone: String,
  val assignedGovernorates: List<String>,
  val activeContractsCount: Int,
  val warrantyDevicesCount: Int,
  val inspectionTicketsCount: Int,
  val completionRatePercent: Int,
  val technicalRating: Float, // out of 5.0
  val avatarInitial: String
)
