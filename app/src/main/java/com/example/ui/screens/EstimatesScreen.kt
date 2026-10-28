package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.RepairEstimate
import com.example.ui.DeltaMedicalViewModel
import com.example.ui.components.StatusBadge
import com.example.ui.theme.*

@Composable
fun EstimatesScreen(
  viewModel: DeltaMedicalViewModel,
  modifier: Modifier = Modifier
) {
  val estimates by viewModel.repairEstimates.collectAsState()
  val totalEstimatesValue by viewModel.totalEstimatesValue.collectAsState()

  Column(
    modifier = modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp)
  ) {
    // Header summary card
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp)
        .testTag("estimates_summary_header"),
      colors = CardDefaults.cardColors(containerColor = MaterialNavyCard),
      shape = RoundedCornerShape(16.dp),
      border = CardDefaults.outlinedCardBorder().copy(
        brush = Brush.horizontalGradient(listOf(MedicalAmber.copy(alpha = 0.5f), Color.Transparent))
      )
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "إجمالي المقايسات المعتمدة رسمياً",
            fontSize = 12.sp,
            color = Slate400
          )
          Text(
            text = "${String.format("%,d", totalEstimatesValue)} ج.م",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = MedicalAmber
          )
          Text(
            text = "معهد ناصر (200 ألف) + أبو الريش الياباني (34 ألف)",
            fontSize = 11.sp,
            color = Slate200
          )
        }
        StatusBadge(text = "${estimates.size} مقايسة معتمدة", color = MedicalAmber)
      }
    }

    LazyColumn(
      modifier = Modifier.fillMaxSize(),
      verticalArrangement = Arrangement.spacedBy(12.dp),
      contentPadding = PaddingValues(bottom = 24.dp)
    ) {
      items(estimates) { estimate ->
        EstimateCard(estimate = estimate)
      }
    }
  }
}

@Composable
fun EstimateCard(
  estimate: RepairEstimate,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .testTag("estimate_card_${estimate.id}"),
    colors = CardDefaults.cardColors(containerColor = MaterialNavyCard),
    shape = RoundedCornerShape(14.dp),
    border = CardDefaults.outlinedCardBorder().copy(
      brush = Brush.horizontalGradient(listOf(MedicalAmber.copy(alpha = 0.35f), Color.Transparent))
    )
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = estimate.facilityName,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
          )
          Text(
            text = estimate.deviceModel,
            fontSize = 12.sp,
            color = MedicalCyan,
            fontWeight = FontWeight.Medium
          )
        }

        Surface(
          color = MedicalAmber.copy(alpha = 0.15f),
          shape = RoundedCornerShape(8.dp),
          border = androidx.compose.foundation.BorderStroke(1.dp, MedicalAmber.copy(alpha = 0.4f))
        ) {
          Text(
            text = "${String.format("%,d", estimate.totalAmount)} ج.م",
            color = MedicalAmber,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
          )
        }
      }

      HorizontalDivider(
        modifier = Modifier.padding(vertical = 10.dp),
        color = MedicalNavyLight
      )

      Text(
        text = "نطاق العمل والإصلاح المعتمد:",
        fontSize = 11.sp,
        color = Slate400
      )
      Text(
        text = estimate.scopeOfWork,
        fontSize = 12.sp,
        color = Slate200,
        lineHeight = 18.sp
      )

      if (estimate.partsRequired.isNotEmpty()) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = "قطع الغيار واللوحات المشمولة:",
          fontSize = 11.sp,
          color = Slate400
        )
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          estimate.partsRequired.forEach { part ->
            Surface(
              color = MedicalNavyLight.copy(alpha = 0.6f),
              shape = RoundedCornerShape(6.dp)
            ) {
              Text(
                text = part,
                fontSize = 10.sp,
                color = Slate200,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "تاريخ الاعتماد: ${estimate.approvalDate}",
            fontSize = 11.sp,
            color = Slate400
          )
          Text(
            text = "المهندس المشرف: ${estimate.assignedEngineer}",
            fontSize = 11.sp,
            color = MedicalCyan
          )
        }

        StatusBadge(text = "معتمدة رسمياً", color = MedicalEmerald)
      }
    }
  }
}
