package com.interview.pdfApp.controller

import com.interview.pdfApp.entity.Item
import com.interview.pdfApp.service.ItemService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import org.springframework.http.HttpStatus
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class ControllerTest {

    private val itemService = mockk<ItemService>(relaxed = true)
    private val cut = Controller(itemService)

    @Test
    fun testGenerateItems(){
        val result = cut.generateItems()
        verify (exactly = 1) { itemService.generateItems() }
        assertNotNull(result)
        assertEquals(result.statusCode, HttpStatus.CREATED)
        assertNotNull(result.headers)
        assertEquals(result.body, "Items generated successfully")
    }

    @Test
    fun testGeneratePdf(){
        val ids = setOf(1L, 2L, 8L)
        every { itemService.generatePdfStream(ids) } .returns("574935776589.pdf")
        val result = cut.generatePdf(ids)
        verify (exactly = 1) { itemService.generatePdfStream(ids) }
        assertNotNull(result)
        assertNotNull(result.headers)
        assertEquals(result.statusCode, HttpStatus.OK)
        assertNotNull(result.body)
    }

    @Test
    fun testExportPdf(){
        every { itemService.getFile("test") } .returns("TEST".toByteArray())
        val result = cut.exportPdf("test")
        verify (exactly = 1) { itemService.getFile("test") }
        assertNotNull(result)
        assertNotNull(result.headers)
        assertEquals(result.statusCode, HttpStatus.OK)
    }

    @Test
    fun testGetFirstItems(){
         val itemList = listOf(
            Item("Item 1", 1),
            Item("Item 2", 10),
            Item("Item 3", 38)
        )
        every { itemService.findTop5() } .returns(itemList)
        val result = cut.firstItems
        verify (exactly = 1) { itemService.findTop5() }
        assertNotNull(result)
        assertNotNull(result.headers)
        assertEquals(result.statusCode, HttpStatus.OK)
        assertNotNull(result.body)
        assertEquals(result.body, itemList)
    }

}