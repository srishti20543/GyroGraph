package com.example.mc_a3_1

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "anglesTable")
data class Angles(

    @PrimaryKey
    val seconds: Int,
    val pitch: Float,
    val roll: Float,
    val yaw: Float
) : Serializable
