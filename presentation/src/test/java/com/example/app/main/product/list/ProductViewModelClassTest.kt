package com.example.app.main.product.list

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ProductViewModelClassTest {

    @Test
    fun `firstClosestWidthImage | return null when empty list parameter`() {
        assertNull(firstClosestWidthImage(1000, emptyList()))
    }

    @Test
    fun `firstClosestWidthImage | return image with closest width when empty list parameter`() {
        val imageViewModel1 = ImageViewModel("src", "url", 900)
        val imageViewModel2 = ImageViewModel("src", "url", 200)
        val imageViewModel3 = ImageViewModel("src", "url", 500)
        assertEquals(imageViewModel1, firstClosestWidthImage(1000, listOf(imageViewModel1, imageViewModel2, imageViewModel3)))
    }
}

