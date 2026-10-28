package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.Contract
import com.example.data.model.OverdueSeverity
import com.example.ui.DeltaMedicalViewModel
import com.example.ui.MainNavigationTab
import com.example.ui.components.MetricKpiCard
import com.example.ui.components.SectionHeader
import com.example.ui.components.StatusBadge
import com.example.ui.theme.*

@Composable
fun DashboardScreen(
  viewModel: DeltaMedicalViewModel,
  modifier: Modifier = Modifier
) {
  val contracts by viewModel.contracts.collectAsState()
  val warrantyClients by viewModel.warrantyClients.collectAsState()
  val repairEstimates by viewModel.repairEstimates.collectAsState()
  val overdueTasks by viewModel.overdueTasks.collectAsState()
  val totalContractsValue by viewModel.totalContractsValue.collectAsState()
  val totalEstimatesValue by viewModel.totalEstimatesValue.collectAsState()
  val criticalOverdueCount by viewModel.criticalOverdueCount.collectAsState()

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // 1. Header Hero Card
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 8.dp)
          .testTag("hero_dashboard_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MedicalNavyCard)
      ) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .background(
              Brush.linearGradient(
                colors = listOf(
                  MedicalCyan.copy(alpha = 0.15f),
                  MedicalNavyLight.copy(alpha = 0.6f)
                )
              )
            )
            .padding(18.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MedicalNavyDark)
                .border(1.5.dp, MedicalCyan.copy(alpha = 0.6f), RoundedCornerShape(16.dp)),
              contentAlignment = Alignment.Center
            ) {
              Image(
                painter = painterResource(id = R.drawable.delta_medical_logo),
                contentDescription = "Delta Medical Logo",
                modifier = Modifier.size(54.dp).clip(RoundedCornerShape(14.dp))
              )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                  text = "دلتا الطبية - Delta Medical",
                  fontSize = 18.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                StatusBadge(text = "CMMS & ERP", color = MedicalCyan)
              }
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = "نظام إدارة دورة حياة أجهزة التشخيص العصبي (EMG & EEG)",
                fontSize = 12.sp,
                color = Slate400,
                maxLines = 2
              )
              Spacer(modifier = Modifier.height(6.dp))
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                  imageVector = Icons.Default.CheckCircle,
                  contentDescription = null,
                  tint = MedicalEmerald,
                  modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                  text = "11 جهازاً بعقود معتمدة + 6 أجهزة تحت الضمان (كفاءة 100%)",
                  fontSize = 11.sp,
                  color = MedicalEmerald,
                  fontWeight = FontWeight.SemiBold
                )
              }
            }
          }
        }
      }
    }

    // 2. Urgent Overdue Alert Banner (if critical overdue exists)
    if (criticalOverdueCount > 0) {
      item {
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { viewModel.setTab(MainNavigationTab.ENGINEERS) }
            .testTag("urgent_overdue_banner"),
          colors = CardDefaults.cardColors(containerColor = MedicalCrimsonBg),
          border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.horizontalGradient(listOf(MedicalCrimson, MedicalAmber))
          )
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(MedicalCrimson.copy(alpha = 0.25f)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = null,
                tint = MedicalCrimson,
                modifier = Modifier.size(24.dp)
              )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "تنبيه: يوجد $criticalOverdueCount مهام متأخرة تجاوزت 14 يوماً (حرج)",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = MedicalCrimson
              )
              Text(
                text = "مستشفى بنها الجامعي (16 يوم) ومركز الفتح (15 يوم) تتطلب تدخلاً فورياً",
                fontSize = 11.sp,
                color = Slate200
              )
            }

            Icon(
              imageVector = Icons.Default.ArrowBack,
              contentDescription = "الانتقال",
              tint = MedicalCrimson,
              modifier = Modifier.size(18.dp)
            )
          }
        }
      }
    }

    // 3. Core KPI Grid
    item {
      Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          MetricKpiCard(
            title = "عقود الصيانة السارية",
            value = "638,000 ج.م",
            subtitle = "9 عقود (11 جهازاً طبياً)",
            icon = Icons.Default.AssignmentTurnedIn,
            accentColor = MedicalCyan,
            badge = "سارية 100%",
            modifier = Modifier.weight(1f),
            onClick = { viewModel.setTab(MainNavigationTab.CONTRACTS) }
          )
          MetricKpiCard(
            title = "مقايسات الإصلاح",
            value = "234,000 ج.م",
            subtitle = "معهد ناصر + أبو الريش",
            icon = Icons.Default.Build,
            accentColor = MedicalAmber,
            badge = "2 مقايسة معتمدة",
            modifier = Modifier.weight(1f),
            onClick = { viewModel.setTab(MainNavigationTab.ESTIMATES) }
          )
        }

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          MetricKpiCard(
            title = "عملاء فترة الضمان",
            value = "6 أجهزة",
            subtitle = "3 عملاء (ضمان حتى 2027)",
            icon = Icons.Default.VerifiedUser,
            accentColor = MedicalEmerald,
            badge = "كفاءة 100%",
            modifier = Modifier.weight(1f),
            onClick = { viewModel.setTab(MainNavigationTab.CONTRACTS) }
          )
          MetricKpiCard(
            title = "المهام المتأخرة",
            value = "${overdueTasks.count { !it.isCompleted }} مهام",
            subtitle = "$criticalOverdueCount حرجة + 2 متوسطة",
            icon = Icons.Default.Schedule,
            accentColor = MedicalCrimson,
            badge = "محرك المتابعة",
            modifier = Modifier.weight(1f),
            onClick = { viewModel.setTab(MainNavigationTab.ENGINEERS) }
          )
        }
      }
    }

    // 4. Quick Action Shortcuts
    item {
      SectionHeader(title = "الإجراءات التشغيلية السريعة")
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        QuickActionButton(
          label = "فتح تذكرة صيانة",
          icon = Icons.Default.AddBox,
          color = MedicalCyan,
          modifier = Modifier.weight(1f),
          onClick = { viewModel.setShowNewTicketDialog(true) }
        )
        QuickActionButton(
          label = "مركز الشيتات",
          icon = Icons.Default.CloudDownload,
          color = MedicalEmerald,
          modifier = Modifier.weight(1f),
          onClick = { viewModel.setTab(MainNavigationTab.EXPORT) }
        )
        QuickActionButton(
          label = "قائمة PM الوقائية",
          icon = Icons.Default.Checklist,
          color = MedicalAmber,
          modifier = Modifier.weight(1f),
          onClick = { viewModel.setShowPmChecklist(true) }
        )
        QuickActionButton(
          label = "تقويم Google",
          icon = Icons.Default.CalendarMonth,
          color = Color(0xFF818CF8),
          modifier = Modifier.weight(1f),
          onClick = { viewModel.setShowAlertsDialog(true) }
        )
      }
    }

    // 5. Active Contracts Spotlight (Top 4 preview)
    item {
      SectionHeader(
        title = "عقود الصيانة المعتمدة (9 عقود - 638 ألف ج.م)",
        actionText = "عرض الكل",
        onActionClick = { viewModel.setTab(MainNavigationTab.CONTRACTS) }
      )
    }

    items(contracts.take(4)) { contract ->
      ContractSummaryCard(
        contract = contract,
        onRenewClick = { viewModel.openContractRenewal(contract) }
      )
    }

    // 6. Warranty Base Spotlight
    item {
      SectionHeader(
        title = "عملاء وأجهزة الضمان (3 عملاء - 6 أجهزة)",
        actionText = "عرض الكل",
        onActionClick = { viewModel.setTab(MainNavigationTab.CONTRACTS) }
      )
    }

    items(warrantyClients) { client ->
      WarrantyClientMiniCard(client = client)
    }

    item {
      Spacer(modifier = Modifier.height(24.dp))
    }
  }
}

@Composable
fun QuickActionButton(
  label: String,
  icon: ImageVector,
  color: Color,
  modifier: Modifier = Modifier,
  onClick: () -> Unit
) {
  Card(
    modifier = modifier
      .clip(RoundedCornerShape(12.dp))
      .clickable { onClick() }
      .testTag("quick_action_${label.replace(" ", "_")}"),
    colors = CardDefaults.cardColors(containerColor = MaterialNavyCard),
    shape = RoundedCornerShape(12.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 12.dp, horizontal = 4.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Box(
        modifier = Modifier
          .size(36.dp)
          .clip(CircleShape)
          .background(color.copy(alpha = 0.15f)),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = icon,
          contentDescription = null,
          tint = color,
          modifier = Modifier.size(20.dp)
        )
      }
      Spacer(modifier = Modifier.height(6.dp))
      Text(
        text = label,
        fontSize = 11.sp,
        fontWeight = FontWeight.Medium,
        color = Slate200,
        textAlign = TextAlign.Center,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
      )
    }
  }
}

@Composable
fun ContractSummaryCard(
  contract: Contract,
  onRenewClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .testTag("contract_summary_${contract.id}"),
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
            text = contract.facilityName,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
          )
          Text(
            text = "${contract.governorate} - ${contract.city}",
            fontSize = 11.sp,
            color = Slate400
          )
        }

        Surface(
          color = MedicalCyan.copy(alpha = 0.15f),
          shape = RoundedCornerShape(8.dp),
          border = androidx.compose.foundation.BorderStroke(1.dp, MedicalCyan.copy(alpha = 0.4f))
        ) {
          Text(
            text = "${String.format("%,d", contract.annualValue)} ج.م",
            color = MedicalCyan,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Text(
          text = "الجهاز: ${contract.deviceType}",
          fontSize = 12.sp,
          color = Slate200,
          fontWeight = FontWeight.Medium
        )
        Text(
          text = "الزيارات: ${contract.visitsCompleted}/${contract.visitsPerYear}",
          fontSize = 12.sp,
          color = MedicalEmerald,
          fontWeight = FontWeight.SemiBold
        )
      }

      Spacer(modifier = Modifier.height(8.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "المسؤول: ${contract.contactPerson} (${contract.phone})",
          fontSize = 11.sp,
          color = Slate400
        )
        if (contract.endDate.contains("2026-11")) {
          TextButton(
            onClick = onRenewClick,
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
          ) {
            Text(
              text = "تجديد العقد (60 يوم)",
              color = MedicalAmber,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }
    }
  }
}

@Composable
fun WarrantyClientMiniCard(
  client: com.example.data.model.WarrantyClient,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .testTag("warranty_client_${client.id}"),
    colors = CardDefaults.cardColors(containerColor = MaterialNavyCard),
    shape = RoundedCornerShape(14.dp),
    border = CardDefaults.outlinedCardBorder().copy(
      brush = Brush.horizontalGradient(listOf(MedicalEmerald.copy(alpha = 0.4f), Color.Transparent))
    )
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .size(44.dp)
          .clip(RoundedCornerShape(10.dp))
          .background(MedicalEmerald.copy(alpha = 0.15f)),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.Shield,
          contentDescription = null,
          tint = MedicalEmerald,
          modifier = Modifier.size(24.dp)
        )
      }

      Spacer(modifier = Modifier.width(12.dp))

      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = client.clientName,
          fontSize = 14.sp,
          fontWeight = FontWeight.Bold,
          color = Color.White
        )
        Text(
          text = "${client.deviceTypes} • ضمان حتى ${client.warrantyExpiryDate}",
          fontSize = 11.sp,
          color = Slate400
        )
        Text(
          text = "فحص وقائي مجاني كل 3 شهور • معايرة المعاوقة (Impedance)",
          fontSize = 10.sp,
          color = MedicalEmerald
        )
      }

      StatusBadge(text = "100%", color = MedicalEmerald)
    }
  }
}
