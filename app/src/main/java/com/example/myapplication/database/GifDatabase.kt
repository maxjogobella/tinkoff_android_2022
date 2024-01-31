package com.example.myapplication.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myapplication.models.Data
import com.example.myapplication.database.dao.GifDao

@Database(entities = [Data::class], version = 6, exportSchema = false)
abstract class GifDatabase : RoomDatabase() {

    companion object {
        private const val DB_NAME : String = "1234567.db"

        private var db : GifDatabase? = null
        fun getInstance(context : Context) : GifDatabase {
            synchronized(this) {
                db?.let { return it }
                val instance = Room.databaseBuilder(
                    context,
                    GifDatabase::class.java,
                    DB_NAME
                ).fallbackToDestructiveMigration().build()
                db = instance
                return instance
            }
        }
    }

    abstract fun gifDao() : GifDao
}