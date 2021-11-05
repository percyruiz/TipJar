package com.example.tipjar

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asLiveData
import androidx.paging.PagingData
import com.example.tipjar.data.Search
import com.example.tipjar.data.TipFactory
import com.example.tipjar.repository.TipJarRepository
import com.example.tipjar.ui.viewtip.ViewTipsViewModel
import com.example.tipjar.ui.viewtip.ViewTipsViewModel.Companion.SEARCH
import com.jraska.livedata.test
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ViewTipsModelTest {

  @get:Rule
  val instantExecutorRule = InstantTaskExecutorRule()

  private val dispatcher = TestCoroutineDispatcher()

  private lateinit var underTest: ViewTipsViewModel

  @MockK
  private lateinit var repository: TipJarRepository

  @RelaxedMockK
  private lateinit var handle: SavedStateHandle

  private val search = Search(100, 200)
  private val nullSearch = Search(null, null)

  private val tips = listOf(
    TipFactory.createTip(1, 99),
    TipFactory.createTip(2, 150),
    TipFactory.createTip(3, 300)
  )


  @Before
  fun setup() {
    MockKAnnotations.init(this)
    Dispatchers.setMain(dispatcher)
  }

  @After
  fun tearDown() {
    Dispatchers.resetMain()
    dispatcher.cleanupTestCoroutines()
  }

  @InternalCoroutinesApi
  @Test
  fun `Should be able to get tip list`() = runBlockingTest {
    every { handle.contains(SEARCH) } returns false
    every { handle.set(SEARCH, any<Search>()) } just Runs
    every {
      handle.getLiveData<Search>(SEARCH)
    } returns MutableLiveData(nullSearch)
    coEvery { repository.getAllTips() } returns flow {
      emit(PagingData.from(tips))
    }

    underTest = ViewTipsViewModel(repository, handle)

    // assert tips list is populated
    underTest.tips.asLiveData().test().assertHasValue()
  }

  @InternalCoroutinesApi
  @Test
  fun `Should be able to get tip list with date range`() = runBlockingTest {
    every { handle.contains(SEARCH) } returns false
    every { handle.set(SEARCH, any<Search>()) } just Runs
    every {
      handle.getLiveData<Search>(SEARCH)
    } returns MutableLiveData(search)
    coEvery { repository.getTipsWithRange(search.start!!, search.end!!) } returns flow {
      emit(PagingData.from(tips))
    }

    underTest = ViewTipsViewModel(repository, handle)

    // assert tips list is populated
    underTest.tips.asLiveData().test().assertHasValue()
  }
}
