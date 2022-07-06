package com.example.app.main.product.presenter

import com.example.app.core.SchedulerFactory
import com.example.app.main.product.ProductScreenView
import com.example.app.main.product.list.AttributeViewModel
import com.example.app.main.product.list.CategoryViewModel
import com.example.app.main.product.list.ImageViewModel
import com.example.app.main.product.list.ProductViewModel
import com.example.app.main.product.mapper.ProductViewModelMapper
import com.example.domain.interactor.comic.ComicInteractor
import com.nhaarman.mockito_kotlin.*
import io.reactivex.Single
import io.reactivex.schedulers.TestScheduler
import model.Attribute
import model.Category
import model.Image
import model.Product
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.util.concurrent.TimeUnit

@RunWith(MockitoJUnitRunner::class)
class ProductScreenPresenterImplTest {

    @Mock
    lateinit var schedulerFactory: SchedulerFactory

    @Mock
    lateinit var view: ProductScreenView

    @Mock
    lateinit var productInteractor: ComicInteractor

    @Mock
    lateinit var viewModelMapper: ProductViewModelMapper

    private lateinit var presenter: ProductScreenPresenterImpl

    private lateinit var ioScheduler: TestScheduler

    private lateinit var mainScheduler: TestScheduler

    @Before
    fun setUp() {
        ioScheduler = TestScheduler()
        mainScheduler = TestScheduler()

        whenever(schedulerFactory.io()).thenReturn(ioScheduler)
        whenever(schedulerFactory.main()).thenReturn(mainScheduler)

        presenter = ProductScreenPresenterImpl(
            schedulerFactory,
            view,
            productInteractor,
            viewModelMapper
        )
    }

    @Test
    fun `Test loadProducts`() {
        val product = Product(
            "id",
            "sku",
            "title",
            "description",
            "listPrice",
            isVatable = false,
            isForSale = false,
            ageRestricted = false,
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
            tags = product.tags,
            categories = listOf(CategoryViewModel("category", false)),
            attributes = listOf(AttributeViewModel("title", "unit", "value")),
            images = listOf(ImageViewModel("src", "url", 120), ImageViewModel("src", "url", 520))
        )

        whenever(productInteractor.getProducts(listOf(1000))).thenReturn(
            Single.just(listOf(product))
        )
        whenever(viewModelMapper.map(any())).thenReturn(
            resultViewModel
        )

        presenter.loadItems()
        verify(view).showLoading()

        ioScheduler.triggerActions()
        mainScheduler.triggerActions()
        ioScheduler.advanceTimeBy(1, TimeUnit.SECONDS)
        mainScheduler.advanceTimeBy(1, TimeUnit.SECONDS)

        verify(view).hideLoading()
        verify(viewModelMapper).map(product)
        verify(view).showProducts(listOf(resultViewModel))
    }

    @Test
    fun `Test loadProducts with error`() {
        val throwable = Throwable("error")
        whenever(productInteractor.getProducts((any()))).thenReturn(
            Single.error(
                throwable
            )
        )

        presenter.loadItems()
        verify(view).showLoading()

        ioScheduler.triggerActions()
        mainScheduler.triggerActions()
        ioScheduler.advanceTimeBy(1, TimeUnit.SECONDS)
        mainScheduler.advanceTimeBy(1, TimeUnit.SECONDS)

        verify(view).hideLoading()
        verify(view).showError("error")
        verify(view, never()).showProducts(any())
    }
}
