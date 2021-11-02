package com.example.tipjar.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tipjar.database.entity.Tip

@Dao
interface TipJarDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insert(vararg tipJar: Tip)

  @Query("SELECT * FROM tip where id = :id")
  fun getTip(id: Long): Tip
}
