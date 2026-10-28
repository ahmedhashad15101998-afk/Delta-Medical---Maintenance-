package com.example

import com.example.data.repository.DeltaMedicalRepository
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {

  private val repository = DeltaMedicalRepository.getInstance()

  @Test
  fun testActiveContractsCountAndTotalValue() {
    val contracts = repository.contracts.value
    assertEquals("Should have exactly 9 active maintenance contracts", 9, contracts.size)

    val totalValue = contracts.sumOf { it.annualValue }
    assertEquals("Total contracts annual value should be 638,000 EGP", 638000L, totalValue)

    val totalDevices = contracts.sumOf { it.deviceCount }
    assertEquals("Total devices covered by contracts should be 11", 11, totalDevices)
  }

  @Test
  fun testWarrantyClientsAndDevices() {
    val warranty = repository.warrantyClients.value
    assertEquals("Should have exactly 3 warranty clients", 3, warranty.size)

    val totalWarrantyDevices = warranty.sumOf { it.deviceCount }
    assertEquals("Total warranty devices should be 6", 6, totalWarrantyDevices)
  }

  @Test
  fun testRepairEstimatesQuotations() {
    val estimates = repository.repairEstimates.value
    assertEquals("Should have 2 approved repair estimates", 2, estimates.size)

    val totalEstimates = estimates.sumOf { it.totalAmount }
    assertEquals("Total repair estimates should be 234,000 EGP", 234000L, totalEstimates)
  }

  @Test
  fun testCsvUtf8BomAndTsvExport() {
    val csv = repository.generateContractsCsv()
    assertTrue("CSV must start with UTF-8 BOM byte marker for Arabic Excel", csv.startsWith("\uFEFF"))
    assertTrue("CSV must contain Benha University", csv.contains("مستشفى بنها الجامعي"))
    assertTrue("CSV must contain 638000 contract elements", csv.contains("مستشفى أطفال الرمل بالإسكندرية"))

    val tsv = repository.generateContractsTsv()
    assertTrue("TSV must contain tab separated fields", tsv.contains("\t"))
  }

  @Test
  fun testOverdueTasksEngine() {
    val overdue = repository.overdueTasks.value
    assertTrue("Overdue tasks should be present", overdue.isNotEmpty())
    val critical = overdue.filter { it.severity == com.example.data.model.OverdueSeverity.CRITICAL }
    assertTrue("Should have critical overdue tasks (>14 days)", critical.isNotEmpty())
  }
}
