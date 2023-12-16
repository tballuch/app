package com.interview.pdfApp.controller

import com.interview.pdfApp.service.ItemService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import org.springframework.http.HttpStatus
import java.io.ByteArrayOutputStream
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
        assertEquals(result.statusCode, HttpStatus.OK)
        assertEquals(result.body, "Items generated successfully")
    }

    @Test
    fun testExportPdfs(){
        val ids = setOf(1L, 2L, 8L)
        every { itemService.generatePdfStream(ids) } .returns(ByteArrayOutputStream())
        val result = cut.exportPdf(ids)
        verify (exactly = 1) { itemService.generatePdfStream(ids) }
        assertNotNull(result)
        assertEquals(result.statusCode, HttpStatus.OK)
        assertNotNull(result.body)
    }

}