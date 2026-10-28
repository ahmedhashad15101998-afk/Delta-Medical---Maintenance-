package com.example.data.model

enum class PipelineStage(val stepNumber: Int, val titleArabic: String) {
  PURCHASE_ORDER(1, "أمر التوريد المعتمد"),
  CUSTOMS_CLEARANCE(2, "الإفراج الجمركي والموانئ"),
  INITIAL_INSPECTION(3, "المعاينة المبدئية والتفريغ"),
  INSTALLATION_TESTING(4, "التركيب والتشغيل التجريبي"),
  TECHNICAL_SIGNOFF(5, "الفحص الفني والاستلام الابتدائي"),
  TRAINING_WARRANTY(6, "التدريب وتسليم شهادة الضمان")
}

data class SupplyItem(
  val id: String,
  val nameArabic: String,
  val category: String, // إبر فحص / إلكترودات / جل ومعجون / ورق تسجيل
  val unit: String, // علبة / عبوة / رول
  val purchasePrice: Double, // سعر الشراء
  val salePrice: Double, // سعر البيع
  val stockQuantity: Int,
  val reorderThreshold: Int, // حد إعادة الطلب
  val isLowStock: Boolean = stockQuantity <= reorderThreshold
)

data class SupplyOrderItem(
  val item: SupplyItem,
  val quantity: Int
)

data class SupplyOrder(
  val orderId: String,
  val clientName: String,
  val orderDate: String,
  val items: List<SupplyOrderItem>,
  val discountPercent: Double = 0.0,
  val taxPercent: Double = 14.0, // 14% VAT
  val currentStage: PipelineStage = PipelineStage.INSTALLATION_TESTING
) {
  val subtotal: Double get() = items.sumOf { it.item.salePrice * it.quantity }
  val discountAmount: Double get() = subtotal * (discountPercent / 100.0)
  val taxableAmount: Double get() = subtotal - discountAmount
  val taxAmount: Double get() = taxableAmount * (taxPercent / 100.0)
  val finalTotal: Double get() = taxableAmount + taxAmount
}
