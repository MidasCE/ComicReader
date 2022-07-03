package com.example.app.productDetails.presenter

import com.example.app.main.product.list.*
import com.example.app.productDetails.view.ProductDetailsView
import com.nhaarman.mockito_kotlin.verify
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ProductDetailsPresenterImplTest {

    @Mock
    lateinit var view: ProductDetailsView

    private lateinit var presenter: ProductDetailsPresenterImpl

    @Before
    fun setUp() {
        presenter = ProductDetailsPresenterImpl(view)
    }

    @Test
    fun `updateItemDetails save in memory`() {
        val viewModel = ProductViewModel(
            "id",
            "title",
            "description",
            "listPrice",
            isForSale = false,
            ageRestricted = false,
            tags = listOf(),
            categories = listOf(CategoryViewModel("category", false)),
            attributes = listOf(AttributeViewModel("title", "unit", "value")),
            images = listOf(ImageViewModel("src", "url", 120), ImageViewModel("src", "url", 520))
        )

        presenter.updateItemDetails(viewModel)

        assertEquals(viewModel, presenter.currentItemViewModel())
    }

    @Test
    fun `findBestImageDetails return images with closest size`() {
        val imageViewModel1 = ImageViewModel("src", "url", 1000)
        val imageViewModel2 = ImageViewModel("src", "url", 900)

        val viewModel = ProductViewModel(
            "id",
            "title",
            "description",
            "listPrice",
            isForSale = false,
            ageRestricted = false,
            tags = listOf(),
            categories = listOf(CategoryViewModel("category", false)),
            attributes = listOf(AttributeViewModel("title", "unit", "value")),
            images = listOf(imageViewModel1, imageViewModel2)
        )

        presenter.updateItemDetails(viewModel)
        presenter.findBestImageDetails(1080)

        verify(view).showImage(imageViewModel1)
    }
}
