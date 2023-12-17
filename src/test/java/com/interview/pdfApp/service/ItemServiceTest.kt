package com.interview.pdfApp.service

import com.interview.pdfApp.entity.Item
import com.interview.pdfApp.repository.ItemRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import java.io.File
import kotlin.test.assertNotNull

class ItemServiceTest {

    private val itemRepository = mockk<ItemRepository>(relaxed = true)
    private val cut = ItemService(itemRepository)

    private val itemList = listOf(
            Item("Item 1", 1),
            Item("Item 2", 10),
            Item("Item 3", 38)
    )

    @Test
    fun testGenerateItems() {
        every { itemRepository.save(any()) } .returns(Item("", 1))
        cut.generateItems()
        verify (exactly = 20) { itemRepository.save(any()) }
    }

    @Test
    fun testGeneratePdfStream() {
        val ids = setOf(1L, 2L, 8L)
        every { itemRepository.findAllById(ids) } .returns(itemList)
        val result = cut.generatePdfStream(ids)
        verify (exactly = 1) { itemRepository.findAllById(ids) }
        assertNotNull(result)
    }

    @Test
    fun test() {
        File("pdf/").mkdir()
        File("pdf/" + "test").createNewFile()

        val result = cut.getFile("test")
        assertNotNull(result)
    }

}