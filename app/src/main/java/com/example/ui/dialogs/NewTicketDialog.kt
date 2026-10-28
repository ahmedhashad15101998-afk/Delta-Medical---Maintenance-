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
import com.example.data.model.BreakdownType
import com.example.ui.theme.*

@Composable
fun NewTicketDialog(
  onDismiss: () -> Unit,
  onCreate: (
    clientName: String,
    deviceModel: String,
    breakdownType: BreakdownType,
    assignedEngineer: String,
    inspectionFee: Long,
    phone: String,
    location: String
  ) -> Unit
) {
  var clientName by remember { mutableStateOf("") }
  var deviceModel by remember { mutableStateOf("جهاز رسم عضلات EMG 4ch") }
  var isCompleteBreakdown by remember { mutableStateOf(false) }
  var assignedEngineer by remember { mutableStateOf("م. أحمد حشاد") }
  var feeText by remember { mutableStateOf("2500") }
  var phone by remember { mutableStateOf("010...") }
  var location by remember { mutableStateOf("القاهرة") }

  Dialog(onDismissRequest = onDismiss) {
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .testTag("new_ticket_dialog"),
      colors = CardDefaults.cardColors(containerColor = MaterialNavyCard),
      shape = RoundedCornerShape(16.dp)
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(20.dp)
          .verticalScroll(rememberScrollState())
      ) {
        Text(
          text = "تسجيل تذكرة خدمة وبلاغ عطل جديد",
          fontSize = 16.sp,
          fontWeight = FontWeight.Bold,
          color = Color.White
        )
        Text(
          text = "تسجيل رسوم المعاينة وتكليف مهندس الفحص الميداني",
          fontSize = 11.sp,
          color = MedicalCyan
        )

        Spacer(modifier = Modifier.height(14.dp))

        OutlinedTextField(
          value = clientName,
          onValueChange = { clientName = it },
          label = { Text("اسم المستشفى / الطبيب / المركز", fontSize = 12.sp) },
          placeholder = { Text("مثال: مستشفى دار الفؤاد", fontSize = 11.sp) },
          modifier = Modifier.fillMaxWidth().testTag("ticket_client_input")
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
          value = deviceModel,
          onValueChange = { deviceModel = it },
          label = { Text("طراز الجهاز الطبي", fontSize = 12.sp) },
          modifier = Modifier.fillMaxWidth().testTag("ticket_device_input")
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text("تصنيف العطل الميداني:", fontSize = 11.sp, color = Slate200)
        Row(modifier = Modifier.fillMaxWidth()) {
          Row(
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
          ) {
            RadioButton(
              selected = isCompleteBreakdown,
              onClick = { isCompleteBreakdown = true },
              colors = RadioButtonDefaults.colors(selectedColor = MedicalCrimson)
            )
            Text("عطل كلي (توقف)", fontSize = 11.sp, color = Color.White)
          }
          Row(
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
          ) {
            RadioButton(
              selected = !isCompleteBreakdown,
              onClick = { isCompleteBreakdown = false },
              colors = RadioButtonDefaults.colors(selectedColor = MedicalAmber)
            )
            Text("عطل جزئي (إشارة/كابل)", fontSize = 11.sp, color = Color.White)
          }
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
          value = feeText,
          onValueChange = { feeText = it },
          label = { Text("رسم الانتقال والمعاينة (ج.م)", fontSize = 12.sp) },
          modifier = Modifier.fillMaxWidth().testTag("ticket_fee_input")
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
          value = location,
          onValueChange = { location = it },
          label = { Text("الموقع والمدينة", fontSize = 12.sp) },
          modifier = Modifier.fillMaxWidth()
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
              val name = clientName.ifBlank { "مستشفى الدقي التخصصي" }
              val fee = feeText.toLongOrNull() ?: 2500L
              onCreate(
                name,
                deviceModel,
                if (isCompleteBreakdown) BreakdownType.COMPLETE else BreakdownType.PARTIAL,
                assignedEngineer,
                fee,
                phone,
                location
              )
            },
            colors = ButtonDefaults.buttonColors(containerColor = MedicalCyan),
            modifier = Modifier.weight(1.2f).testTag("submit_new_ticket_btn")
          ) {
            Text("فتح التذكرة", color = MedicalNavyDark, fontWeight = FontWeight.Bold)
          }
        }
      }
    }
  }
}
