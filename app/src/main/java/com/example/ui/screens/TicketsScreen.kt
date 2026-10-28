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
import com.example.data.model.BreakdownType
import com.example.data.model.FieldServiceTicket
import com.example.data.model.TicketStatus
import com.example.ui.DeltaMedicalViewModel
import com.example.ui.components.StatusBadge
import com.example.ui.theme.*

@Composable
fun TicketsScreen(
  viewModel: DeltaMedicalViewModel,
  modifier: Modifier = Modifier
) {
  val tickets by viewModel.tickets.collectAsState()

  Column(
    modifier = modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp)
  ) {
    // Top Action Bar
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column {
        Text(
          text = "تذاكر الخدمة والمعاينة الميدانية",
          fontSize = 18.sp,
          fontWeight = FontWeight.Bold,
          color = Color.White
        )
        Text(
          text = "دورة العمل: بلاغ -> معاينة -> تشخيص -> تحويل لمقايسة",
          fontSize = 11.sp,
          color = Slate400
        )
      }

      Button(
        onClick = { viewModel.setShowNewTicketDialog(true) },
        colors = ButtonDefaults.buttonColors(containerColor = MedicalCyan),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.testTag("create_ticket_btn")
      ) {
        Icon(
          imageVector = Icons.Default.Add,
          contentDescription = null,
          tint = MedicalNavyDark,
          modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
          text = "فتح تذكرة",
          color = MedicalNavyDark,
          fontWeight = FontWeight.Bold,
          fontSize = 12.sp
        )
      }
    }

    LazyColumn(
      modifier = Modifier.fillMaxSize(),
      verticalArrangement = Arrangement.spacedBy(12.dp),
      contentPadding = PaddingValues(bottom = 24.dp)
    ) {
      items(tickets) { ticket ->
        TicketCard(
          ticket = ticket,
          onOpenDiagnostic = { viewModel.openDiagnosticReport(ticket) },
          onConvertToEstimate = { viewModel.convertTicketToEstimate(ticket.id) },
          onOpenJobOrder = { viewModel.openJobOrder(ticket) }
        )
      }
    }
  }
}

@Composable
fun TicketCard(
  ticket: FieldServiceTicket,
  onOpenDiagnostic: () -> Unit,
  onConvertToEstimate: () -> Unit,
  onOpenJobOrder: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .testTag("ticket_card_${ticket.id}"),
    colors = CardDefaults.cardColors(containerColor = MaterialNavyCard),
    shape = RoundedCornerShape(14.dp),
    border = CardDefaults.outlinedCardBorder().copy(
      brush = Brush.horizontalGradient(
        if (ticket.breakdownType == BreakdownType.COMPLETE)
          listOf(MedicalCrimson.copy(alpha = 0.4f), Color.Transparent)
        else
          listOf(MedicalAmber.copy(alpha = 0.4f), Color.Transparent)
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
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = ticket.id,
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              color = MedicalCyan
            )
            Spacer(modifier = Modifier.width(8.dp))
            StatusBadge(
              text = ticket.breakdownType.labelArabic,
              color = if (ticket.breakdownType == BreakdownType.COMPLETE) MedicalCrimson else MedicalAmber
            )
          }
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = ticket.clientName,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
          )
          Text(
            text = "${ticket.deviceModel} • ${ticket.location}",
            fontSize = 11.sp,
            color = Slate400
          )
        }

        Surface(
          color = MedicalNavyLight.copy(alpha = 0.6f),
          shape = RoundedCornerShape(8.dp)
        ) {
          Column(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalAlignment = Alignment.End
          ) {
            Text(
              text = "رسم المعاينة: ${String.format("%,d", ticket.inspectionFee)} ج.م",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = Slate200
            )
            Text(
              text = if (ticket.isFeeCollected) "تم التحصيل ✓" else "قيد التحصيل",
              fontSize = 10.sp,
              color = if (ticket.isFeeCollected) MedicalEmerald else MedicalAmber,
              fontWeight = FontWeight.SemiBold
            )
          }
        }
      }

      HorizontalDivider(
        modifier = Modifier.padding(vertical = 10.dp),
        color = MedicalNavyLight
      )

      // Diagnostic Report section if available
      val diag = ticket.diagnosticReport
      if (diag != null) {
        Surface(
          color = MedicalNavyDark.copy(alpha = 0.5f),
          shape = RoundedCornerShape(10.dp),
          modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
        ) {
          Column(modifier = Modifier.padding(10.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "تقرير المعاينة والتشخيص الفني الميداني:",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = MedicalCyan
              )
              Text(
                text = diag.reportDate,
                fontSize = 10.sp,
                color = Slate400
              )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = diag.findings,
              fontSize = 12.sp,
              color = Slate200
            )
            if (diag.damagedParts.isNotEmpty()) {
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = "القطع التالفة واللوحات: ${diag.damagedParts.joinToString("، ")}",
                fontSize = 11.sp,
                color = MedicalAmber
              )
            }
            if (diag.pcbBoardSerialNumbers.isNotEmpty()) {
              Text(
                text = "أرقام لوحات PCB: ${diag.pcbBoardSerialNumbers.joinToString(" | ")}",
                fontSize = 10.sp,
                color = Slate400
              )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "التوصية: ${diag.technicalRecommendations}",
              fontSize = 11.sp,
              color = MedicalEmerald
            )
          }
        }
      }

      // Action buttons: Job Order, Diagnostic, Convert to Estimate
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        OutlinedButton(
          onClick = onOpenJobOrder,
          modifier = Modifier.weight(1f).testTag("job_order_btn_${ticket.id}"),
          shape = RoundedCornerShape(8.dp),
          contentPadding = PaddingValues(vertical = 4.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Print,
            contentDescription = null,
            modifier = Modifier.size(14.dp),
            tint = MedicalCyan
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(text = "أمر الشغل", fontSize = 11.sp, color = MedicalCyan)
        }

        if (diag == null) {
          Button(
            onClick = onOpenDiagnostic,
            modifier = Modifier.weight(1.2f).testTag("record_diag_btn_${ticket.id}"),
            colors = ButtonDefaults.buttonColors(containerColor = MedicalNavyLight),
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(vertical = 4.dp)
          ) {
            Icon(
              imageVector = Icons.Default.EditNote,
              contentDescription = null,
              modifier = Modifier.size(14.dp),
              tint = Color.White
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = "تسجيل التقرير", fontSize = 11.sp, color = Color.White)
          }
        } else if (ticket.status != TicketStatus.CONVERTED_TO_ESTIMATE) {
          Button(
            onClick = onConvertToEstimate,
            modifier = Modifier.weight(1.4f).testTag("convert_estimate_btn_${ticket.id}"),
            colors = ButtonDefaults.buttonColors(containerColor = MedicalAmber),
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(vertical = 4.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Transform,
              contentDescription = null,
              modifier = Modifier.size(14.dp),
              tint = MedicalNavyDark
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "تحويل لمقايسة إصلاح",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = MedicalNavyDark
            )
          }
        } else {
          Surface(
            color = MedicalEmeraldBg,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.weight(1.4f)
          ) {
            Row(
              modifier = Modifier.padding(vertical = 6.dp),
              horizontalArrangement = Arrangement.Center,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = MedicalEmerald,
                modifier = Modifier.size(14.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = "تم تحويلها لمقايسة",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = MedicalEmerald
              )
            }
          }
        }
      }
    }
  }
}
