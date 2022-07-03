package com.example.domain.interactor.comic

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Single
import model.Product
import org.junit.Test

import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import repository.comic.ComicRepository
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class ProductInteractorTest {

    @Mock
    lateinit var repository: ComicRepository

    private lateinit var productInteractor: ComicInteractor

    @Before
    fun setUp() {
        productInteractor = ComicInteractorImpl(repository)
    }

    @Test
    fun `Test getProducts | should fetch directly from repository`() {
        val product = Product(id = "id",
            sku = "sku",
            title = "title",
            description = "description",
            listPrice = "listPrice",
            isVatable = false,
            isForSale = false,
            ageRestricted = false,
            createdAt = Date(),
            tags = emptyList(),
            categories = emptyList(),
            attributes = emptyList(),
            images = emptyMap()
        )

        whenever(repository.getProducts(any())).thenReturn(Single.just(listOf(product)))

        productInteractor.getProducts(listOf(1000)).test()
            .assertValue(listOf(product))
    }

}
