package com.example.tipjar.repository

import com.example.tipjar.database.TipDatabase
import com.example.tipjar.database.entity.Tip
import com.example.tipjar.data.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

interface TipJarRepository {
  suspend fun addTip(total: String, tipValue: String, photoPath: String?): Flow<Result<Boolean>>
}

/**
 * Data source class for [Tip] related data
 *
 * @property db App's database instance
 */
class TipJarRepositoryImpl(val db: TipDatabase) : TipJarRepository {

  override suspend fun addTip(
    total: String,
    tipValue: String,
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
