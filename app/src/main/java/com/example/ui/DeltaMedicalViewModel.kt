package com.example.ui

import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.*
import com.example.data.repository.DeltaMedicalRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class MainNavigationTab(val titleArabic: String) {
  DASHBOARD("الرئيسية"),
  CONTRACTS("العقود والضمان"),
  ESTIMATES("المقايسات"),
  TICKETS("تذاكر الخدمة"),
  ENGINEERS("محفظة المهندسين"),
  SUPPLIES("المستلزمات"),
  INVOICES("الإيرادات"),
  EXPORT("مركز التصدير")
}

class DeltaMedicalViewModel(application: Application) : AndroidViewModel(application) {

  private val repository = DeltaMedicalRepository.getInstance()

  val contracts: StateFlow<List<Contract>> = repository.contracts
  val warrantyClients: StateFlow<List<WarrantyClient>> = repository.warrantyClients
  val repairEstimates: StateFlow<List<RepairEstimate>> = repository.repairEstimates
  val engineers: StateFlow<List<Engineer>> = repository.engineers
  val overdueTasks: StateFlow<List<OverdueTask>> = repository.overdueTasks
  val tickets: StateFlow<List<FieldServiceTicket>> = repository.tickets
  val supplies: StateFlow<List<SupplyItem>> = repository.supplies
  val supplyOrders: StateFlow<List<SupplyOrder>> = repository.supplyOrders
  val invoices: StateFlow<List<Invoice>> = repository.invoices
  val installedDevices: StateFlow<List<InstalledDevice>> = repository.installedDevices

  // UI States
  private val _currentTab = MutableStateFlow(MainNavigationTab.DASHBOARD)
  val currentTab: StateFlow<MainNavigationTab> = _currentTab.asStateFlow()

  private val _searchQuery = MutableStateFlow("")
  val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

  private val _selectedContractForRenewal = MutableStateFlow<Contract?>(null)
  val selectedContractForRenewal: StateFlow<Contract?> = _selectedContractForRenewal.asStateFlow()

  private val _selectedTicketForJobOrder = MutableStateFlow<FieldServiceTicket?>(null)
  val selectedTicketForJobOrder: StateFlow<FieldServiceTicket?> = _selectedTicketForJobOrder.asStateFlow()

  private val _selectedTicketForDiagnostic = MutableStateFlow<FieldServiceTicket?>(null)
  val selectedTicketForDiagnostic: StateFlow<FieldServiceTicket?> = _selectedTicketForDiagnostic.asStateFlow()

  private val _selectedTaskForReassign = MutableStateFlow<OverdueTask?>(null)
  val selectedTaskForReassign: StateFlow<OverdueTask?> = _selectedTaskForReassign.asStateFlow()

  private val _showPmChecklist = MutableStateFlow(false)
  val showPmChecklist: StateFlow<Boolean> = _showPmChecklist.asStateFlow()

  private val _showAlertsDialog = MutableStateFlow(false)
  val showAlertsDialog: StateFlow<Boolean> = _showAlertsDialog.asStateFlow()

  private val _showNewTicketDialog = MutableStateFlow(false)
  val showNewTicketDialog: StateFlow<Boolean> = _showNewTicketDialog.asStateFlow()

  // PM Checklist items
  private val _pmChecklistItems = MutableStateFlow<List<PMCheckItem>>(
    listOf(
      PMCheckItem("فحص كابلات تحفيز المريض والمجسات وتأكيد سلامة العزل", "الكابلات والإشارات"),
      PMCheckItem("تنظيف واستبدال فلاتر التبريد لمكبرات الإشارة (Amplifiers)", "الفلاتر والتبريد"),
      PMCheckItem("اختبار جهد التأريض ومقاومة التسريب للأمان الكهربائي (< 0.1 Ohm)", "الأمان والتأريض"),
      PMCheckItem("معايرة حساسية القنوات وتأكيد خط الأساس (Baseline Noise < 5uV)", "المعايرة وضبط الإشارة"),
      PMCheckItem("فحص ومعايرة قياس المعاوقة (Impedance Check < 5 kOhm)", "المعاوقة والمعايير"),
      PMCheckItem("اختبار محفز الضوء الفلاشي لـ EEG ومحفز الصدمة لـ EMG", "وحدات التحفيز")
    )
  )
  val pmChecklistItems: StateFlow<List<PMCheckItem>> = _pmChecklistItems.asStateFlow()

  // Computed summary stats
  val totalContractsValue: StateFlow<Long> = contracts.map { list ->
    list.sumOf { it.annualValue }
  }.stateIn(viewModelScope, SharingStarted.Eagerly, 638000L)

  val totalEstimatesValue: StateFlow<Long> = repairEstimates.map { list ->
    list.sumOf { it.totalAmount }
  }.stateIn(viewModelScope, SharingStarted.Eagerly, 234000L)

  val totalInstalledDevicesCount: StateFlow<Int> = installedDevices.map { it.size }
    .stateIn(viewModelScope, SharingStarted.Eagerly, 17)

  val activeOverdueTasksCount: StateFlow<Int> = overdueTasks.map { list ->
    list.count { !it.isCompleted }
  }.stateIn(viewModelScope, SharingStarted.Eagerly, 4)

  val criticalOverdueCount: StateFlow<Int> = overdueTasks.map { list ->
    list.count { !it.isCompleted && it.severity == OverdueSeverity.CRITICAL }
  }.stateIn(viewModelScope, SharingStarted.Eagerly, 2)

  // Navigation & Actions
  fun setTab(tab: MainNavigationTab) {
    _currentTab.value = tab
  }

  fun setSearchQuery(query: String) {
    _searchQuery.value = query
  }

  fun togglePmCheckItem(index: Int) {
    _pmChecklistItems.update { current ->
      current.mapIndexed { idx, item ->
        if (idx == index) item.copy(isChecked = !item.isChecked) else item
      }
    }
  }

  fun openContractRenewal(contract: Contract) {
    _selectedContractForRenewal.value = contract
  }

  fun closeContractRenewal() {
    _selectedContractForRenewal.value = null
  }

  fun confirmContractRenewal(contractId: String, newEndDate: String) {
    repository.renewContract(contractId, newEndDate)
    _selectedContractForRenewal.value = null
    showToast("تم تجديد العقد بنجاح وتحديث تاريخ الانتهاء")
  }

  fun openJobOrder(ticket: FieldServiceTicket) {
    _selectedTicketForJobOrder.value = ticket
  }

  fun closeJobOrder() {
    _selectedTicketForJobOrder.value = null
  }

  fun openDiagnosticReport(ticket: FieldServiceTicket) {
    _selectedTicketForDiagnostic.value = ticket
  }

  fun closeDiagnosticReport() {
    _selectedTicketForDiagnostic.value = null
  }

  fun saveDiagnosticReport(ticketId: String, report: DiagnosticReport) {
    repository.addDiagnosticReport(ticketId, report)
    _selectedTicketForDiagnostic.value = null
    showToast("تم حفظ تقرير المعاينة والتشخيص الفني بنجاح")
  }

  fun convertTicketToEstimate(ticketId: String) {
    repository.convertTicketToEstimate(ticketId)
    showToast("تم تحويل التذكرة مباشرة إلى مقايسة إصلاح معتمدة!")
  }

  fun openReassignTask(task: OverdueTask) {
    _selectedTaskForReassign.value = task
  }

  fun closeReassignTask() {
    _selectedTaskForReassign.value = null
  }

  fun reassignTask(taskId: String, newEngineer: String) {
    repository.reassignOverdueTask(taskId, newEngineer)
    _selectedTaskForReassign.value = null
    showToast("تم إعادة إسناد المهمة إلى $newEngineer")
  }

  fun completeTask(taskId: String) {
    repository.completeOverdueTask(taskId)
    showToast("تم تأكيد إنجاز المهمة بنجاح")
  }

  fun setShowPmChecklist(show: Boolean) {
    _showPmChecklist.value = show
  }

  fun setShowAlertsDialog(show: Boolean) {
    _showAlertsDialog.value = show
  }

  fun setShowNewTicketDialog(show: Boolean) {
    _showNewTicketDialog.value = show
  }

  fun createTicket(
    clientName: String,
    deviceModel: String,
    breakdownType: BreakdownType,
    assignedEngineer: String,
    inspectionFee: Long,
    phone: String,
    location: String
  ) {
    val newId = "TKT-2026-${(100..999).random()}"
    val ticket = FieldServiceTicket(
      id = newId,
      clientName = clientName,
      deviceModel = deviceModel,
      breakdownType = breakdownType,
      reportDate = "2026-10-28",
      assignedEngineer = assignedEngineer,
      inspectionFee = inspectionFee,
      isFeeCollected = true,
      status = TicketStatus.OPEN,
      contactPhone = phone,
      location = location
    )
    repository.addTicket(ticket)
    _showNewTicketDialog.value = false
    showToast("تم تسجيل تذكرة الخدمة رقم $newId بنجاح")
  }

  fun updatePipelineStage(orderId: String, newStage: PipelineStage) {
    repository.updatePipelineStage(orderId, newStage)
    showToast("تم تحديث مرحلة التوريد إلى: ${newStage.titleArabic}")
  }

  // Export methods
  fun copyTsvToClipboard(type: String) {
    val tsvData = when (type) {
      "contracts" -> repository.generateContractsTsv()
      "warranty" -> repository.generateWarrantyTsv()
      else -> repository.generateCombinedTsv()
    }
    val clipboard = getApplication<Application>().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("Delta Medical Data", tsvData)
    clipboard.setPrimaryClip(clip)
    showToast("تم نسخ البيانات كـ TSV للحافظة! يمكنك لصقها الآن في Excel أو Google Sheets")
  }

  fun downloadCsvUtf8Bom(type: String): String {
    val csv = if (type == "contracts") repository.generateContractsCsv() else repository.generateWarrantyCsv()
    showToast("تم تجهيز ملف CSV بترميز UTF-8 مع BOM لدعم العربية في Excel")
    return csv
  }

  fun exportIcsCalendar(): String {
    val ics = repository.generateIcsCalendar()
    val clipboard = getApplication<Application>().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("Delta Medical Calendar", ics)
    clipboard.setPrimaryClip(clip)
    showToast("تم نسخ جدول الزيارات بصيغة .ics القياسية لتقويم Google / Outlook")
    return ics
  }

  private fun showToast(message: String) {
    Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show()
  }
}
