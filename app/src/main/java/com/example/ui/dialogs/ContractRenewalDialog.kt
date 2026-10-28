package com.example.ui.dialogs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.Contract
import com.example.ui.theme.*

@Composable
fun ContractRenewalDialog(
  contract: Contract,
  onDismiss: () -> Unit,
  onConfirm: (newEndDate: String) -> Unit
) {
  var newEndDate by remember { mutableStateOf("2027-11-20") }

  Dialog(onDismissRequest = onDismiss) {
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .testTag("contract_renewal_dialog"),
      colors = CardDefaults.cardColors(containerColor = MaterialNavyCard),
      shape = RoundedCornerShape(16.dp)
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(20.dp)
      ) {
        Text(
          text = "تجديد وتمديد عقد الصيانة السنوي",
          fontSize = 16.sp,
          fontWeight = FontWeight.Bold,
          color = Color.White
        )
        Text(
          text = "${contract.facilityName} (${String.format("%,d", contract.annualValue)} ج.م)",
          fontSize = 12.sp,
          color = MedicalCyan
        )

        Spacer(modifier = Modifier.height(14.dp))

        Text(
          text = "تاريخ الانتهاء الحالي: ${contract.endDate}",
          fontSize = 12.sp,
          color = Slate400
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
          value = newEndDate,
          onValueChange = { newEndDate = it },
          label = { Text("تاريخ الانتهاء الجديد (سنة إضافية)", fontSize = 12.sp) },
          modifier = Modifier.fillMaxWidth().testTag("new_end_date_input")
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
            onClick = { onConfirm(newEndDate) },
            colors = ButtonDefaults.buttonColors(containerColor = MedicalEmerald),
            modifier = Modifier.weight(1.2f).testTag("confirm_renew_btn")
          ) {
            Text("تأكيد التجديد", color = Color.White, fontWeight = FontWeight.Bold)
          }
        }
      }
    }
  }
}

@Composable
fun ReassignTaskDialog(
  task: com.example.data.model.OverdueTask,
  onDismiss: () -> Unit,
  onReassign: (newEngineer: String) -> Unit
) {
  val engineersList = listOf("م. أحمد حشاد", "م. محمد زيدان", "م. خالد يوسف")
  var selectedEngineer by remember { mutableStateOf(task.assignedEngineer) }

  Dialog(onDismissRequest = onDismiss) {
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .testTag("reassign_task_dialog"),
      colors = CardDefaults.cardColors(containerColor = MaterialNavyCard),
      shape = RoundedCornerShape(16.dp)
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(20.dp)
      ) {
        Text(
          text = "إعادة إسناد المهمة المتأخرة",
          fontSize = 16.sp,
          fontWeight = FontWeight.Bold,
          color = Color.White
        )
        Text(
          text = "${task.facilityName} - ${task.title}",
          fontSize = 12.sp,
          color = MedicalCyan
        )

        Spacer(modifier = Modifier.height(14.dp))

        Text(
          text = "اختر المهندس الجديد لتولي المهمة فورياً:",
          fontSize = 12.sp,
          color = Slate200
        )

        Spacer(modifier = Modifier.height(8.dp))

        engineersList.forEach { eng ->
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 4.dp),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
          ) {
            RadioButton(
              selected = selectedEngineer == eng,
              onClick = { selectedEngineer = eng },
              colors = RadioButtonDefaults.colors(selectedColor = MedicalCyan)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = eng, fontSize = 13.sp, color = Color.White)
          }
        }

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
            onClick = { onReassign(selectedEngineer) },
            colors = ButtonDefaults.buttonColors(containerColor = MedicalCyan),
            modifier = Modifier.weight(1.2f).testTag("confirm_reassign_btn")
          ) {
            Text("تأكيد الإسناد", color = MedicalNavyDark, fontWeight = FontWeight.Bold)
          }
        }
      }
    }
  }
}
