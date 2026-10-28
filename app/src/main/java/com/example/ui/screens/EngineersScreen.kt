package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import com.example.data.model.Engineer
import com.example.data.model.OverdueSeverity
import com.example.data.model.OverdueTask
import com.example.ui.DeltaMedicalViewModel
import com.example.ui.components.SectionHeader
import com.example.ui.components.StatusBadge
import com.example.ui.theme.*

@Composable
fun EngineersScreen(
  viewModel: DeltaMedicalViewModel,
  modifier: Modifier = Modifier
) {
  var selectedTab by remember { mutableStateOf(0) } // 0: Overdue Engine, 1: Portfolio
  var selectedEngineerFilter by remember { mutableStateOf("الكل") }

  val engineers by viewModel.engineers.collectAsState()
  val overdueTasks by viewModel.overdueTasks.collectAsState()

  val activeOverdueTasks = overdueTasks.filter { !it.isCompleted }
  val filteredTasks = activeOverdueTasks.filter {
    selectedEngineerFilter == "الكل" || it.assignedEngineer == selectedEngineerFilter
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp)
  ) {
    // Top Tabs: Overdue Tasks Engine vs Engineers Portfolio
    TabRow(
      selectedTabIndex = selectedTab,
      containerColor = MaterialNavyCard,
      contentColor = MedicalCyan,
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(12.dp))
        .padding(vertical = 8.dp)
    ) {
      Tab(
        selected = selectedTab == 0,
        onClick = { selectedTab = 0 },
        text = {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text("المهام المتأخرة", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            if (activeOverdueTasks.isNotEmpty()) {
              Spacer(modifier = Modifier.width(6.dp))
              Surface(
                color = MedicalCrimson,
                shape = CircleShape
              ) {
                Text(
                  text = "${activeOverdueTasks.size}",
                  color = Color.White,
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold,
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
              }
            }
          }
        }
      )
      Tab(
        selected = selectedTab == 1,
        onClick = { selectedTab = 1 },
        text = {
          Text("بطاقات المهندسين (${engineers.size})", fontWeight = FontWeight.Bold, fontSize = 13.sp)
        }
      )
    }

    if (selectedTab == 0) {
      // Overdue Tasks Engine View
      SectionHeader(
        title = "محرك المهام المتأخرة (Overdue Engine)",
        subtitle = "تتبع فوري للزيارات والمعاينات المتأخرة حسب شدة التأخير"
      )

      // Filter by engineer
      val engineerFilterOptions = listOf("الكل", "م. أحمد حشاد", "م. محمد زيدان", "م. خالد يوسف")
      LazyRow(
        modifier = Modifier
          .fillMaxWidth()
          .padding(bottom = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        items(engineerFilterOptions) { eng ->
          FilterChip(
            selected = selectedEngineerFilter == eng,
            onClick = { selectedEngineerFilter = eng },
            label = { Text(eng, fontSize = 12.sp) },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = MedicalCyan.copy(alpha = 0.2f),
              selectedLabelColor = MedicalCyan,
              containerColor = MaterialNavyCard,
              labelColor = Slate400
            )
          )
        }
      }

      LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
      ) {
        if (filteredTasks.isEmpty()) {
          item {
            Card(
              modifier = Modifier.fillMaxWidth(),
              colors = CardDefaults.cardColors(containerColor = MaterialNavyCard)
            ) {
              Column(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
              ) {
                Icon(
                  imageVector = Icons.Default.CheckCircleOutline,
                  contentDescription = null,
                  tint = MedicalEmerald,
                  modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                  text = "لا توجد مهام متأخرة لهذا المهندس!",
                  fontSize = 15.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color.White
                )
              }
            }
          }
        } else {
          items(filteredTasks) { task ->
            OverdueTaskCard(
              task = task,
              onComplete = { viewModel.completeTask(task.id) },
              onReassign = { viewModel.openReassignTask(task) }
            )
          }
        }
      }
    } else {
      // Engineers Portfolio View
      LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
      ) {
        items(engineers) { engineer ->
          EngineerCard(engineer = engineer)
        }
      }
    }
  }
}

@Composable
fun OverdueTaskCard(
  task: OverdueTask,
  onComplete: () -> Unit,
  onReassign: () -> Unit,
  modifier: Modifier = Modifier
) {
  val isCritical = task.severity == OverdueSeverity.CRITICAL
  val indicatorColor = if (isCritical) MedicalCrimson else MedicalAmber

  Card(
    modifier = modifier
      .fillMaxWidth()
      .testTag("overdue_task_${task.id}"),
    colors = CardDefaults.cardColors(containerColor = MaterialNavyCard),
    shape = RoundedCornerShape(14.dp),
    border = CardDefaults.outlinedCardBorder().copy(
      brush = Brush.horizontalGradient(listOf(indicatorColor, Color.Transparent))
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
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(10.dp)
              .clip(CircleShape)
              .background(indicatorColor)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = if (isCritical) "تأخير حرج (+14 يوماً)" else "تأخير متوسط (+7 أيام)",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = indicatorColor
          )
        }

        Surface(
          color = indicatorColor.copy(alpha = 0.2f),
          shape = RoundedCornerShape(6.dp)
        ) {
          Text(
            text = "متأخرة ${task.daysOverdue} يوماً",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = indicatorColor,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = task.title,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = Color.White
      )
      Text(
        text = "${task.facilityName} • ${task.deviceType}",
        fontSize = 12.sp,
        color = MedicalCyan
      )

      Spacer(modifier = Modifier.height(6.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Text(
          text = "المهندس المكلف: ${task.assignedEngineer}",
          fontSize = 11.sp,
          color = Slate200,
          fontWeight = FontWeight.Medium
        )
        Text(
          text = "الموعد المستحق: ${task.dueDate}",
          fontSize = 11.sp,
          color = Slate400
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Action buttons: Reassign & Mark Completed
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        OutlinedButton(
          onClick = onReassign,
          modifier = Modifier.weight(1f).testTag("reassign_btn_${task.id}"),
          shape = RoundedCornerShape(8.dp),
          contentPadding = PaddingValues(vertical = 4.dp)
        ) {
          Icon(
            imageVector = Icons.Default.SwapHoriz,
            contentDescription = null,
            modifier = Modifier.size(14.dp),
            tint = MedicalCyan
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text("إعادة إسناد", fontSize = 11.sp, color = MedicalCyan)
        }

        Button(
          onClick = onComplete,
          modifier = Modifier.weight(1f).testTag("complete_btn_${task.id}"),
          colors = ButtonDefaults.buttonColors(containerColor = MedicalEmerald),
          shape = RoundedCornerShape(8.dp),
          contentPadding = PaddingValues(vertical = 4.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Check,
            contentDescription = null,
            modifier = Modifier.size(14.dp),
            tint = Color.White
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text("تأكيد الإنجاز", fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}

@Composable
fun EngineerCard(
  engineer: Engineer,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .testTag("engineer_card_${engineer.id}"),
    colors = CardDefaults.cardColors(containerColor = MaterialNavyCard),
    shape = RoundedCornerShape(16.dp),
    border = CardDefaults.outlinedCardBorder().copy(
      brush = Brush.horizontalGradient(listOf(MedicalCyan.copy(alpha = 0.35f), Color.Transparent))
    )
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .size(50.dp)
            .clip(CircleShape)
            .background(MedicalCyan.copy(alpha = 0.2f))
            .border(1.5.dp, MedicalCyan, CircleShape),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = engineer.avatarInitial,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = MedicalCyan
          )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = engineer.name,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
          )
          Text(
            text = engineer.title,
            fontSize = 11.sp,
            color = Slate400
          )
          Text(
            text = "هاتف: ${engineer.phone}",
            fontSize = 11.sp,
            color = MedicalCyan
          )
        }

        Column(horizontalAlignment = Alignment.End) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Star,
              contentDescription = null,
              tint = MedicalAmber,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(2.dp))
            Text(
              text = "${engineer.technicalRating}",
              fontSize = 14.sp,
              fontWeight = FontWeight.Bold,
              color = Color.White
            )
          }
          Text(
            text = "نسبة الإنجاز ${engineer.completionRatePercent}%",
            fontSize = 10.sp,
            color = MedicalEmerald,
            fontWeight = FontWeight.Bold
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      Text(
        text = "النطاق الجغرافي المسند:",
        fontSize = 11.sp,
        color = Slate400
      )
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        engineer.assignedGovernorates.forEach { gov ->
          Surface(
            color = MedicalNavyLight.copy(alpha = 0.5f),
            shape = RoundedCornerShape(6.dp)
          ) {
            Text(
              text = gov,
              fontSize = 10.sp,
              color = Slate200,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Stats row
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        MiniStat(label = "عقود مسندة", value = "${engineer.activeContractsCount}")
        MiniStat(label = "أجهزة ضمان", value = "${engineer.warrantyDevicesCount}")
        MiniStat(label = "تذاكر ومعاينات", value = "${engineer.inspectionTicketsCount}")
      }
    }
  }
}

@Composable
fun MiniStat(label: String, value: String) {
  Surface(
    color = MedicalNavyDark.copy(alpha = 0.4f),
    shape = RoundedCornerShape(8.dp)
  ) {
    Column(
      modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(text = value, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MedicalCyan)
      Text(text = label, fontSize = 10.sp, color = Slate400)
    }
  }
}
