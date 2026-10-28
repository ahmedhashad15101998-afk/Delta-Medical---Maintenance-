package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.DeltaMedicalViewModel
import com.example.ui.MainNavigationTab
import com.example.ui.dialogs.*
import com.example.ui.screens.*
import com.example.ui.theme.*

class MainActivity : ComponentActivity() {

  private val viewModel: DeltaMedicalViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    setContent {
      MyApplicationTheme(darkTheme = true) {
        // Enforce RTL-first layout for Arabic UI
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
          DeltaMedicalApp(viewModel = viewModel)
        }
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeltaMedicalApp(viewModel: DeltaMedicalViewModel) {
  val currentTab by viewModel.currentTab.collectAsState()
  val criticalOverdueCount by viewModel.criticalOverdueCount.collectAsState()

  val selectedContractForRenewal by viewModel.selectedContractForRenewal.collectAsState()
  val selectedTicketForJobOrder by viewModel.selectedTicketForJobOrder.collectAsState()
  val selectedTicketForDiagnostic by viewModel.selectedTicketForDiagnostic.collectAsState()
  val selectedTaskForReassign by viewModel.selectedTaskForReassign.collectAsState()
  val showPmChecklist by viewModel.showPmChecklist.collectAsState()
  val showAlertsDialog by viewModel.showAlertsDialog.collectAsState()
  val showNewTicketDialog by viewModel.showNewTicketDialog.collectAsState()
  val pmChecklistItems by viewModel.pmChecklistItems.collectAsState()

  // Handle back button to return to Dashboard if in another tab
  BackHandler(enabled = currentTab != MainNavigationTab.DASHBOARD) {
    viewModel.setTab(MainNavigationTab.DASHBOARD)
  }

  Scaffold(
    modifier = Modifier.fillMaxSize(),
    containerColor = MedicalNavyDark,
    contentWindowInsets = WindowInsets.safeDrawing,
    topBar = {
      TopAppBar(
        title = {
          Row(
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(8.dp))
                .border(1.dp, MedicalCyan.copy(alpha = 0.5f), RoundedCornerShape(8.dp)),
              contentAlignment = Alignment.Center
            ) {
              Image(
                painter = painterResource(id = R.drawable.delta_medical_logo),
                contentDescription = "Delta Medical Icon",
                modifier = Modifier.size(32.dp).clip(RoundedCornerShape(8.dp))
              )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = "دلتا الطبية - Delta Medical",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
              )
              Text(
                text = "Medical CMMS & ERP • Neuro-Diagnostic",
                fontSize = 10.sp,
                color = MedicalCyan
              )
            }
          }
        },
        actions = {
          // Alert Bell Icon with Critical Badge
          IconButton(
            onClick = { viewModel.setShowAlertsDialog(true) },
            modifier = Modifier.testTag("top_bar_alerts_btn")
          ) {
            BadgedBox(
              badge = {
                if (criticalOverdueCount > 0) {
                  Badge(containerColor = MedicalCrimson) {
                    Text(text = "$criticalOverdueCount", color = Color.White, fontSize = 10.sp)
                  }
                }
              }
            ) {
              Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "التنبيهات",
                tint = if (criticalOverdueCount > 0) MedicalAmber else Slate200
              )
            }
          }

          // PM Checklist Icon
          IconButton(
            onClick = { viewModel.setShowPmChecklist(true) },
            modifier = Modifier.testTag("top_bar_pm_btn")
          ) {
            Icon(
              imageVector = Icons.Default.Checklist,
              contentDescription = "قائمة التدقيق الوقائية",
              tint = MedicalCyan
            )
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = MedicalNavyDark
        )
      )
    },
    bottomBar = {
      // Horizontal Scrollable Navigation Bar covering all 8 modules cleanly
      Surface(
        color = MedicalNavy,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        shadowElevation = 8.dp,
        modifier = Modifier.fillMaxWidth().navigationBarsPadding()
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 8.dp, vertical = 6.dp),
          horizontalArrangement = Arrangement.spacedBy(4.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          MainNavigationTab.values().forEach { tab ->
            val isSelected = currentTab == tab
            Surface(
              modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .clickable { viewModel.setTab(tab) }
                .testTag("nav_tab_${tab.name}"),
              color = if (isSelected) MedicalCyan.copy(alpha = 0.2f) else Color.Transparent,
              border = if (isSelected) androidx.compose.foundation.BorderStroke(1.dp, MedicalCyan.copy(alpha = 0.6f)) else null
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Icon(
                  imageVector = when (tab) {
                    MainNavigationTab.DASHBOARD -> Icons.Default.Dashboard
                    MainNavigationTab.CONTRACTS -> Icons.Default.Assignment
                    MainNavigationTab.ESTIMATES -> Icons.Default.RequestQuote
                    MainNavigationTab.TICKETS -> Icons.Default.BuildCircle
                    MainNavigationTab.ENGINEERS -> Icons.Default.Engineering
                    MainNavigationTab.SUPPLIES -> Icons.Default.Inventory2
                    MainNavigationTab.INVOICES -> Icons.Default.ReceiptLong
                    MainNavigationTab.EXPORT -> Icons.Default.CloudDownload
                  },
                  contentDescription = null,
                  tint = if (isSelected) MedicalCyan else Slate400,
                  modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = tab.titleArabic,
                  fontSize = 12.sp,
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                  color = if (isSelected) MedicalCyan else Slate400
                )
              }
            }
          }
        }
      }
    }
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
    ) {
      when (currentTab) {
        MainNavigationTab.DASHBOARD -> DashboardScreen(viewModel = viewModel)
        MainNavigationTab.CONTRACTS -> ContractsScreen(viewModel = viewModel)
        MainNavigationTab.ESTIMATES -> EstimatesScreen(viewModel = viewModel)
        MainNavigationTab.TICKETS -> TicketsScreen(viewModel = viewModel)
        MainNavigationTab.ENGINEERS -> EngineersScreen(viewModel = viewModel)
        MainNavigationTab.SUPPLIES -> SuppliesScreen(viewModel = viewModel)
        MainNavigationTab.INVOICES -> InvoicesScreen(viewModel = viewModel)
        MainNavigationTab.EXPORT -> ExportCenterScreen(viewModel = viewModel)
      }
    }
  }

  // Dialogs
  selectedContractForRenewal?.let { contract ->
    ContractRenewalDialog(
      contract = contract,
      onDismiss = { viewModel.closeContractRenewal() },
      onConfirm = { newDate -> viewModel.confirmContractRenewal(contract.id, newDate) }
    )
  }

  selectedTicketForJobOrder?.let { ticket ->
    JobOrderPrintDialog(
      ticket = ticket,
      onDismiss = { viewModel.closeJobOrder() }
    )
  }

  selectedTicketForDiagnostic?.let { ticket ->
    DiagnosticReportDialog(
      ticket = ticket,
      onDismiss = { viewModel.closeDiagnosticReport() },
      onSave = { report -> viewModel.saveDiagnosticReport(ticket.id, report) }
    )
  }

  selectedTaskForReassign?.let { task ->
    ReassignTaskDialog(
      task = task,
      onDismiss = { viewModel.closeReassignTask() },
      onReassign = { newEng -> viewModel.reassignTask(task.id, newEng) }
    )
  }

  if (showPmChecklist) {
    PMChecklistDialog(
      checklistItems = pmChecklistItems,
      onToggle = { idx -> viewModel.togglePmCheckItem(idx) },
      onDismiss = { viewModel.setShowPmChecklist(false) }
    )
  }

  if (showAlertsDialog) {
    SmartAlertsDialog(
      viewModel = viewModel,
      onDismiss = { viewModel.setShowAlertsDialog(false) }
    )
  }

  if (showNewTicketDialog) {
    NewTicketDialog(
      onDismiss = { viewModel.setShowNewTicketDialog(false) },
      onCreate = { name, model, type, eng, fee, phone, loc ->
        viewModel.createTicket(name, model, type, eng, fee, phone, loc)
      }
    )
  }
}
