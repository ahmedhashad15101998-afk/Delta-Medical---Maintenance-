package com.example.ui.screens

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.DeltaMedicalViewModel
import com.example.ui.components.SectionHeader
import com.example.ui.components.StatusBadge
import com.example.ui.theme.*

@Composable
fun ExportCenterScreen(
  viewModel: DeltaMedicalViewModel,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  var selectedExportTarget by remember { mutableStateOf(0) } // 0: Contracts (9), 1: Warranty (3), 2: Combined
  val contracts by viewModel.contracts.collectAsState()
  val warrantyClients by viewModel.warrantyClients.collectAsState()

  Column(
    modifier = modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp)
  ) {
    // Header
    SectionHeader(
      title = "مركز سحب وتصدير الشيتات (Export Center)",
      subtitle = "تصدير متوافق 100% مع Microsoft Excel وGoogle Sheets مع دعم كامل للأحرف العربية (UTF-8 BOM)"
    )

    // Export Dataset Selector
    TabRow(
      selectedTabIndex = selectedExportTarget,
      containerColor = MaterialNavyCard,
      contentColor = MedicalCyan,
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(12.dp))
        .padding(vertical = 8.dp)
    ) {
      Tab(
        selected = selectedExportTarget == 0,
        onClick = { selectedExportTarget = 0 },
        text = { Text("عقود الصيانة (9)", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
      )
      Tab(
        selected = selectedExportTarget == 1,
        onClick = { selectedExportTarget = 1 },
        text = { Text("أجهزة الضمان (3)", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
      )
      Tab(
        selected = selectedExportTarget == 2,
        onClick = { selectedExportTarget = 2 },
        text = { Text("الجدول المدمج الشامل", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
      )
    }

    // Export Actions Toolbar
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialNavyLight.copy(alpha = 0.5f)),
      shape = RoundedCornerShape(14.dp),
      border = CardDefaults.outlinedCardBorder().copy(
        brush = Brush.horizontalGradient(listOf(MedicalCyan.copy(alpha = 0.4f), Color.Transparent))
      )
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(14.dp)
      ) {
        Text(
          text = "خيارات التصدير السريع:",
          fontSize = 13.sp,
          fontWeight = FontWeight.Bold,
          color = Color.White
        )
        Spacer(modifier = Modifier.height(10.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          // 1. Copy TSV Button
          Button(
            onClick = {
              val target = when (selectedExportTarget) {
                0 -> "contracts"
                1 -> "warranty"
                else -> "combined"
              }
              viewModel.copyTsvToClipboard(target)
            },
            colors = ButtonDefaults.buttonColors(containerColor = MedicalEmerald),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.weight(1f).testTag("copy_tsv_button")
          ) {
            Icon(
              imageVector = Icons.Default.ContentCopy,
              contentDescription = null,
              tint = Color.White,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "نسخ للشيتات (TSV)",
              color = Color.White,
              fontWeight = FontWeight.Bold,
              fontSize = 11.sp
            )
          }

          // 2. CSV UTF-8 BOM Button
          Button(
            onClick = {
              val target = when (selectedExportTarget) {
                0 -> "contracts"
                1 -> "warranty"
                else -> "combined"
              }
              val csvContent = viewModel.downloadCsvUtf8Bom(target)
              val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, csvContent)
                type = "text/plain"
              }
              val shareIntent = Intent.createChooser(sendIntent, "مشاركة ملف CSV المرمز")
              context.startActivity(shareIntent)
            },
            colors = ButtonDefaults.buttonColors(containerColor = MedicalCyan),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.weight(1f).testTag("export_csv_bom_button")
          ) {
            Icon(
              imageVector = Icons.Default.FileDownload,
              contentDescription = null,
              tint = MedicalNavyDark,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "CSV (UTF-8 BOM)",
              color = MedicalNavyDark,
              fontWeight = FontWeight.Bold,
              fontSize = 11.sp
            )
          }
        }
      }
    }

    // Live Spreadsheet Preview Table
    Text(
      text = "معاينة جدول البيانات للشيتات:",
      fontSize = 13.sp,
      fontWeight = FontWeight.Bold,
      color = Slate200,
      modifier = Modifier.padding(vertical = 4.dp)
    )

    Card(
      modifier = Modifier
        .fillMaxWidth()
        .weight(1f),
      colors = CardDefaults.cardColors(containerColor = MaterialNavyCard),
      shape = RoundedCornerShape(12.dp)
    ) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .horizontalScroll(rememberScrollState())
          .padding(8.dp)
      ) {
        LazyColumn(
          modifier = Modifier.fillMaxHeight(),
          verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          if (selectedExportTarget == 0) {
            // Header Row
            item {
              Row(
                modifier = Modifier
                  .background(MedicalNavyLight, RoundedCornerShape(6.dp))
                  .padding(8.dp)
              ) {
                TableCell("م", 30.dp, isHeader = true)
                TableCell("اسم المنشأة", 180.dp, isHeader = true)
                TableCell("المحافظة", 80.dp, isHeader = true)
                TableCell("قيمة العقد", 90.dp, isHeader = true)
                TableCell("الأجهزة", 140.dp, isHeader = true)
                TableCell("الزيارات", 70.dp, isHeader = true)
                TableCell("المسؤول", 100.dp, isHeader = true)
                TableCell("رقم الهاتف", 100.dp, isHeader = true)
                TableCell("المهندس", 100.dp, isHeader = true)
              }
            }

            items(contracts.size) { index ->
              val c = contracts[index]
              Row(
                modifier = Modifier
                  .background(if (index % 2 == 0) MedicalNavy else MedicalNavyCard)
                  .padding(8.dp)
              ) {
                TableCell("${index + 1}", 30.dp)
                TableCell(c.facilityName, 180.dp, isBold = true)
                TableCell(c.governorate, 80.dp)
                TableCell("${String.format("%,d", c.annualValue)} ج.م", 90.dp, color = MedicalCyan)
                TableCell(c.deviceType, 140.dp)
                TableCell("${c.visitsCompleted}/${c.visitsPerYear}", 70.dp, color = MedicalEmerald)
                TableCell(c.contactPerson, 100.dp)
                TableCell(c.phone, 100.dp)
                TableCell(c.assignedEngineer, 100.dp)
              }
            }
          } else if (selectedExportTarget == 1) {
            // Warranty Header Row
            item {
              Row(
                modifier = Modifier
                  .background(MedicalNavyLight, RoundedCornerShape(6.dp))
                  .padding(8.dp)
              ) {
                TableCell("م", 30.dp, isHeader = true)
                TableCell("اسم العميل / المركز", 200.dp, isHeader = true)
                TableCell("المدينة والموقع", 120.dp, isHeader = true)
                TableCell("الأجهزة", 160.dp, isHeader = true)
                TableCell("تاريخ انتهاء الضمان", 110.dp, isHeader = true)
                TableCell("المسؤول", 100.dp, isHeader = true)
                TableCell("رقم الهاتف", 100.dp, isHeader = true)
                TableCell("المهندس", 100.dp, isHeader = true)
              }
            }

            items(warrantyClients.size) { index ->
              val w = warrantyClients[index]
              Row(
                modifier = Modifier
                  .background(if (index % 2 == 0) MedicalNavy else MedicalNavyCard)
                  .padding(8.dp)
              ) {
                TableCell("${index + 1}", 30.dp)
                TableCell(w.clientName, 200.dp, isBold = true)
                TableCell("${w.city} - ${w.location}", 120.dp)
                TableCell(w.deviceTypes, 160.dp)
                TableCell(w.warrantyExpiryDate, 110.dp, color = MedicalEmerald)
                TableCell(w.contactPerson, 100.dp)
                TableCell(w.phone, 100.dp)
                TableCell(w.assignedEngineer, 100.dp)
              }
            }
          } else {
            // Combined Header Row
            item {
              Row(
                modifier = Modifier
                  .background(MedicalNavyLight, RoundedCornerShape(6.dp))
                  .padding(8.dp)
              ) {
                TableCell("النوع", 80.dp, isHeader = true)
                TableCell("اسم المنشأة", 190.dp, isHeader = true)
                TableCell("المحافظة / المدينة", 120.dp, isHeader = true)
                TableCell("الأجهزة", 160.dp, isHeader = true)
                TableCell("القيمة / الضمان", 120.dp, isHeader = true)
                TableCell("المسؤول", 100.dp, isHeader = true)
                TableCell("رقم الهاتف", 100.dp, isHeader = true)
              }
            }

            val combined = contracts.map {
              Triple("عقد صيانة", it.facilityName, "${String.format("%,d", it.annualValue)} ج.م") to it
            } + warrantyClients.map {
              Triple("فترة ضمان", it.clientName, "ضمان ${it.warrantyExpiryDate}") to it
            }

            items(combined.size) { index ->
              val pair = combined[index]
              val type = pair.first.first
              val name = pair.first.second
              val valOrWar = pair.first.third

              Row(
                modifier = Modifier
                  .background(if (index % 2 == 0) MedicalNavy else MedicalNavyCard)
                  .padding(8.dp)
              ) {
                TableCell(type, 80.dp, color = if (type == "عقد صيانة") MedicalCyan else MedicalEmerald)
                TableCell(name, 190.dp, isBold = true)
                TableCell("مصر", 120.dp)
                TableCell("أجهزة EMG / EEG", 160.dp)
                TableCell(valOrWar, 120.dp, isBold = true)
                TableCell("معتمد", 100.dp)
                TableCell("010...", 100.dp)
              }
            }
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(16.dp))
  }
}

@Composable
fun TableCell(
  text: String,
  width: androidx.compose.ui.unit.Dp,
  isHeader: Boolean = false,
  isBold: Boolean = false,
  color: Color = if (isHeader) MedicalCyan else Slate200
) {
  Box(
    modifier = Modifier
      .width(width)
      .padding(horizontal = 4.dp),
    contentAlignment = Alignment.CenterStart
  ) {
    Text(
      text = text,
      fontSize = if (isHeader) 11.sp else 11.sp,
      fontWeight = if (isHeader || isBold) FontWeight.Bold else FontWeight.Normal,
      color = color,
      maxLines = 1
    )
  }
}
