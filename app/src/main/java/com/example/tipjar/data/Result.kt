package com.example.tipjar.data

/**
 * Represents result of querying to the db, wraps the data with enum [Status]
 *
 * @property status Enum wrapper of the result
 * @property data Account data result
 * @property message Error message if status is [Status.ERROR]
 */
data class Result<T>(val status: Status, val data: T?, val message: String?) {

  enum class Status {
    SUCCESS,
    ERROR,
    NONE
  }

  companion object {
    fun <T> success(data: T): Result<T> {
      return Result(Status.SUCCESS, data, null)
    }

    fun <T> error(message: String, data: T? = null): Result<T> {
      return Result(Status.ERROR, data, message)
    }

    fun <T> none(data: T? = null): Result<T> {
      return Result(Status.NONE, data, null)
    }
  }
}
