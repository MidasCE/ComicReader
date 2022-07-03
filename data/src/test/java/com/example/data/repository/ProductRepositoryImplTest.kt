package com.example.data.repository

import com.example.data.api.ProductAPI
import com.example.data.db.ProductDao
import com.example.data.entity.*
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Single
import model.Attribute
import model.Category
import model.Image
import model.Product
import org.junit.Test

import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import repository.comic.ComicRepository
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class ProductRepositoryImplTest {

    @Mock
    lateinit var api: ProductAPI

    @Mock
    lateinit var dao: ProductDao

    private lateinit var productRepository: ComicRepository

    @Before
    fun setUp() {
        productRepository = ProductRepositoryImpl(api, dao)
    }

    @Test
    fun `Test getProducts | should return data as a domain type`() {
        val date = Date()

        val productResponse = ProductEntity(
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
            categories = listOf(CategoryEntity("categoryId", "category", 1, isDefault = false, recentlyAdded = false)),
            attributes = listOf(AttributeEntity("attributeID", "title", "unit", "value")),
            images = mapOf("120" to ImageEntity("src", "url", 120), "500" to ImageEntity("src", "url", 520))
        )
        val response = Response(listOf(productResponse))

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

        whenever(api.getProducts(any(), any())).thenReturn(Single.just(response))

        productRepository.getProducts(listOf(1000)).test()
            .assertValue(listOf(product))

        verify(dao).insertAll(response.productList)
    }

    @Test
    fun `Test getProducts | should return data from dao if remote got error`() {
        val date = Date()

        val productResponse = ProductEntity(
            "id",
            "sku",
            "title",
            "description",
            "listPrice",
            isVatable = false,
            isForSale = false,
            ageRestricted = false,
            tags = emptyList(),
            createdAt = date,
            categories = listOf(CategoryEntity("categoryId", "category", 1, isDefault = false, recentlyAdded = false)),
            attributes = listOf(AttributeEntity("attributeID", "title", "unit", "value")),
            images = mapOf("120" to ImageEntity("src", "url", 120), "500" to ImageEntity("src", "url", 520))
        )

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

        val throwable = Throwable("error")
        whenever(api.getProducts(any(), any())).thenReturn(
            Single.error(
                throwable
            )
        )
        whenever(dao.getAll()).thenReturn(listOf(productResponse))

        productRepository.getProducts(listOf(1000)).test()
            .assertValue(listOf(product))
    }
}
