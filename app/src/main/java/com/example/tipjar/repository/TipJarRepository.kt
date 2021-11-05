package com.example.tipjar.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.tipjar.data.Result
import com.example.tipjar.database.TipDatabase
import com.example.tipjar.database.entity.Tip
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

interface TipJarRepository {
  suspend fun addTip(total: Float, tipValue: Float, photoPath: String?): Flow<Result<Boolean>>
  suspend fun getAllTips(): Flow<PagingData<Tip>>
  suspend fun getTipsWithRange(start: Long, end: Long): Flow<PagingData<Tip>>
}

/**
 * Data source class for [Tip] related data
 *
 * @property db App's database instance
 */
class TipJarRepositoryImpl(val db: TipDatabase) : TipJarRepository {

  override suspend fun addTip(
    total: Float,
    tipValue: Float,
    photoPath: String?
  ): Flow<Result<Boolean>> {
    return flow {
      val result = safeDbCall {
        db.tipJarDao()
          .insert(
            Tip(
              total = total,
              tipValue = tipValue,
              path = photoPath,
              timestamp = System.currentTimeMillis()
            )
          )
        true
      }
      emit(result)

    }.flowOn(Dispatchers.IO)
  }

  override suspend fun getAllTips(): Flow<PagingData<Tip>> = Pager(
    config = PagingConfig(pageSize = 60, enablePlaceholders = true, maxSize = 200)
  ) {
    db.tipJarDao().getAll()
  }.flow

  override suspend fun getTipsWithRange(start: Long, end: Long): Flow<PagingData<Tip>> = Pager(
    config = PagingConfig(pageSize = 60, enablePlaceholders = true, maxSize = 200)
  ) {
    db.tipJarDao().getWithRange(start, end)
  }.flow

  private suspend fun <T> safeDbCall(dbCall: suspend () -> T): Result<T> {
    return try {
      val result = dbCall()
      if (result != null) {
        Result.success(result)
      } else {
        Result.error("Something went wrong")
      }
    } catch (e: Exception) {
      Result.error("Something went wrong, $e")
    }
  }
}
