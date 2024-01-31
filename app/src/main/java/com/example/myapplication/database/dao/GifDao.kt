package com.example.myapplication.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myapplication.models.Data
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

@Dao
interface GifDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGif(data : Data)

    @Query("SELECT * FROM gif_table")
    fun getAllGifs() : Flowable<List<Data>>

    @Query("SELECT COUNT(*) FROM gif_table")
    fun getNumberOfGifs() : LiveData<Int>

    @Query("DELETE FROM gif_table WHERE id=:id")
    fun deleteGif(id : String) : Completable
}