package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.InstalledDevice
import com.example.data.model.Invoice
import com.example.data.model.PaymentStatus
import com.example.ui.DeltaMedicalViewModel
import com.example.ui.components.StatusBadge
import com.example.ui.theme.*

@Composable
fun InvoicesScreen(
  viewModel: DeltaMedicalViewModel,
  modifier: Modifier = Modifier
) {
  var selectedSubTab by remember { mutableStateOf(0) } // 0: Invoices, 1: Installed Base Hub
  val invoices by viewModel.invoices.collectAsState()
  val installedDevices by viewModel.installedDevices.collectAsState()

  val totalInvoiced = invoices.sumOf { it.totalAmount }
  val totalCollected = invoices.sumOf { it.paidAmount }
  val totalRemaining = invoices.sumOf { it.remainingAmount }

  Column(
    modifier = modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp)
  ) {
    TabRow(
      selectedTabIndex = selectedSubTab,
      containerColor = MaterialNavyCard,
      contentColor = MedicalCyan,
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(12.dp))
        .padding(vertical = 8.dp)
    ) {
      Tab(
        selected = selectedSubTab == 0,
        onClick = { selectedSubTab = 0 },
        text = { Text("الفواتير والتحصيل (${invoices.size})", fontWeight = FontWeight.Bold, fontSize = 13.sp) }
      )
      Tab(
        selected = selectedSubTab == 1,
        onClick = { selectedSubTab = 1 },
        text = { Text("قاعدة الأجهزة المركبة (${installedDevices.size})", fontWeight = FontWeight.Bold, fontSize = 13.sp) }
      )
    }

    if (selectedSubTab == 0) {
      // Invoices View
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(bottom = 10.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialNavyCard),
        shape = RoundedCornerShape(14.dp)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Column {
            Text("المحصل الفعلي", fontSize = 11.sp, color = Slate400)
            Text(
              "${String.format("%,d", totalCollected)} ج.م",
              fontSize = 16.sp,
              fontWeight = FontWeight.Bold,
              color = MedicalEmerald
            )
          }
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("المتبقي للتحصيل", fontSize = 11.sp, color = Slate400)
            Text(
              "${String.format("%,d", totalRemaining)} ج.م",
              fontSize = 16.sp,
              fontWeight = FontWeight.Bold,
              color = MedicalCrimson
            )
          }
          Column(horizontalAlignment = Alignment.End) {
            Text("إجمالي الفواتير", fontSize = 11.sp, color = Slate400)
            Text(
              "${String.format("%,d", totalInvoiced)} ج.م",
              fontSize = 16.sp,
              fontWeight = FontWeight.Bold,
              color = MedicalCyan
            )
          }
        }
      }

      LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
      ) {
        items(invoices) { invoice ->
          InvoiceCard(invoice = invoice)
        }
      }
    } else {
      // Installed Base Hub View
      LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
      ) {
        items(installedDevices) { device ->
          InstalledDeviceCard(device = device)
        }
      }
    }
  }
}

@Composable
fun InvoiceCard(
  invoice: Invoice,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .testTag("invoice_card_${invoice.id}"),
    colors = CardDefaults.cardColors(containerColor = MaterialNavyCard),
    shape = RoundedCornerShape(14.dp),
    border = CardDefaults.outlinedCardBorder().copy(
      brush = Brush.horizontalGradient(listOf(MedicalCyan.copy(alpha = 0.3f), Color.Transparent))
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
            text = invoice.facilityName,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
          )
          Text(
            text = "${invoice.id} • ${invoice.invoiceType.labelArabic}",
            fontSize = 11.sp,
            color = Slate400
          )
        }
        StatusBadge(
          text = invoice.paymentStatus.labelArabic,
          color = when (invoice.paymentStatus) {
            PaymentStatus.PAID_FULL -> MedicalEmerald
            PaymentStatus.PARTIALLY_PAID -> MedicalAmber
            PaymentStatus.DUE -> MedicalCrimson
          }
        )
      }

      HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MedicalNavyLight)

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Text(
          text = "المبلغ: ${String.format("%,d", invoice.totalAmount)} ج.م",
          fontSize = 12.sp,
          fontWeight = FontWeight.SemiBold,
          color = Color.White
        )
        Text(
          text = "المسدد: ${String.format("%,d", invoice.paidAmount)} ج.م",
          fontSize = 12.sp,
          color = MedicalEmerald
        )
        Text(
          text = "المتبقي: ${String.format("%,d", invoice.remainingAmount)} ج.م",
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold,
          color = if (invoice.remainingAmount > 0) MedicalCrimson else MedicalEmerald
        )
      }

      Spacer(modifier = Modifier.height(6.dp))
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Text("تاريخ الإصدار: ${invoice.issueDate}", fontSize = 10.sp, color = Slate400)
        Text("تاريخ الاستحقاق: ${invoice.dueDate}", fontSize = 10.sp, color = Slate400)
      }
    }
  }
}

@Composable
fun InstalledDeviceCard(
  device: InstalledDevice,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .testTag("device_card_${device.id}"),
    colors = CardDefaults.cardColors(containerColor = MaterialNavyCard),
    shape = RoundedCornerShape(14.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = device.facilityName,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
          )
          Text(
            text = "${device.deviceModel} • S/N: ${device.serialNumber}",
            fontSize = 12.sp,
            color = MedicalCyan
          )
        }
        StatusBadge(text = device.modality, color = MedicalCyan)
      }

      Spacer(modifier = Modifier.height(6.dp))
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Text(
          text = "${device.governorate} • ${device.coverageType}",
          fontSize = 11.sp,
          color = Slate400
        )
        Text(
          text = device.status,
          fontSize = 11.sp,
          color = MedicalEmerald,
          fontWeight = FontWeight.Bold
        )
      }
    }
  }
}
