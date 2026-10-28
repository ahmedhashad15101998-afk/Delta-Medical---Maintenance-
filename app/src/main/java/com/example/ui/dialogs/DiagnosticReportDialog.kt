package com.example.ui.dialogs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.DiagnosticReport
import com.example.data.model.FieldServiceTicket
import com.example.ui.theme.*

@Composable
fun DiagnosticReportDialog(
  ticket: FieldServiceTicket,
  onDismiss: () -> Unit,
  onSave: (DiagnosticReport) -> Unit
) {
  var findings by remember { mutableStateOf("") }
  var damagedPartsText by remember { mutableStateOf("") }
  var pcbSerialText by remember { mutableStateOf("") }
  var recommendations by remember { mutableStateOf("") }
  var estimatedCostText by remember { mutableStateOf("35000") }

  Dialog(onDismissRequest = onDismiss) {
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .testTag("diagnostic_report_dialog"),
      colors = CardDefaults.cardColors(containerColor = MaterialNavyCard),
      shape = RoundedCornerShape(16.dp)
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(18.dp)
          .verticalScroll(rememberScrollState())
      ) {
        Text(
          text = "تقرير المعاينة والتشخيص الفني الميداني",
          fontSize = 16.sp,
          fontWeight = FontWeight.Bold,
          color = Color.White
        )
        Text(
          text = "${ticket.clientName} • ${ticket.deviceModel}",
          fontSize = 11.sp,
          color = MedicalCyan
        )

        Spacer(modifier = Modifier.height(14.dp))

        OutlinedTextField(
          value = findings,
          onValueChange = { findings = it },
          label = { Text("نتائج الفحص والتشخيص الميداني", fontSize = 12.sp) },
          placeholder = { Text("مثال: تلف وحدة التغذية الكهربائية وتشويش إشارة العصب الحسي", fontSize = 11.sp) },
          modifier = Modifier.fillMaxWidth().testTag("findings_input"),
          minLines = 2
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
          value = damagedPartsText,
          onValueChange = { damagedPartsText = it },
          label = { Text("القطع التالفة المطلوب استبدالها (مفصولة بفواصل)", fontSize = 12.sp) },
          placeholder = { Text("مثال: وحدة Power Supply، كابل تحفيز", fontSize = 11.sp) },
          modifier = Modifier.fillMaxWidth().testTag("parts_input")
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
          value = pcbSerialText,
          onValueChange = { pcbSerialText = it },
          label = { Text("أرقام اللوحات الإلكترونية (PCB S/N)", fontSize = 12.sp) },
          placeholder = { Text("مثال: PCB-HV-9921, DSP-AMP-04", fontSize = 11.sp) },
          modifier = Modifier.fillMaxWidth().testTag("pcb_input")
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
          value = recommendations,
          onValueChange = { recommendations = it },
          label = { Text("التوصيات الهندسية المعتمدة", fontSize = 12.sp) },
          placeholder = { Text("مثال: استبدال فوري وإعادة معايرة المعاوقة", fontSize = 11.sp) },
          modifier = Modifier.fillMaxWidth().testTag("recommendations_input")
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
          value = estimatedCostText,
          onValueChange = { estimatedCostText = it },
          label = { Text("التكلفة التقديرية للإصلاح (ج.م)", fontSize = 12.sp) },
          modifier = Modifier.fillMaxWidth().testTag("cost_input")
        )

        Spacer(modifier = Modifier.height(18.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          OutlinedButton(
            onClick = onDismiss,
            modifier = Modifier.weight(1f)
          ) {
            Text("إلغاء", color = Slate200)
          }

          Button(
            onClick = {
              val cost = estimatedCostText.toLongOrNull() ?: 35000L
              val parts = damagedPartsText.split("،", ",").map { it.trim() }.filter { it.isNotEmpty() }
              val pcbs = pcbSerialText.split("،", ",").map { it.trim() }.filter { it.isNotEmpty() }
              val report = DiagnosticReport(
                reportDate = "2026-10-28",
                findings = findings.ifBlank { "تمت المعاينة الميدانية وفحص الدوائر الإلكترونية وكابلات الإشارة" },
                damagedParts = if (parts.isNotEmpty()) parts else listOf("كابل الإشارة المرجعي", "دائرة التغذية"),
                pcbBoardSerialNumbers = if (pcbs.isNotEmpty()) pcbs else listOf("PCB-DEF-8812"),
                technicalRecommendations = recommendations.ifBlank { "إجراء صيانة فورية ومعايرة إشارة EMG / EEG" },
                estimatedRepairCost = cost
              )
              onSave(report)
            },
            colors = ButtonDefaults.buttonColors(containerColor = MedicalCyan),
            modifier = Modifier.weight(1.2f).testTag("save_report_btn")
          ) {
            Text("حفظ التقرير الفني", color = MedicalNavyDark, fontWeight = FontWeight.Bold)
          }
        }
      }
    }
  }
}
