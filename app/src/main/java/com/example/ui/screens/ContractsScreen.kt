package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import com.example.data.model.Contract
import com.example.data.model.WarrantyClient
import com.example.ui.DeltaMedicalViewModel
import com.example.ui.components.SectionHeader
import com.example.ui.components.StatusBadge
import com.example.ui.theme.*

@Composable
fun ContractsScreen(
  viewModel: DeltaMedicalViewModel,
  modifier: Modifier = Modifier
) {
  var selectedSubTab by remember { mutableStateOf(0) } // 0: Contracts, 1: Warranty
  var selectedGovFilter by remember { mutableStateOf("الكل") }
  val contracts by viewModel.contracts.collectAsState()
  val warrantyClients by viewModel.warrantyClients.collectAsState()
  val searchQuery by viewModel.searchQuery.collectAsState()

  val governorates = listOf("الكل", "القاهرة", "القليوبية", "الإسكندرية", "الفيوم", "الشرقية", "الجيزة")

  val filteredContracts = contracts.filter { c ->
    (selectedGovFilter == "الكل" || c.governorate == selectedGovFilter) &&
      (searchQuery.isEmpty() || c.facilityName.contains(searchQuery, ignoreCase = true) ||
        c.deviceType.contains(searchQuery, ignoreCase = true) ||
        c.contactPerson.contains(searchQuery, ignoreCase = true))
  }

  val filteredWarranty = warrantyClients.filter { w ->
    (selectedGovFilter == "الكل" || w.city.contains(selectedGovFilter) || w.location.contains(selectedGovFilter)) &&
      (searchQuery.isEmpty() || w.clientName.contains(searchQuery, ignoreCase = true) ||
        w.deviceTypes.contains(searchQuery, ignoreCase = true))
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp)
  ) {
    // Top Tabs
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
        text = {
          Text(
            text = "عقود الصيانة السارية (${contracts.size})",
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp
          )
        }
      )
      Tab(
        selected = selectedSubTab == 1,
        onClick = { selectedSubTab = 1 },
        text = {
          Text(
            text = "أجهزة فترة الضمان (${warrantyClients.size})",
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp
          )
        }
      )
    }

    // Filter Chips
    LazyRow(
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 6.dp),
      horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
      items(governorates) { gov ->
        FilterChip(
          selected = selectedGovFilter == gov,
          onClick = { selectedGovFilter = gov },
          label = { Text(gov, fontSize = 12.sp) },
          colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = MedicalCyan.copy(alpha = 0.2f),
            selectedLabelColor = MedicalCyan,
            containerColor = MaterialNavyCard,
            labelColor = Slate400
          )
        )
      }
    }

    if (selectedSubTab == 0) {
      // Contracts List
      Surface(
        color = MedicalNavyLight.copy(alpha = 0.4f),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 6.dp)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = "إجمالي قيمة التعاقدات السنوية",
              fontSize = 12.sp,
              color = Slate400
            )
            Text(
              text = "638,000 ج.م",
              fontSize = 18.sp,
              fontWeight = FontWeight.Bold,
              color = MedicalCyan
            )
          }
          Column(horizontalAlignment = Alignment.End) {
            Text(
              text = "الأجهزة المشمولة بالعقود",
              fontSize = 12.sp,
              color = Slate400
            )
            Text(
              text = "11 جهازاً طبياً (EMG / EEG)",
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold,
              color = MedicalEmerald
            )
          }
        }
      }

      LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
      ) {
        items(filteredContracts) { contract ->
          ContractFullCard(
            contract = contract,
            onRenew = { viewModel.openContractRenewal(contract) }
          )
        }
      }
    } else {
      // Warranty List
      Surface(
        color = MedicalEmeraldBg,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 6.dp)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = "قاعدة عملاء فترة الضمان",
              fontSize = 12.sp,
              color = Slate200
            )
            Text(
              text = "3 عملاء معتمدين (6 أجهزة)",
              fontSize = 16.sp,
              fontWeight = FontWeight.Bold,
              color = MedicalEmerald
            )
          }
          StatusBadge(text = "كفاءة 100%", color = MedicalEmerald)
        }
      }

      LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
      ) {
        items(filteredWarranty) { client ->
          WarrantyFullCard(client = client)
        }
      }
    }
  }
}

@Composable
fun ContractFullCard(
  contract: Contract,
  onRenew: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .testTag("contract_card_${contract.id}"),
    colors = CardDefaults.cardColors(containerColor = MaterialNavyCard),
    shape = RoundedCornerShape(14.dp),
    border = CardDefaults.outlinedCardBorder().copy(
      brush = Brush.horizontalGradient(
        if (contract.endDate.contains("2026-11"))
          listOf(MedicalAmber.copy(alpha = 0.5f), Color.Transparent)
        else
          listOf(MedicalCyan.copy(alpha = 0.3f), Color.Transparent)
      )
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
            text = contract.facilityName,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
          )
          Text(
            text = "${contract.governorate} • ${contract.city} • ${contract.address}",
            fontSize = 11.sp,
            color = Slate400
          )
        }

        Column(horizontalAlignment = Alignment.End) {
          Text(
            text = "${String.format("%,d", contract.annualValue)} ج.م",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = MedicalCyan
          )
          Text(
            text = "سنوياً غير شامل ض.ق.م 14%",
            fontSize = 9.sp,
            color = Slate400
          )
        }
      }

      HorizontalDivider(
        modifier = Modifier.padding(vertical = 10.dp),
        color = MedicalNavyLight
      )

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Column {
          Text(text = "نوع وعدد الأجهزة:", fontSize = 11.sp, color = Slate400)
          Text(
            text = "${contract.deviceType} (${contract.deviceCount} جهاز)",
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = Slate200
          )
        }
        Column(horizontalAlignment = Alignment.End) {
          Text(text = "الزيارات الوقائية:", fontSize = 11.sp, color = Slate400)
          Text(
            text = "${contract.visitsCompleted} من أصل ${contract.visitsPerYear} زيارات",
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = MedicalEmerald
          )
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Column {
          Text(text = "مسؤول التواصل:", fontSize = 11.sp, color = Slate400)
          Text(
            text = "${contract.contactPerson} (${contract.phone})",
            fontSize = 12.sp,
            color = Slate200
          )
        }
        Column(horizontalAlignment = Alignment.End) {
          Text(text = "المهندس المكلف:", fontSize = 11.sp, color = Slate400)
          Text(
            text = contract.assignedEngineer,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = MedicalCyan
          )
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          if (contract.includesSpareParts) {
            StatusBadge(text = "شامل قطع الغيار", color = MedicalEmerald)
            Spacer(modifier = Modifier.width(6.dp))
          }
          Text(
            text = "ساري حتى: ${contract.endDate}",
            fontSize = 11.sp,
            color = if (contract.endDate.contains("2026-11")) MedicalAmber else Slate400
          )
        }

        OutlinedButton(
          onClick = onRenew,
          modifier = Modifier.testTag("renew_btn_${contract.id}"),
          shape = RoundedCornerShape(8.dp),
          contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Refresh,
            contentDescription = null,
            modifier = Modifier.size(14.dp),
            tint = MedicalCyan
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(text = "تجديد / تمديد", fontSize = 11.sp, color = MedicalCyan)
        }
      }
    }
  }
}

@Composable
fun WarrantyFullCard(
  client: WarrantyClient,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .testTag("warranty_full_${client.id}"),
    colors = CardDefaults.cardColors(containerColor = MaterialNavyCard),
    shape = RoundedCornerShape(14.dp),
    border = CardDefaults.outlinedCardBorder().copy(
      brush = Brush.horizontalGradient(listOf(MedicalEmerald.copy(alpha = 0.4f), Color.Transparent))
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
            text = client.clientName,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
          )
          Text(
            text = "${client.facilityType} • ${client.city} • ${client.location}",
            fontSize = 11.sp,
            color = Slate400
          )
        }
        StatusBadge(text = "ضمان رسمي", color = MedicalEmerald)
      }

      HorizontalDivider(
        modifier = Modifier.padding(vertical = 10.dp),
        color = MedicalNavyLight
      )

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Column {
          Text(text = "الأجهزة المشمولة:", fontSize = 11.sp, color = Slate400)
          Text(
            text = "${client.deviceTypes} (${client.deviceCount} أجهزة)",
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = Slate200
          )
        }
        Column(horizontalAlignment = Alignment.End) {
          Text(text = "سريان الضمان حتى:", fontSize = 11.sp, color = Slate400)
          Text(
            text = client.warrantyExpiryDate,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = MedicalEmerald
          )
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Column {
          Text(text = "المسؤول والتواصل:", fontSize = 11.sp, color = Slate400)
          Text(
            text = "${client.contactPerson} (${client.phone})",
            fontSize = 12.sp,
            color = Slate200
          )
        }
        Column(horizontalAlignment = Alignment.End) {
          Text(text = "المهندس المتابع:", fontSize = 11.sp, color = Slate400)
          Text(
            text = client.assignedEngineer,
            fontSize = 12.sp,
            color = MedicalCyan
          )
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      Surface(
        color = MedicalNavyLight.copy(alpha = 0.5f),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 6.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "فحص ربع سنوي دوري مجاني + فحص ومعايرة المعاوقة (Impedance)",
            fontSize = 11.sp,
            color = MedicalEmerald
          )
          Text(
            text = "S/N: ${client.serialNumbers.joinToString(", ")}",
            fontSize = 10.sp,
            color = Slate400
          )
        }
      }
    }
  }
}
