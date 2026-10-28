package com.example.ui.dialogs

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.FieldServiceTicket
import com.example.ui.components.StatusBadge
import com.example.ui.theme.*

@Composable
fun JobOrderPrintDialog(
  ticket: FieldServiceTicket,
  onDismiss: () -> Unit
) {
  val context = LocalContext.current

  Dialog(onDismissRequest = onDismiss) {
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(16.dp))
        .testTag("job_order_dialog"),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      shape = RoundedCornerShape(16.dp)
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(18.dp)
          .verticalScroll(rememberScrollState())
      ) {
        // Document Header
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = "شركة دلتا الطبية - Delta Medical",
              fontSize = 15.sp,
              fontWeight = FontWeight.Bold,
              color = Slate900
            )
            Text(
              text = "قسم الصيانة الطبية الحيوية والتشخيص العصبي",
              fontSize = 11.sp,
              color = Slate600
            )
          }
          Surface(
            color = MedicalCyan.copy(alpha = 0.15f),
            shape = RoundedCornerShape(6.dp)
          ) {
            Text(
              text = "أمر شغل / Job Order",
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF0369A1),
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
          }
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = Slate200)

        // Ticket Details Box
        Text(
          text = "بيانات أمر الشغل رقم: ${ticket.id}",
          fontSize = 13.sp,
          fontWeight = FontWeight.Bold,
          color = Slate900
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "العميل / المنشأة: ${ticket.clientName}", fontSize = 12.sp, color = Slate800)
        Text(text = "الموقع والعنوان: ${ticket.location}", fontSize = 11.sp, color = Slate600)
        Text(text = "طراز الجهاز: ${ticket.deviceModel}", fontSize = 12.sp, color = Slate800)
        Text(text = "تاريخ البلاغ: ${ticket.reportDate}", fontSize = 11.sp, color = Slate600)
        Text(text = "تصنيف العطل: ${ticket.breakdownType.labelArabic}", fontSize = 12.sp, color = Slate800)
        Text(text = "المهندس المسؤول: ${ticket.assignedEngineer}", fontSize = 12.sp, color = Slate800)

        Spacer(modifier = Modifier.height(8.dp))
        Surface(
          color = Slate100,
          shape = RoundedCornerShape(8.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text(text = "رسم المعاينة والانتقال:", fontSize = 12.sp, color = Slate800)
            Text(
              text = "${String.format("%,d", ticket.inspectionFee)} ج.م (${if (ticket.isFeeCollected) "تم التحصيل" else "قيد التحصيل"})",
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF059669)
            )
          }
        }

        // Diagnostic Details if available
        val diag = ticket.diagnosticReport
        if (diag != null) {
          Spacer(modifier = Modifier.height(10.dp))
          Text(
            text = "نتائج الفحص والتشخيص الفني:",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Slate900
          )
          Text(text = diag.findings, fontSize = 11.sp, color = Slate800)
          if (diag.pcbBoardSerialNumbers.isNotEmpty()) {
            Text(
              text = "لوحات PCB: ${diag.pcbBoardSerialNumbers.joinToString(", ")}",
              fontSize = 10.sp,
              color = Slate600
            )
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Signatures area
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "توقيع مهندس الصيانة", fontSize = 11.sp, color = Slate600)
            Spacer(modifier = Modifier.height(24.dp))
            Text(text = ".......................", color = Slate400)
          }
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "توقيع واستلام المستشفى", fontSize = 11.sp, color = Slate600)
            Spacer(modifier = Modifier.height(24.dp))
            Text(text = ".......................", color = Slate400)
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Action buttons
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          OutlinedButton(
            onClick = onDismiss,
            modifier = Modifier.weight(1f)
          ) {
            Text(text = "إغلاق", color = Slate800)
          }

          Button(
            onClick = {
              val shareText = """
                أمر شغل رسمي - شركة دلتا الطبية
                رقم التذكرة: ${ticket.id}
                العميل: ${ticket.clientName}
                الجهاز: ${ticket.deviceModel}
                تاريخ البلاغ: ${ticket.reportDate}
                المهندس المكلف: ${ticket.assignedEngineer}
                رسم المعاينة: ${ticket.inspectionFee} ج.م
                الحالة: معتمد رسمياً
              """.trimIndent()
              val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, shareText)
                type = "text/plain"
              }
              context.startActivity(Intent.createChooser(sendIntent, "مشاركة أمر الشغل"))
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7)),
            modifier = Modifier.weight(1f)
          ) {
            Icon(imageVector = Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = "مشاركة / طباعة", color = Color.White)
          }
        }
      }
    }
  }
}
