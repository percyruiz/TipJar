package com.example.tipjar

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asLiveData
import com.example.tipjar.repository.TipJarRepository
import com.example.tipjar.ui.addtip.AddTipViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import com.example.tipjar.data.Result
import com.jraska.livedata.test
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
class AddTipViewModelTest {

  @get:Rule
  val instantExecutorRule = InstantTaskExecutorRule()

  private val dispatcher = TestCoroutineDispatcher()

  private lateinit var underTest: AddTipViewModel

  @MockK
  private lateinit var repository: TipJarRepository


  @Before
  fun setup() {
    MockKAnnotations.init(this)
    Dispatchers.setMain(dispatcher)
    underTest = AddTipViewModel(repository, dispatcher)
  }

  @After
  fun tearDown() {
    Dispatchers.resetMain()
    dispatcher.cleanupTestCoroutines()
  }

  @InternalCoroutinesApi
  @Test
  fun `Should be able to save tip successfully`() = runBlockingTest {
    val result = Result.success(true)
    coEvery { repository.addTip(any(), any(), any()) } returns flow {
      emit(result)
    }
    underTest.saveTip(100.00f, 10f, "photoPath")

    underTest.addTipFlow.asLiveData().test().assertValue(result)
  }
}
