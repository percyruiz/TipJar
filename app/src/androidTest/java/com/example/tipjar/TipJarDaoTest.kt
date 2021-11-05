package com.example.tipjar

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.tipjar.database.TipDatabase
import com.example.tipjar.database.entity.Tip
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class AccountDAOTest {

  @get:Rule
  val instantExecutorRule = InstantTaskExecutorRule()

  private val dispatcher = TestCoroutineDispatcher()

  private lateinit var db: TipDatabase

  @Before
  fun setup() {
    Dispatchers.setMain(dispatcher)
    db = Room.inMemoryDatabaseBuilder(
      InstrumentationRegistry.getContext(),
      TipDatabase::class.java
    )
      .allowMainThreadQueries()
      .build()
  }

  @After
  fun tearDown() {
    Dispatchers.resetMain()
    db.close()
  }

  @Test
  fun insertAndGet() = runBlockingTest {
    val tip = Tip(1, 100.0f, 10.0f, "path", System.currentTimeMillis())
    db.tipJarDao().insert(tip)
    val db = async {
      db.tipJarDao().getTip(1)
    }

    db.await().let {
      Assert.assertEquals(tip, it)
    }
  }
}
