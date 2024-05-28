package com.example.mc_a3_1


import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface AnglesDao {
    @Upsert
    suspend fun upsertAngles(angles: Angles)

    @Query("SELECT * FROM anglesTable")
    fun getAllAngles(): Flow<List<Angles>>

}
