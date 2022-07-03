package com.example.app.main.product.mapper

import com.example.app.main.product.list.*
import junit.framework.TestCase.assertEquals
import model.*
import org.junit.Before
import org.junit.Test
import java.util.*

class ProductViewModelMapperImplTest {

    private lateinit var viewModelMapper: ProductViewModelMapper

    @Before
    fun setUp() {
        viewModelMapper = ProductViewModelMapperImpl()
    }

    @Test
    fun `mapper ItemViewModel`() {
        val date = Date()
        val product = Product(
            "id",
            "sku",
            "title",
            "description",
            "listPrice",
            isVatable = false,
            isForSale = false,
            ageRestricted = false,
            createdAt = date,
            tags = emptyList(),
            categories = listOf(Category("categoryId", "category", 1, isDefault = false, recentlyAdded = false)),
            attributes = listOf(Attribute("attributeID", "title", "unit", "value")),
            images = mapOf("120" to Image("src", "url", 120), "500" to Image("src", "url", 520))
        )

        val resultViewModel = ProductViewModel(
            id = product.id,
            title = product.title,
            description = product.description,
            listPrice = product.listPrice,
            isForSale = product.isForSale,
            ageRestricted = product.ageRestricted,
            createdAt = date,
            tags = product.tags,
            categories = listOf(CategoryViewModel("category", false)),
            attributes = listOf(AttributeViewModel("title", "unit", "value")),
            images = listOf(ImageViewModel("src", "url", 120), ImageViewModel("src", "url", 520))
        )

        assertEquals(resultViewModel, viewModelMapper.map(product))
    }
}
