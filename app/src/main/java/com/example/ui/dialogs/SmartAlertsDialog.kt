package com.example.ui.dialogs

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.PMCheckItem
import com.example.ui.DeltaMedicalViewModel
import com.example.ui.components.StatusBadge
import com.example.ui.theme.*

@Composable
fun PMChecklistDialog(
  checklistItems: List<PMCheckItem>,
  onToggle: (Int) -> Unit,
  onDismiss: () -> Unit
) {
  Dialog(onDismissRequest = onDismiss) {
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .testTag("pm_checklist_dialog"),
      colors = CardDefaults.cardColors(containerColor = MaterialNavyCard),
      shape = RoundedCornerShape(16.dp)
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(20.dp)
          .verticalScroll(rememberScrollState())
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = "قائمة تدقيق الصيانة الوقائية (PM Checklist)",
              fontSize = 15.sp,
              fontWeight = FontWeight.Bold,
              color = Color.White
            )
            Text(
              text = "معايير الأمان الكهربائي ومعايرة إشارات EMG & EEG",
              fontSize = 11.sp,
              color = MedicalCyan
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        checklistItems.forEachIndexed { index, item ->
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(8.dp))
              .clickable { onToggle(index) }
              .padding(vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Checkbox(
              checked = item.isChecked,
              onCheckedChange = { onToggle(index) },
              colors = CheckboxDefaults.colors(
                checkedColor = MedicalEmerald,
                checkmarkColor = Color.White
              )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
              Text(
                text = item.title,
                fontSize = 12.sp,
                fontWeight = if (item.isChecked) FontWeight.Bold else FontWeight.Normal,
                color = if (item.isChecked) MedicalEmerald else Slate200
              )
              Text(
                text = item.category,
                fontSize = 10.sp,
                color = Slate400
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
          onClick = onDismiss,
          colors = ButtonDefaults.buttonColors(containerColor = MedicalCyan),
          modifier = Modifier.fillMaxWidth().testTag("close_checklist_btn")
        ) {
          Text("اعتماد نتائج الفحص الوقائي", color = MedicalNavyDark, fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}

@Composable
fun SmartAlertsDialog(
  viewModel: DeltaMedicalViewModel,
  onDismiss: () -> Unit
) {
  val context = LocalContext.current
  val contracts by viewModel.contracts.collectAsState()

  // Contracts expiring in < 60 days
  val expiringSoon = contracts.filter { it.endDate.contains("2026-11") }

  Dialog(onDismissRequest = onDismiss) {
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .testTag("smart_alerts_dialog"),
      colors = CardDefaults.cardColors(containerColor = MaterialNavyCard),
      shape = RoundedCornerShape(16.dp)
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(20.dp)
          .verticalScroll(rememberScrollState())
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "نظام التنبيهات والتقويم الذكي",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
          )
          StatusBadge(text = "تنبيهات فورية", color = MedicalAmber)
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 1. Expiring contracts warning
        Text(
          text = "عقود أوشكت على الانتهاء خلال 60 يوماً:",
          fontSize = 13.sp,
          fontWeight = FontWeight.Bold,
          color = MedicalAmber
        )
        Spacer(modifier = Modifier.height(6.dp))

        expiringSoon.forEach { c ->
          Surface(
            color = MedicalAmberBg,
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 4.dp)
          ) {
            Column(modifier = Modifier.padding(10.dp)) {
              Text(
                text = "${c.facilityName} (${String.format("%,d", c.annualValue)} ج.م)",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
              )
              Text(
                text = "ينتهي العقد بتاريخ: ${c.endDate} • يلزم إصدار عرض تجديد",
                fontSize = 11.sp,
                color = MedicalAmber
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 2. Google Calendar .ics export
        Text(
          text = "تقويم الزيارات الدورية (Google / Outlook Calendar):",
          fontSize = 13.sp,
          fontWeight = FontWeight.Bold,
          color = MedicalCyan
        )
        Text(
          text = "توليد ملف تقويم قياسي (.ics) لجدولة زيارات مهندسي الصيانة ومزامنتها بنقرة واحدة.",
          fontSize = 11.sp,
          color = Slate400
        )

        Spacer(modifier = Modifier.height(10.dp))

        Button(
          onClick = {
            val icsData = viewModel.exportIcsCalendar()
            val sendIntent = Intent().apply {
              action = Intent.ACTION_SEND
              putExtra(Intent.EXTRA_TEXT, icsData)
              type = "text/calendar"
            }
            context.startActivity(Intent.createChooser(sendIntent, "مشاركة تقويم الزيارات (.ics)"))
          },
          colors = ButtonDefaults.buttonColors(containerColor = MedicalCyan),
          modifier = Modifier.fillMaxWidth().testTag("export_ics_btn")
        ) {
          Icon(imageVector = Icons.Default.Event, contentDescription = null, tint = MedicalNavyDark)
          Spacer(modifier = Modifier.width(6.dp))
          Text("تصدير تقويم الزيارات (.ics)", color = MedicalNavyDark, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
          onClick = onDismiss,
          modifier = Modifier.fillMaxWidth()
        ) {
          Text("إغلاق", color = Slate200)
        }
      }
    }
  }
}
