package com.example.mc_a3_1

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Angles::class],
    version = 1
)
abstract class AnglesDatabase : RoomDatabase() {
    abstract fun dao(): AnglesDao

    companion object {
        @Volatile
        private var Instance: AnglesDatabase? = null

        fun getDatabase(context: Context): AnglesDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, AnglesDatabase::class.java, "anglesTable")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}