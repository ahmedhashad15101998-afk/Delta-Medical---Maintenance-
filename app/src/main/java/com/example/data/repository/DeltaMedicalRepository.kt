package com.example.data.repository

import com.example.data.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DeltaMedicalRepository private constructor() {

  // 1. Active Maintenance Contracts (9 contracts, 11 devices, 638,000 EGP)
  private val _contracts = MutableStateFlow<List<Contract>>(
    listOf(
      Contract(
        id = "CNT-2026-001",
        facilityName = "مستشفى بنها الجامعي",
        annualValue = 45000,
        deviceType = "EMG 4ch (رسم عضلات 4 قنوات)",
        deviceCount = 1,
        visitsPerYear = 4,
        visitsCompleted = 2,
        contactPerson = "م. نجوى",
        phone = "01060368000",
        governorate = "القليوبية",
        city = "بنها",
        address = "شارع فريد ندا، مجمع الكليات الطبية",
        startDate = "2026-01-01",
        endDate = "2026-12-31",
        serialNumbers = listOf("BNH-EMG4-8921"),
        assignedEngineer = "م. خالد يوسف",
        status = ContractStatus.ACTIVE
      ),
      Contract(
        id = "CNT-2026-002",
        facilityName = "مستشفى مصر للطيران ألماظة",
        annualValue = 8000,
        deviceType = "EMG 4ch (رسم عضلات 4 قنوات)",
        deviceCount = 1,
        visitsPerYear = 4,
        visitsCompleted = 3,
        contactPerson = "م. أحمد عبدالرحمن",
        phone = "01010800070",
        governorate = "القاهرة",
        city = "ألماظة",
        address = "طريق المطار، بجوار مستشفى الجلاء",
        startDate = "2026-01-15",
        endDate = "2026-11-20", // Expiring soon (<60 days alert)
        serialNumbers = listOf("MS-EMG4-5541"),
        assignedEngineer = "م. أحمد حشاد",
        status = ContractStatus.EXPIRING_SOON
      ),
      Contract(
        id = "CNT-2026-003",
        facilityName = "مستشفى عين شمس التخصصي بالعبور",
        annualValue = 30000,
        deviceType = "EMG 2ch (رسم عضلات قناتين)",
        deviceCount = 1,
        visitsPerYear = 2,
        visitsCompleted = 1,
        contactPerson = "م. شيماء",
        phone = "01141968967",
        governorate = "القليوبية",
        city = "العبور",
        address = "الحي السابع، مدينة العبور",
        startDate = "2026-02-01",
        endDate = "2027-01-31",
        serialNumbers = listOf("ASU-OBR-2210"),
        assignedEngineer = "م. خالد يوسف",
        status = ContractStatus.ACTIVE
      ),
      Contract(
        id = "CNT-2026-004",
        facilityName = "مركز أورام الفيوم",
        annualValue = 90000,
        deviceType = "جهازان (EMG 2ch + EEG 34ch)",
        deviceCount = 2,
        visitsPerYear = 4,
        visitsCompleted = 2,
        contactPerson = "م. محمد حمدي",
        phone = "01153861300",
        governorate = "الفيوم",
        city = "الفيوم",
        address = "ميدان التدريب، مدينة الفيوم",
        startDate = "2026-01-01",
        endDate = "2026-12-31",
        serialNumbers = listOf("FYM-EMG2-7721", "FYM-EEG34-9904"),
        assignedEngineer = "م. خالد يوسف",
        status = ContractStatus.ACTIVE
      ),
      Contract(
        id = "CNT-2026-005",
        facilityName = "مستشفى أبو الريش الياباني للأطفال (العيادات الخارجية)",
        annualValue = 150000,
        deviceType = "جهازان (EMG Matrix 1005 + EEG 34ch)",
        deviceCount = 2,
        visitsPerYear = 4,
        visitsCompleted = 3,
        contactPerson = "م. شاهيناز",
        phone = "01026324069",
        governorate = "القاهرة",
        city = "السيدة زينب",
        address = "شارع علي إبراهيم، المنيرة",
        startDate = "2026-01-01",
        endDate = "2026-12-31",
        serialNumbers = listOf("ABR-MTX-1005", "ABR-EEG34-3112"),
        assignedEngineer = "م. أحمد حشاد",
        status = ContractStatus.ACTIVE
      ),
      Contract(
        id = "CNT-2026-006",
        facilityName = "مستشفى جامعة القاهرة التخصصي للأطفال (أبو الريش الياباني)",
        annualValue = 75000,
        deviceType = "EMG 4ch (رسم عضلات 4 قنوات)",
        deviceCount = 1,
        visitsPerYear = 4,
        visitsCompleted = 2,
        contactPerson = "م. نورا",
        phone = "01004549273",
        governorate = "القاهرة",
        city = "المنيرة",
        address = "مجمع مستشفيات قصر العيني، القاهرة",
        startDate = "2026-01-01",
        endDate = "2026-12-31",
        serialNumbers = listOf("CUH-EMG4-1029"),
        assignedEngineer = "م. أحمد حشاد",
        status = ContractStatus.ACTIVE
      ),
      Contract(
        id = "CNT-2026-007",
        facilityName = "مركز الزهور د/ محمد عمارة",
        annualValue = 25000,
        deviceType = "EEG 34ch (رسم مخ 34 قناة)",
        deviceCount = 1,
        visitsPerYear = 4,
        visitsCompleted = 1,
        contactPerson = "أ. عبدالله",
        phone = "01013001140",
        governorate = "الشرقية",
        city = "الزقازيق",
        address = "شارع المحافظة، برج الزهور الطبي",
        startDate = "2026-03-01",
        endDate = "2027-02-28",
        serialNumbers = listOf("ZHR-EEG34-6014"),
        assignedEngineer = "م. خالد يوسف",
        status = ContractStatus.ACTIVE
      ),
      Contract(
        id = "CNT-2026-008",
        facilityName = "مستشفى أطفال الرمل بالإسكندرية",
        annualValue = 200000,
        deviceType = "EMG 2ch (شامل قطع الغيار)",
        deviceCount = 1,
        visitsPerYear = 4,
        visitsCompleted = 3,
        contactPerson = "م. إيمان",
        phone = "01275714887",
        governorate = "الإسكندرية",
        city = "الرمل",
        address = "محطة الرمل، ميدان سعد زغلول",
        startDate = "2026-01-01",
        endDate = "2026-12-31",
        serialNumbers = listOf("RAM-EMG2-4419"),
        includesSpareParts = true,
        assignedEngineer = "م. محمد زيدان",
        status = ContractStatus.ACTIVE
      ),
      Contract(
        id = "CNT-2026-009",
        facilityName = "مركز الفتح للأشعة بالمعادي",
        annualValue = 15000,
        deviceType = "EMG 2ch (رسم عضلات قناتين)",
        deviceCount = 1,
        visitsPerYear = 4,
        visitsCompleted = 1,
        contactPerson = "أ. أسامة",
        phone = "01001792923",
        governorate = "القاهرة",
        city = "المعادي",
        address = "شارع النصر، المعادي الجديدة",
        startDate = "2026-01-01",
        endDate = "2026-12-31",
        serialNumbers = listOf("FAT-EMG2-3301"),
        assignedEngineer = "م. أحمد حشاد",
        status = ContractStatus.ACTIVE
      )
    )
  )
  val contracts: StateFlow<List<Contract>> = _contracts.asStateFlow()

  // 2. Warranty Base (3 clients, 6 medical devices)
  private val _warrantyClients = MutableStateFlow<List<WarrantyClient>>(
    listOf(
      WarrantyClient(
        id = "WAR-2026-01",
        clientName = "معمل الدكتور أحمد عبدالله (Rolex LAB)",
        facilityType = "معمل تحاليل وفحوصات عصبية خاصة",
        deviceCount = 2,
        deviceTypes = "جهازان: EMG 4ch + EEG 34ch",
        warrantyExpiryDate = "2027-04-20",
        location = "الدقي، شارع مصدق",
        city = "الجيزة",
        contactPerson = "د. أحمد عبدالله",
        phone = "01001234567",
        serialNumbers = listOf("RLX-EMG4-2024", "RLX-EEG34-2024"),
        visitsPerYear = 4,
        visitsCompleted = 2,
        assignedEngineer = "م. أحمد حشاد"
      ),
      WarrantyClient(
        id = "WAR-2026-02",
        clientName = "مستشفى أبو قير العام",
        facilityType = "مستشفى عام حكومي",
        deviceCount = 2,
        deviceTypes = "جهازان: EMG 2ch + EEG 34ch",
        warrantyExpiryDate = "2027-04-20",
        location = "طوسون، أبو قير",
        city = "الإسكندرية",
        contactPerson = "م. سامح فؤاد",
        phone = "01223456789",
        serialNumbers = listOf("ABQ-EMG2-8812", "ABQ-EEG34-9934"),
        visitsPerYear = 4,
        visitsCompleted = 2,
        assignedEngineer = "م. محمد زيدان"
      ),
      WarrantyClient(
        id = "WAR-2026-03",
        clientName = "مركز نيورو أدفنس Neuro Advance (د/ صلاح ود/ أنس السيسي)",
        facilityType = "مركز تشخيصي مخ وأعصاب متقدم",
        deviceCount = 2,
        deviceTypes = "جهازان: EMG Matrix + EEG High-Density",
        warrantyExpiryDate = "2026-12-01",
        location = "بفرلي هيلز، الشيخ زايد",
        city = "الجيزة",
        contactPerson = "د. أنس السيسي",
        phone = "01112233445",
        serialNumbers = listOf("NAD-EMG-5001", "NAD-EEG-5002"),
        visitsPerYear = 4,
        visitsCompleted = 3,
        assignedEngineer = "م. أحمد حشاد"
      )
    )
  )
  val warrantyClients: StateFlow<List<WarrantyClient>> = _warrantyClients.asStateFlow()

  // 3. Approved Repair Estimates (2 estimates totaling 234,000 EGP)
  private val _repairEstimates = MutableStateFlow<List<RepairEstimate>>(
    listOf(
      RepairEstimate(
        id = "EST-2026-01",
        facilityName = "معهد ناصر للبحوث والعلاج",
        deviceModel = "جهاز رسم عضلات (Micromed - Myolight)",
        scopeOfWork = "إصلاح كروت المعالجة الرقمية DSP وتنزيل أحدث إصدار سوفت وير تشغيلي ومعايرة القنوات",
        totalAmount = 200000,
        approvalDate = "2026-10-15",
        targetCompletionDate = "2026-11-10",
        status = EstimateStatus.APPROVED,
        assignedEngineer = "م. أحمد حشاد",
        partsRequired = listOf("كارت DSP رئيسي", "حزمة السوفت وير الطبية v5.2", "كابلات إشارة معزولة")
      ),
      RepairEstimate(
        id = "EST-2026-02",
        facilityName = "مستشفى أبو الريش الياباني للعيادات الخارجية",
        deviceModel = "جهاز رسم وتخطيط المخ (EEG 34ch)",
        scopeOfWork = "إصلاح وحدة المنبه الضوئي (Photic Stimulation Unit) واستبدال لمبة الفلاش زينون والتحكم الرقمي",
        totalAmount = 34000,
        approvalDate = "2026-10-22",
        targetCompletionDate = "2026-11-05",
        status = EstimateStatus.APPROVED,
        assignedEngineer = "م. أحمد حشاد",
        partsRequired = listOf("وحدة الفلاش الضوئي Photic Stimulator", "كابل الربط المتزامن Trigger Cable")
      )
    )
  )
  val repairEstimates: StateFlow<List<RepairEstimate>> = _repairEstimates.asStateFlow()

  // 4. Engineers Portfolio
  private val _engineers = MutableStateFlow<List<Engineer>>(
    listOf(
      Engineer(
        id = "ENG-01",
        name = "م. أحمد حشاد",
        title = "كبير مهندسي الأجهزة الطبية الحيوية",
        phone = "01015101998",
        assignedGovernorates = listOf("القاهرة", "الجيزة"),
        activeContractsCount = 4,
        warrantyDevicesCount = 4,
        inspectionTicketsCount = 3,
        completionRatePercent = 98,
        technicalRating = 4.9f,
        avatarInitial = "أ.ح"
      ),
      Engineer(
        id = "ENG-02",
        name = "م. محمد زيدان",
        title = "أخصائي صيانة الأجهزة العصبية",
        phone = "01123456780",
        assignedGovernorates = listOf("الإسكندرية", "البحيرة", "الغربية"),
        activeContractsCount = 1,
        warrantyDevicesCount = 2,
        inspectionTicketsCount = 2,
        completionRatePercent = 95,
        technicalRating = 4.8f,
        avatarInitial = "م.ز"
      ),
      Engineer(
        id = "ENG-03",
        name = "م. خالد يوسف",
        title = "مهندس صيانة ومعايرة إكلينيكية",
        phone = "01234567891",
        assignedGovernorates = listOf("القليوبية", "الشرقية", "الفيوم"),
        activeContractsCount = 4,
        warrantyDevicesCount = 0,
        inspectionTicketsCount = 2,
        completionRatePercent = 92,
        technicalRating = 4.7f,
        avatarInitial = "خ.ي"
      )
    )
  )
  val engineers: StateFlow<List<Engineer>> = _engineers.asStateFlow()

  // 5. Overdue Tasks Engine (+14 days Critical red, +7 days Medium amber)
  private val _overdueTasks = MutableStateFlow<List<OverdueTask>>(
    listOf(
      OverdueTask(
        id = "OVD-001",
        title = "زيارة الصيانة الوقائية ومعايرة إشارة EMG",
        facilityName = "مستشفى بنها الجامعي",
        deviceType = "EMG 4ch",
        taskType = "زيارة دورية وقائية",
        dueDate = "2026-10-12",
        daysOverdue = 16,
        severity = OverdueSeverity.CRITICAL,
        assignedEngineer = "م. خالد يوسف",
        isCompleted = false
      ),
      OverdueTask(
        id = "OVD-002",
        title = "فحص واختبار معاوقة الحزام الأرضي والتأريض الكهربائي",
        facilityName = "مركز الفتح للأشعة بالمعادي",
        deviceType = "EMG 2ch",
        taskType = "فحص ومعايرة التأريض",
        dueDate = "2026-10-13",
        daysOverdue = 15,
        severity = OverdueSeverity.CRITICAL,
        assignedEngineer = "م. أحمد حشاد",
        isCompleted = false
      ),
      OverdueTask(
        id = "OVD-003",
        title = "فحص واستبدال كابلات المرضى والمجسات السطحية",
        facilityName = "مركز الزهور د/ محمد عمارة",
        deviceType = "EEG 34ch",
        taskType = "فحص كابلات المرضى",
        dueDate = "2026-10-19",
        daysOverdue = 9,
        severity = OverdueSeverity.MEDIUM,
        assignedEngineer = "م. خالد يوسف",
        isCompleted = false
      ),
      OverdueTask(
        id = "OVD-004",
        title = "تسليم تقرير ومقايسة كارت تحفيز العصب الحسي",
        facilityName = "مستشفى مصر للطيران ألماظة",
        deviceType = "EMG 4ch",
        taskType = "تسليم مقايسة إصلاح",
        dueDate = "2026-10-20",
        daysOverdue = 8,
        severity = OverdueSeverity.MEDIUM,
        assignedEngineer = "م. أحمد حشاد",
        isCompleted = false
      )
    )
  )
  val overdueTasks: StateFlow<List<OverdueTask>> = _overdueTasks.asStateFlow()

  // 6. Field Service Tickets
  private val _tickets = MutableStateFlow<List<FieldServiceTicket>>(
    listOf(
      FieldServiceTicket(
        id = "TKT-2026-081",
        clientName = "مستشفى الساحل التعليمي",
        deviceModel = "جهاز رسم أعصاب وعضلات EMG 4ch",
        breakdownType = BreakdownType.COMPLETE,
        reportDate = "2026-10-26",
        assignedEngineer = "م. أحمد حشاد",
        inspectionFee = 2500,
        isFeeCollected = true,
        status = TicketStatus.DIAGNOSED,
        contactPhone = "01009988776",
        location = "شبرا، القاهرة",
        diagnosticReport = DiagnosticReport(
          reportDate = "2026-10-27",
          findings = "عطل كامل في دائرة التغذية الكهربائية Power Supply وتشويش عالي (50Hz Artifact) على إشارة العصب الحسي",
          damagedParts = listOf("وحدة Power Supply 24V", "كابل التأريض المرجعي Reference Lead"),
          pcbBoardSerialNumbers = listOf("PCB-PSU-9921", "AMP-CH14-041"),
          technicalRecommendations = "تغيير وحدة التغذية وفحص عزل المحول الطبي وإجراء معايرة المعاوقة الشاملة",
          estimatedRepairCost = 48000
        )
      ),
      FieldServiceTicket(
        id = "TKT-2026-082",
        clientName = "عيادة الأستاذ الدكتور طارق البسيوني",
        deviceModel = "جهاز رسم وتخطيط المخ EEG 34ch",
        breakdownType = BreakdownType.PARTIAL,
        reportDate = "2026-10-27",
        assignedEngineer = "م. خالد يوسف",
        inspectionFee = 2000,
        isFeeCollected = true,
        status = TicketStatus.OPEN,
        contactPhone = "01122334499",
        location = "طنطا، الغربية",
        diagnosticReport = null
      )
    )
  )
  val tickets: StateFlow<List<FieldServiceTicket>> = _tickets.asStateFlow()

  // 7. Supplies Catalog
  private val _supplies = MutableStateFlow<List<SupplyItem>>(
    listOf(
      SupplyItem("SUP-01", "إبر رسم عضلات متحدة المركز (Concentric EMG Needles 30mm)", "إبر فحص العضلات", "علبة (25 إبرة)", 1850.0, 2400.0, 42, 15),
      SupplyItem("SUP-02", "إبر رسم عضلات متحدة المركز (Concentric EMG Needles 50mm)", "إبر فحص العضلات", "علبة (25 إبرة)", 2100.0, 2750.0, 28, 10),
      SupplyItem("SUP-03", "إلكترودات سطحية فضة/كلوريد الفضة (Ag/AgCl Disposable)", "إلكترودات ومجسات", "باكيت (100 قطعة)", 620.0, 850.0, 85, 20),
      SupplyItem("SUP-04", "معجون تثبيت إلكترودات رسم المخ (Ten20 EEG Paste 8oz)", "جل ومعجون توصيل", "عبوة", 480.0, 680.0, 64, 15),
      SupplyItem("SUP-05", "جل توصيل فائق النقاء للإشارة العصبية (Signa Gel 250g)", "جل ومعجون توصيل", "زجاجة", 180.0, 270.0, 110, 25),
      SupplyItem("SUP-06", "ورق تسجيل حراري طبي عالي الدقة (Thermal Paper 110mm)", "ورق تسجيل", "رول", 95.0, 145.0, 9, 15), // Low stock
      SupplyItem("SUP-07", "حزام أرضي سطحي للمريض مضاد للتشويش (Ground Band)", "ملحقات حماية", "قطعة", 350.0, 520.0, 18, 5)
    )
  )
  val supplies: StateFlow<List<SupplyItem>> = _supplies.asStateFlow()

  // 8. Supply Orders & Pipeline
  private val _supplyOrders = MutableStateFlow<List<SupplyOrder>>(
    listOf(
      SupplyOrder(
        orderId = "ORD-2026-112",
        clientName = "مركز أورام الفيوم",
        orderDate = "2026-10-24",
        items = listOf(
          SupplyOrderItem(_supplies.value[0], 2), // Concentric needles
          SupplyOrderItem(_supplies.value[3], 3), // Ten20 Paste
          SupplyOrderItem(_supplies.value[4], 5)  // Signa gel
        ),
        discountPercent = 5.0,
        taxPercent = 14.0,
        currentStage = PipelineStage.INSTALLATION_TESTING
      ),
      SupplyOrder(
        orderId = "ORD-2026-113",
        clientName = "معمل الدكتور أحمد عبدالله (Rolex LAB)",
        orderDate = "2026-10-26",
        items = listOf(
          SupplyOrderItem(_supplies.value[1], 1),
          SupplyOrderItem(_supplies.value[2], 2)
        ),
        discountPercent = 0.0,
        taxPercent = 14.0,
        currentStage = PipelineStage.INITIAL_INSPECTION
      )
    )
  )
  val supplyOrders: StateFlow<List<SupplyOrder>> = _supplyOrders.asStateFlow()

  // 9. Invoices & Revenues
  private val _invoices = MutableStateFlow<List<Invoice>>(
    listOf(
      Invoice(
        id = "INV-2026-001",
        facilityName = "مستشفى أطفال الرمل بالإسكندرية",
        invoiceType = InvoiceType.MAINTENANCE_CONTRACT,
        issueDate = "2026-01-10",
        dueDate = "2026-02-10",
        totalAmount = 200000,
        paidAmount = 200000,
        paymentStatus = PaymentStatus.PAID_FULL
      ),
      Invoice(
        id = "INV-2026-002",
        facilityName = "مستشفى أبو الريش الياباني (العيادات الخارجية)",
        invoiceType = InvoiceType.MAINTENANCE_CONTRACT,
        issueDate = "2026-01-15",
        dueDate = "2026-02-15",
        totalAmount = 150000,
        paidAmount = 75000,
        paymentStatus = PaymentStatus.PARTIALLY_PAID
      ),
      Invoice(
        id = "INV-2026-003",
        facilityName = "معهد ناصر للبحوث والعلاج",
        invoiceType = InvoiceType.REPAIR_ESTIMATE,
        issueDate = "2026-10-18",
        dueDate = "2026-11-18",
        totalAmount = 200000,
        paidAmount = 100000, // الدفعة المقدمة 50%
        paymentStatus = PaymentStatus.PARTIALLY_PAID
      ),
      Invoice(
        id = "INV-2026-004",
        facilityName = "مستشفى أبو الريش الياباني للعيادات الخارجية",
        invoiceType = InvoiceType.REPAIR_ESTIMATE,
        issueDate = "2026-10-23",
        dueDate = "2026-11-23",
        totalAmount = 34000,
        paidAmount = 34000,
        paymentStatus = PaymentStatus.PAID_FULL
      ),
      Invoice(
        id = "INV-2026-005",
        facilityName = "مركز أورام الفيوم",
        invoiceType = InvoiceType.MAINTENANCE_CONTRACT,
        issueDate = "2026-02-01",
        dueDate = "2026-03-01",
        totalAmount = 90000,
        paidAmount = 90000,
        paymentStatus = PaymentStatus.PAID_FULL
      ),
      Invoice(
        id = "INV-2026-006",
        facilityName = "مستشفى جامعة القاهرة التخصصي للأطفال",
        invoiceType = InvoiceType.MAINTENANCE_CONTRACT,
        issueDate = "2026-03-01",
        dueDate = "2026-04-01",
        totalAmount = 75000,
        paidAmount = 75000,
        paymentStatus = PaymentStatus.PAID_FULL
      )
    )
  )
  val invoices: StateFlow<List<Invoice>> = _invoices.asStateFlow()

  // 10. Installed Base Hub
  private val _installedDevices = MutableStateFlow<List<InstalledDevice>>(
    listOf(
      InstalledDevice("DEV-01", "مستشفى بنها الجامعي", "القليوبية", "EMG 4ch MyoQuick", "EMG", "BNH-EMG4-8921", "2023-05-10", "عقد صيانة سنوي", "يعمل بكفاءة 100%", "01060368000"),
      InstalledDevice("DEV-02", "مستشفى مصر للطيران ألماظة", "القاهرة", "EMG 4ch Point", "EMG", "MS-EMG4-5541", "2022-09-15", "عقد صيانة سنوي", "يعمل بكفاءة 100%", "01010800070"),
      InstalledDevice("DEV-03", "مستشفى عين شمس التخصصي بالعبور", "القليوبية", "EMG 2ch Duo", "EMG", "ASU-OBR-2210", "2024-01-20", "عقد صيانة سنوي", "يعمل بكفاءة 100%", "01141968967"),
      InstalledDevice("DEV-04", "مركز أورام الفيوم", "الفيوم", "EMG 2ch Compact", "EMG", "FYM-EMG2-7721", "2023-11-01", "عقد صيانة سنوي", "يعمل بكفاءة 100%", "01153861300"),
      InstalledDevice("DEV-05", "مركز أورام الفيوم", "الفيوم", "EEG 34ch NeuroSpectrum", "EEG", "FYM-EEG34-9904", "2023-11-01", "عقد صيانة سنوي", "يعمل بكفاءة 100%", "01153861300"),
      InstalledDevice("DEV-06", "مستشفى أبو الريش الياباني (العيادات الخارجية)", "القاهرة", "EMG Matrix 1005 High-End", "EMG", "ABR-MTX-1005", "2024-02-10", "عقد صيانة سنوي", "يعمل بكفاءة 100%", "01026324069"),
      InstalledDevice("DEV-07", "مستشفى أبو الريش الياباني (العيادات الخارجية)", "القاهرة", "EEG 34ch BrainMaster", "EEG", "ABR-EEG34-3112", "2024-02-10", "عقد صيانة سنوي", "يعمل بكفاءة 100%", "01026324069"),
      InstalledDevice("DEV-08", "مستشفى جامعة القاهرة للأطفال", "القاهرة", "EMG 4ch clinical", "EMG", "CUH-EMG4-1029", "2023-08-25", "عقد صيانة سنوي", "يعمل بكفاءة 100%", "01004549273"),
      InstalledDevice("DEV-09", "مركز الزهور د/ محمد عمارة", "الشرقية", "EEG 34ch NeuroSmart", "EEG", "ZHR-EEG34-6014", "2024-03-01", "عقد صيانة سنوي", "يعمل بكفاءة 100%", "01013001140"),
      InstalledDevice("DEV-10", "مستشفى أطفال الرمل بالإسكندرية", "الإسكندرية", "EMG 2ch pediatric", "EMG", "RAM-EMG2-4419", "2023-04-12", "عقد صيانة سنوي", "يعمل بكفاءة 100%", "01275714887"),
      InstalledDevice("DEV-11", "مركز الفتح للأشعة بالمعادي", "القاهرة", "EMG 2ch Scout", "EMG", "FAT-EMG2-3301", "2024-01-05", "عقد صيانة سنوي", "يعمل بكفاءة 100%", "01001792923"),
      InstalledDevice("DEV-12", "معمل الدكتور أحمد عبدالله (Rolex LAB)", "الجيزة", "EMG 4ch Elite", "EMG", "RLX-EMG4-2024", "2025-04-20", "فترة الضمان حتى 2027", "يعمل بكفاءة 100%", "01001234567"),
      InstalledDevice("DEV-13", "معمل الدكتور أحمد عبدالله (Rolex LAB)", "الجيزة", "EEG 34ch Precision", "EEG", "RLX-EEG34-2024", "2025-04-20", "فترة الضمان حتى 2027", "يعمل بكفاءة 100%", "01001234567"),
      InstalledDevice("DEV-14", "مستشفى أبو قير العام", "الإسكندرية", "EMG 2ch Standard", "EMG", "ABQ-EMG2-8812", "2025-04-20", "فترة الضمان حتى 2027", "يعمل بكفاءة 100%", "01223456789"),
      InstalledDevice("DEV-15", "مستشفى أبو قير العام", "الإسكندرية", "EEG 34ch Hospital Pro", "EEG", "ABQ-EEG34-9934", "2025-04-20", "فترة الضمان حتى 2027", "يعمل بكفاءة 100%", "01223456789"),
      InstalledDevice("DEV-16", "مركز نيورو أدفنس Neuro Advance", "الجيزة", "EMG Matrix Advanced", "EMG", "NAD-EMG-5001", "2024-12-01", "فترة الضمان حتى 2026", "يعمل بكفاءة 100%", "01112233445"),
      InstalledDevice("DEV-17", "مركز نيورو أدفنس Neuro Advance", "الجيزة", "EEG High-Density 64ch", "EEG", "NAD-EEG-5002", "2024-12-01", "فترة الضمان حتى 2026", "يعمل بكفاءة 100%", "01112233445")
    )
  )
  val installedDevices: StateFlow<List<InstalledDevice>> = _installedDevices.asStateFlow()

  // Actions & Mutations
  fun completeOverdueTask(taskId: String) {
    _overdueTasks.update { list ->
      list.map { if (it.id == taskId) it.copy(isCompleted = true) else it }
    }
  }

  fun reassignOverdueTask(taskId: String, newEngineer: String) {
    _overdueTasks.update { list ->
      list.map { if (it.id == taskId) it.copy(assignedEngineer = newEngineer) else it }
    }
  }

  fun convertTicketToEstimate(ticketId: String) {
    val ticket = _tickets.value.find { it.id == ticketId } ?: return
    val diag = ticket.diagnosticReport
    val cost = diag?.estimatedRepairCost?.takeIf { it > 0 } ?: 45000L

    val newEstimate = RepairEstimate(
      id = "EST-${System.currentTimeMillis() % 10000}",
      facilityName = ticket.clientName,
      deviceModel = ticket.deviceModel,
      scopeOfWork = diag?.findings ?: "إصلاح عطل وتغيير قطع الغيار التالفة بعد المعاينة الميدانية",
      totalAmount = cost,
      approvalDate = "2026-10-28",
      targetCompletionDate = "2026-11-25",
      status = EstimateStatus.APPROVED,
      assignedEngineer = ticket.assignedEngineer,
      sourceTicketId = ticket.id,
      partsRequired = diag?.damagedParts ?: listOf("قطع غيار معتمدة")
    )

    _repairEstimates.update { listOf(newEstimate) + it }
    _tickets.update { list ->
      list.map { if (it.id == ticketId) it.copy(status = TicketStatus.CONVERTED_TO_ESTIMATE) else it }
    }
  }

  fun addDiagnosticReport(ticketId: String, report: DiagnosticReport) {
    _tickets.update { list ->
      list.map {
        if (it.id == ticketId) it.copy(
          diagnosticReport = report,
          status = TicketStatus.DIAGNOSED
        ) else it
      }
    }
  }

  fun addTicket(ticket: FieldServiceTicket) {
    _tickets.update { listOf(ticket) + it }
  }

  fun renewContract(contractId: String, newEndDate: String) {
    _contracts.update { list ->
      list.map {
        if (it.id == contractId) it.copy(
          endDate = newEndDate,
          status = ContractStatus.ACTIVE
        ) else it
      }
    }
  }

  fun updatePipelineStage(orderId: String, newStage: PipelineStage) {
    _supplyOrders.update { list ->
      list.map {
        if (it.orderId == orderId) it.copy(currentStage = newStage) else it
      }
    }
  }

  // Export generators: UTF-8 BOM CSV & TSV
  fun generateContractsCsv(): String {
    val bom = "\uFEFF"
    val header = "م,اسم المنشأة,المحافظة,المدينة,العنوان,قيمة العقد (ج.م),الزيارات السنوية,الزيارات المنجزة,نوع الأجهزة,عدد الأجهزة,مسؤول التواصل,رقم الهاتف,الأرقام التسلسلية,تاريخ البدء,تاريخ الانتهاء,المهندس المكلف,شامل قطع الغيار\n"
    val rows = _contracts.value.mapIndexed { index, c ->
      "${index + 1},\"${c.facilityName}\",\"${c.governorate}\",\"${c.city}\",\"${c.address}\",${c.annualValue},${c.visitsPerYear},${c.visitsCompleted},\"${c.deviceType}\",${c.deviceCount},\"${c.contactPerson}\",\"${c.phone}\",\"${c.serialNumbers.joinToString(";")}\",\"${c.startDate}\",\"${c.endDate}\",\"${c.assignedEngineer}\",${if (c.includesSpareParts) "نعم" else "لا"}"
    }.joinToString("\n")
    return bom + header + rows
  }

  fun generateContractsTsv(): String {
    val header = "م\tاسم المنشأة\tالمحافظة\tالمدينة\tالعنوان\tقيمة العقد (ج.م)\tالزيارات السنوية\tالزيارات المنجزة\tنوع الأجهزة\tعدد الأجهزة\tمسؤول التواصل\tرقم الهاتف\tالأرقام التسلسلية\tتاريخ البدء\tتاريخ الانتهاء\tالمهندس المكلف\tشامل قطع الغيار\n"
    val rows = _contracts.value.mapIndexed { index, c ->
      "${index + 1}\t${c.facilityName}\t${c.governorate}\t${c.city}\t${c.address}\t${c.annualValue}\t${c.visitsPerYear}\t${c.visitsCompleted}\t${c.deviceType}\t${c.deviceCount}\t${c.contactPerson}\t${c.phone}\t${c.serialNumbers.joinToString(";")}\t${c.startDate}\t${c.endDate}\t${c.assignedEngineer}\t${if (c.includesSpareParts) "نعم" else "لا"}"
    }.joinToString("\n")
    return header + rows
  }

  fun generateWarrantyCsv(): String {
    val bom = "\uFEFF"
    val header = "م,اسم العميل / المنشأة,المحافظة / المدينة,العنوان,نوع المنشأة,عدد الأجهزة,أنواع الأجهزة,تاريخ انتهاء الضمان,مسؤول التواصل,رقم الهاتف,الأرقام التسلسلية,حالة التشغيل,المهندس المسؤول\n"
    val rows = _warrantyClients.value.mapIndexed { index, w ->
      "${index + 1},\"${w.clientName}\",\"${w.city}\",\"${w.location}\",\"${w.facilityType}\",${w.deviceCount},\"${w.deviceTypes}\",\"${w.warrantyExpiryDate}\",\"${w.contactPerson}\",\"${w.phone}\",\"${w.serialNumbers.joinToString(";")}\",\"${w.operationalStatusPercent}%\",\"${w.assignedEngineer}\""
    }.joinToString("\n")
    return bom + header + rows
  }

  fun generateWarrantyTsv(): String {
    val header = "م\tاسم العميل / المنشأة\tالمحافظة / المدينة\tالعنوان\tنوع المنشأة\tعدد الأجهزة\tأنواع الأجهزة\tتاريخ انتهاء الضمان\tمسؤول التواصل\tرقم الهاتف\tالأرقام التسلسلية\tحالة التشغيل\tالمهندس المسؤول\n"
    val rows = _warrantyClients.value.mapIndexed { index, w ->
      "${index + 1}\t${w.clientName}\t${w.city}\t${w.location}\t${w.facilityType}\t${w.deviceCount}\t${w.deviceTypes}\t${w.warrantyExpiryDate}\t${w.contactPerson}\t${w.phone}\t${w.serialNumbers.joinToString(";")}\t${w.operationalStatusPercent}%\t${w.assignedEngineer}"
    }.joinToString("\n")
    return header + rows
  }

  fun generateCombinedTsv(): String {
    val header = "الفئة\tاسم المنشأة\tالمحافظة / المدينة\tالأجهزة\tالعدد\tالقيمة / الضمان\tالمسؤول\tرقم الهاتف\tالمهندس المكلف\n"
    val cRows = _contracts.value.map {
      "عقد صيانة\t${it.facilityName}\t${it.governorate} - ${it.city}\t${it.deviceType}\t${it.deviceCount}\t${it.annualValue} ج.م\t${it.contactPerson}\t${it.phone}\t${it.assignedEngineer}"
    }
    val wRows = _warrantyClients.value.map {
      "فترة ضمان\t${it.clientName}\t${it.city} - ${it.location}\t${it.deviceTypes}\t${it.deviceCount}\tضمان حتى ${it.warrantyExpiryDate}\t${it.contactPerson}\t${it.phone}\t${it.assignedEngineer}"
    }
    return header + (cRows + wRows).joinToString("\n")
  }

  fun generateIcsCalendar(): String {
    val sb = StringBuilder()
    sb.append("BEGIN:VCALENDAR\n")
    sb.append("VERSION:2.0\n")
    sb.append("PRODID:-//Delta Medical CMMS//Neuro Diagnostic Calendar//AR\n")
    sb.append("CALSCALE:GREGORIAN\n")
    sb.append("METHOD:PUBLISH\n")

    _contracts.value.forEach { c ->
      sb.append("BEGIN:VEVENT\n")
      sb.append("UID:pm-${c.id}@deltamedical.com\n")
      sb.append("SUMMARY:زيارة صيانة وقائية - ${c.facilityName}\n")
      sb.append("DESCRIPTION:فحص وقائي دوري لجهاز ${c.deviceType} - المهندس المكلف: ${c.assignedEngineer} - هاتف: ${c.phone}\n")
      sb.append("LOCATION:${c.address}، ${c.city}، ${c.governorate}\n")
      sb.append("DTSTART:20261115T090000Z\n")
      sb.append("DTEND:20261115T120000Z\n")
      sb.append("STATUS:CONFIRMED\n")
      sb.append("END:VEVENT\n")
    }

    _warrantyClients.value.forEach { w ->
      sb.append("BEGIN:VEVENT\n")
      sb.append("UID:war-${w.id}@deltamedical.com\n")
      sb.append("SUMMARY:فحص مجاني ومعايرة معاوقة - ${w.clientName}\n")
      sb.append("DESCRIPTION:فحص ربع سنوي دوري لأجهزة ${w.deviceTypes} ومعايرة Impedance - المهندس: ${w.assignedEngineer}\n")
      sb.append("LOCATION:${w.location}، ${w.city}\n")
      sb.append("DTSTART:20261120T100000Z\n")
      sb.append("DTEND:20261120T130000Z\n")
      sb.append("STATUS:CONFIRMED\n")
      sb.append("END:VEVENT\n")
    }

    sb.append("END:VCALENDAR\n")
    return sb.toString()
  }

  companion object {
    @Volatile
    private var instance: DeltaMedicalRepository? = null

    fun getInstance(): DeltaMedicalRepository {
      return instance ?: synchronized(this) {
        instance ?: DeltaMedicalRepository().also { instance = it }
      }
    }
  }
}
