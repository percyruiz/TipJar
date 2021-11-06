package com.example.tipjar.database.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.example.tipjar.database.entity.Tip

@Dao
interface TipJarDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insert(vararg tipJar: Tip)

  @Query("SELECT * FROM tip where id = :id")
  fun getTip(id: Long): Tip

  @Query("SELECT * FROM tip ORDER BY timestamp DESC")
  fun getAll(): PagingSource<Int, Tip>

  @Query("SELECT * FROM tip WHERE timestamp BETWEEN :start AND :end ORDER BY timestamp DESC")
  fun getWithRange(start: Long, end: Long): PagingSource<Int, Tip>

  @Delete
  fun delete(tip: Tip)
}
