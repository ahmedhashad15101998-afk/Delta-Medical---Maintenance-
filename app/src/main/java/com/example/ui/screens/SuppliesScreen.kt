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
import com.example.data.model.PipelineStage
import com.example.data.model.SupplyItem
import com.example.data.model.SupplyOrder
import com.example.ui.DeltaMedicalViewModel
import com.example.ui.components.SectionHeader
import com.example.ui.components.StatusBadge
import com.example.ui.theme.*

@Composable
fun SuppliesScreen(
  viewModel: DeltaMedicalViewModel,
  modifier: Modifier = Modifier
) {
  var selectedTab by remember { mutableStateOf(0) } // 0: Catalog, 1: Orders & Pipeline

  val supplies by viewModel.supplies.collectAsState()
  val supplyOrders by viewModel.supplyOrders.collectAsState()

  Column(
    modifier = modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp)
  ) {
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
        text = { Text("كتالوج المستلزمات (${supplies.size})", fontWeight = FontWeight.Bold, fontSize = 13.sp) }
      )
      Tab(
        selected = selectedTab == 1,
        onClick = { selectedTab = 1 },
        text = { Text("أوامر التوريد وخط السير (${supplyOrders.size})", fontWeight = FontWeight.Bold, fontSize = 13.sp) }
      )
    }

    if (selectedTab == 0) {
      // Catalog View
      LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
      ) {
        items(supplies) { item ->
          SupplyItemCard(item = item)
        }
      }
    } else {
      // Orders & Pipeline View
      LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
      ) {
        items(supplyOrders) { order ->
          SupplyOrderCard(
            order = order,
            onStageChange = { stage -> viewModel.updatePipelineStage(order.orderId, stage) }
          )
        }
      }
    }
  }
}

@Composable
fun SupplyItemCard(
  item: SupplyItem,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .testTag("supply_item_${item.id}"),
    colors = CardDefaults.cardColors(containerColor = MaterialNavyCard),
    shape = RoundedCornerShape(14.dp),
    border = CardDefaults.outlinedCardBorder().copy(
      brush = Brush.horizontalGradient(
        if (item.isLowStock)
          listOf(MedicalCrimson.copy(alpha = 0.5f), Color.Transparent)
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
            text = item.nameArabic,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
          )
          Text(
            text = "الفئة: ${item.category} • الوحدة: ${item.unit}",
            fontSize = 11.sp,
            color = Slate400
          )
        }

        if (item.isLowStock) {
          StatusBadge(text = "منخفض المخزون!", color = MedicalCrimson)
        } else {
          StatusBadge(text = "متوفر", color = MedicalEmerald)
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Column {
          Text(text = "سعر البيع للعميل:", fontSize = 11.sp, color = Slate400)
          Text(
            text = "${String.format("%,.0f", item.salePrice)} ج.م",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = MedicalCyan
          )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Text(text = "سعر التكلفة:", fontSize = 11.sp, color = Slate400)
          Text(
            text = "${String.format("%,.0f", item.purchasePrice)} ج.م",
            fontSize = 13.sp,
            color = Slate200
          )
        }
        Column(horizontalAlignment = Alignment.End) {
          Text(text = "الرصيد المتاح:", fontSize = 11.sp, color = Slate400)
          Text(
            text = "${item.stockQuantity} ${item.unit}",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = if (item.isLowStock) MedicalCrimson else MedicalEmerald
          )
          Text(
            text = "حد الطلب: ${item.reorderThreshold}",
            fontSize = 9.sp,
            color = Slate400
          )
        }
      }
    }
  }
}

@Composable
fun SupplyOrderCard(
  order: SupplyOrder,
  onStageChange: (PipelineStage) -> Unit,
  modifier: Modifier = Modifier
) {
  val stages = PipelineStage.values()

  Card(
    modifier = modifier
      .fillMaxWidth()
      .testTag("supply_order_${order.orderId}"),
    colors = CardDefaults.cardColors(containerColor = MaterialNavyCard),
    shape = RoundedCornerShape(16.dp),
    border = CardDefaults.outlinedCardBorder().copy(
      brush = Brush.horizontalGradient(listOf(MedicalCyan.copy(alpha = 0.4f), Color.Transparent))
    )
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "أمر توريد رقم: ${order.orderId}",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = MedicalCyan
          )
          Text(
            text = order.clientName,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
          )
        }
        Surface(
          color = MedicalEmerald.copy(alpha = 0.15f),
          shape = RoundedCornerShape(8.dp)
        ) {
          Text(
            text = "الإجمالي: ${String.format("%,.0f", order.finalTotal)} ج.م",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = MedicalEmerald,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      Text(
        text = "المستلزمات المطلوبة:",
        fontSize = 11.sp,
        color = Slate400
      )
      order.items.forEach { line ->
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Text(
            text = "• ${line.item.nameArabic} × ${line.quantity}",
            fontSize = 11.sp,
            color = Slate200
          )
          Text(
            text = "${String.format("%,.0f", line.item.salePrice * line.quantity)} ج.م",
            fontSize = 11.sp,
            color = Slate400
          )
        }
      }

      Spacer(modifier = Modifier.height(6.dp))
      Text(
        text = "شامل ضريبة القيمة المضافة 14% (+${String.format("%,.0f", order.taxAmount)} ج.م)",
        fontSize = 10.sp,
        color = Slate400
      )

      HorizontalDivider(
        modifier = Modifier.padding(vertical = 10.dp),
        color = MedicalNavyLight
      )

      Text(
        text = "خط سير التوريدات (Supply Pipeline Stages):",
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = MedicalCyan
      )

      Spacer(modifier = Modifier.height(8.dp))

      // 6 Stages Horizontal Tracker
      LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        items(stages) { stage ->
          val isCurrent = stage == order.currentStage
          val isPassed = stage.stepNumber <= order.currentStage.stepNumber

          Surface(
            modifier = Modifier.clickable { onStageChange(stage) },
            shape = RoundedCornerShape(8.dp),
            color = when {
              isCurrent -> MedicalCyan
              isPassed -> MedicalEmerald.copy(alpha = 0.2f)
              else -> MedicalNavyLight.copy(alpha = 0.4f)
            },
            border = if (isCurrent) null else androidx.compose.foundation.BorderStroke(
              1.dp,
              if (isPassed) MedicalEmerald.copy(alpha = 0.5f) else MedicalNavyLight
            )
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "${stage.stepNumber}. ${stage.titleArabic}",
                fontSize = 10.sp,
                fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Medium,
                color = when {
                  isCurrent -> MedicalNavyDark
                  isPassed -> MedicalEmerald
                  else -> Slate400
                }
              )
            }
          }
        }
      }
    }
  }
}
